package game.infrpg.client.world.wangtiles;

import game.infrpg.client.world.Tiles;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Variation
{
	private final Tiles[] types;
	private final String stringValue;
	public final int mask;
	public final int length;

	public Variation(Tiles... types)
	{
		this.types = new Tiles[types.length];
		System.arraycopy(types, 0, this.types, 0, types.length);

		StringBuilder str = new StringBuilder();
		for (Tiles type : types)
		{
			str.append("_").append(type.name().toLowerCase());
		}
		stringValue = str.substring(1);
		
		this.mask = Tiles.getMaskFor(types);
		this.length = types.length;
	}

	public Tiles getType(int i)
	{
		return types[i];
	}

	@Override
	public String toString()
	{
		return stringValue;
	}
}
