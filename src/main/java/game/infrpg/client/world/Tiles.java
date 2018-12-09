package game.infrpg.client.world;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Tiles {
	// Don't even think about reordering these
	WATER,
	GRASS,
	SAND,
	DIRT,
	;

	/**
	 * The number of defined enum tiles.
	 */
	public static final int LENGTH = values().length;
	
	public static Tiles fromDataValue(byte dataValue)
	{
		return values()[dataValue];
	}
	
	public static int getMaskFor(Tiles... tiles)
	{
		int mask = 0;
		for (Tiles t : tiles)
		{
			mask |= 1 << t.dataValue;
		}
		return mask;
	}
	
	public static List<Tiles> getTilesFromMask(int mask)
	{
		List<Tiles> tiles = new ArrayList<>(Tiles.LENGTH);
		for (int i = 0; i < Tiles.LENGTH; i++)
		{
			Tiles t = values()[i];
			if ((mask & 1 << i) != 0)
			{
				tiles.add(t);
			}
		}
		return tiles;
	}
	
	/**
	 * The tile index of the enum list.
	 */
	public final byte dataValue;
	
	public final int mask;

	private Tiles() {
		this.dataValue = (byte)ordinal();
		this.mask = 1 << dataValue;
	}
	
}
