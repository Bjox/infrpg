package game.infrpg.server.map;

import game.infrpg.common.util.Constants;
import java.io.Serializable;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Chunk implements Serializable
{

	// Increment this if non-backwards compatible changes are made on the class.
	private static final long serialVersionUID = 1L;

	private final byte[][] tileData;

	/**
	 * The chunk position.
	 */
	public final ReadablePoint position;
	
	/**
	 * Empty constructor for kryo.
	 */
	public Chunk()
	{
		this.tileData = null;
		this.position = null;
	}

	public Chunk(int x, int y)
	{
		this.tileData = new byte[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
		this.position = new Point(x, y);
	}

	public Chunk(Chunk chunk)
	{
		this.tileData = chunk.tileData;
		this.position = chunk.position;
	}

	public void setTile(int x, int y, byte tile)
	{
		tileData[x][y] = tile;
	}

	public byte getTile(int x, int y)
	{
		return tileData[x][y];
	}

	public void setTiles(TileDataSupplier supplier)
	{
		for (int i = 0; i < Constants.CHUNK_SIZE; i++)
		{
			for (int j = 0; j < Constants.CHUNK_SIZE; j++)
			{
				tileData[i][j] = supplier.supplyTileData(i, j);
			}
		}
	}

	@Override
	public String toString()
	{
		return String.format("Chunk(%d,%d)", position.getX(), position.getY());
	}

	public static interface TileDataSupplier
	{

		public byte supplyTileData(int x, int y);

	}

}
