package game.infrpg.client.world.wangtiles;

import game.infrpg.client.world.AbstractChunkProcessor;
import game.infrpg.client.world.IMapChunk;
import game.infrpg.client.world.IMapChunkStorage;
import game.infrpg.client.world.MapChunk;
import game.infrpg.client.world.Tiles;
import game.infrpg.common.util.Constants;
import game.infrpg.server.map.Chunk;
import java.util.concurrent.TimeUnit;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class WangChunkProcessor extends AbstractChunkProcessor
{
	private final IMapChunkStorage chunkStorage;

	@Inject
	public WangChunkProcessor(IMapChunkStorage chunkStorage)
	{
		this.chunkStorage = chunkStorage;
	}

	@Override
	public IMapChunk process(Chunk chunk)
	{
		MapChunk newMapChunk = new MapChunk(chunk.position.getX(), chunk.position.getY(), (x, y) -> processTile(x, y, chunk, null));
		
		int x = chunk.position.getX();
		int y = chunk.position.getY();
		
		reprocessChunkAt(x-1, y, newMapChunk);
		reprocessChunkAt(x, y-1, newMapChunk);
		reprocessChunkAt(x-1, y-1, newMapChunk);
		
		return newMapChunk;
	}
	
	private void reprocessChunkAt(int x, int y, MapChunk original)
	{
		MapChunk chunkToReprocess = chunkStorage.getMapChunk(x, y);
		
		if (chunkToReprocess == null)
		{
			return;
		}
		
		chunkToReprocess.setAllTileData((xx, yy) -> processTile(xx, yy, chunkToReprocess, original));
	}
	
	private int processTile(int x, int y, Chunk chunk, MapChunk original)
	{	
		Tiles c1 = getTileAt(x + 1, y, chunk, original);
		Tiles c2 = getTileAt(x, y, chunk, original);
		Tiles c4 = getTileAt(x, y + 1, chunk, original);
		Tiles c8 = getTileAt(x + 1, y + 1, chunk, original);
		
		return WangTileset.process(c1, c2, c4, c8) << 8 | chunk.getTileData(x, y); // Put original chunk tile data in first byte.
	}
	
	/**
	 * Gets a tile data value relative to the provided chunk.
	 * For x and y values bigger than Constants.CHUNK_SIZE, neighbouring
	 * chunks will be examined if they exist. Otherwise Tiles.WATER is returned.<br>
	 * x and y must be non-negative.
	 * @param x
	 * @param y
	 * @param chunk
	 * @param original
	 * @return 
	 */
	private Tiles getTileAt(int x, int y, Chunk chunk, MapChunk original)
	{
		if (x < Constants.CHUNK_SIZE && y < Constants.CHUNK_SIZE)
		{
			return chunk.getTileAt(x, y);
		}
		
		int otherChunkX = chunk.position.getX() + x / Constants.CHUNK_SIZE;
		int otherChunkY = chunk.position.getY() + y / Constants.CHUNK_SIZE;

		MapChunk otherChunk = getChunk(otherChunkX, otherChunkY, original);
		
		if (otherChunk == null)
		{
			return Tiles.WATER;
		}
		
		return otherChunk.getTileAt(x % Constants.CHUNK_SIZE, y % Constants.CHUNK_SIZE);
	}
	
	/**
	 * Gets the chunk at x,y from the chunk storage, unless it corresponds
	 * to the provided original map chunk. In that case, original is returned.
	 * @param x
	 * @param y
	 * @param original
	 * @return 
	 */
	private MapChunk getChunk(int x, int y, MapChunk original)
	{
		if (original != null && x == original.position.getX() && y == original.position.getY())
		{
			return original;
		}
		return chunkStorage.getMapChunk(x, y);
	}

}
