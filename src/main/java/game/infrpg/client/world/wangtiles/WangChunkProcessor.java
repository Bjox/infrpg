package game.infrpg.client.world.wangtiles;

import game.infrpg.client.world.IChunkProcessor;
import game.infrpg.client.world.IMapChunk;
import game.infrpg.client.world.IMapChunkStorage;
import game.infrpg.client.world.MapChunk;
import game.infrpg.client.world.Tiles;
import game.infrpg.common.util.Constants;
import game.infrpg.server.map.Chunk;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class WangChunkProcessor implements IChunkProcessor
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
		IMapChunk mapChunk = new MapChunk(chunk.position.getX(), chunk.position.getY(), (x, y) -> processTile(x, y, chunk));
		chunkStorage.storeMapChunk(mapChunk);
		
		int x = chunk.position.getX();
		int y = chunk.position.getY();
		
		reprocessChunkAt(x-1, y);
		reprocessChunkAt(x, y-1);
		reprocessChunkAt(x-1, y-1);
		
		return mapChunk;
	}
	
	private void reprocessChunkAt(int x, int y)
	{
		MapChunk chunkToReprocess = chunkStorage.getMapChunk(x, y);
		
		if (chunkToReprocess == null)
		{
			return;
		}
		
		chunkToReprocess.setAllTileData((xx, yy) -> processTile(xx, yy, chunkToReprocess));
	}

	private int processTile(int x, int y, Chunk chunk)
	{	
		Tiles c1 = getTileAt(x + 1, y, chunk);
		Tiles c2 = getTileAt(x, y, chunk);
		Tiles c4 = getTileAt(x, y + 1, chunk);
		Tiles c8 = getTileAt(x + 1, y + 1, chunk);
		
		return WangTileset.process(c1, c2, c4, c8) << 8 | chunk.getTileData(x, y); // Put original chunk tile data in first byte.
	}
	
	private Tiles getTileAt(int x, int y, Chunk chunk)
	{
		if (x < Constants.CHUNK_SIZE && y < Constants.CHUNK_SIZE)
		{
			return chunk.getTileAt(x, y);
		}
		
		MapChunk otherChunk = chunkStorage.getMapChunk(
			chunk.position.getX() + x / Constants.CHUNK_SIZE,
			chunk.position.getY() + y / Constants.CHUNK_SIZE);
		
		if (otherChunk == null)
		{
			return Tiles.WATER;
		}
		
		return otherChunk.getTileAt(x % Constants.CHUNK_SIZE, y % Constants.CHUNK_SIZE);
	}

	private boolean isEdgeTile(int x, int y)
	{
		return x == Constants.CHUNK_SIZE - 1 || y == Constants.CHUNK_SIZE - 1;
	}

}
