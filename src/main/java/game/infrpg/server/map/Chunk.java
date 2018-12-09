package game.infrpg.server.map;

import game.infrpg.client.world.Tiles;
import game.infrpg.common.util.Constants;
import java.io.Serializable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Chunk extends AbstractChunk implements Serializable, TileDataSupplier
{
	// Increment this if non-backwards compatible changes are made on the class.
	private static final long serialVersionUID = 2L;
	
	protected final byte[][] tileData;
	
	/**
	 * Empty constructor for kryo.
	 */
	public Chunk()
	{
		this.tileData = null;
	}

	public Chunk(int x, int y)
	{
		super(x, y);
		this.tileData = new byte[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
	}

	public Chunk(Chunk orig)
	{
		super(orig);
		this.tileData = orig.tileData;
	}

	public byte getTileData(int x, int y)
	{
		return tileData[x][y];
	}
	
	public Tiles getTileAt(int x, int y)
	{
		return Tiles.fromDataValue(getTileData(x, y));
	}
	
	public void setTileData(int x, int y, byte tile)
	{
		tileData[x][y] = tile;
	}
	
	/**
	 * Sets all tiles in this chunk according to the supplied TileDataSupplier.
	 * @param supplier
	 * @return A reference to this chunk.
	 */
	public final Chunk setTileData(TileDataSupplier supplier)
	{
		for (int i = 0; i < Constants.CHUNK_SIZE; i++)
		{
			for (int j = 0; j < Constants.CHUNK_SIZE; j++)
			{
				tileData[i][j] = (byte)supplier.supplyTileData(i, j);
			}
		}
		return this;
	}
	
	public final TileDataSupplier getTileDataSupplier()
	{
		return (x, y) -> tileData[x][y];
	}
	
	@Override
	public int supplyTileData(int x, int y)
	{
		return tileData[x][y];
	}

	@Override
	public String toString()
	{
		return String.format("Chunk(%d,%d)", position.getX(), position.getY());
	}

	

}
