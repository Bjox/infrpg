package game.infrpg.server.service.map;

import game.infrpg.common.util.Constants;
import game.infrpg.server.map.CachedMapStorage;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.Region;
import game.infrpg.server.service.Service;
import java.io.Closeable;
import java.io.IOException;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapService extends Service implements IMapService {

	private final IMapStorage storage;
	private final ILogger logger;

	@Inject
	public MapService(CachedMapStorage mapStorage, ILogger logger) {
		this.storage = mapStorage;
		this.logger = logger;
	}

	@Override
	public void init() throws Exception {
		storage.init();
	}
	
	@Override
	public void close() throws IOException {
		logger.debug("Closing MapService");
		storage.close();
	}

	@Override
	public Chunk getChunk(int x, int y) {
		Region region = getChunkRegion(x, y);

		int localX = x % Constants.REGION_SIZE; // TODO: optimize irem
		int localY = y % Constants.REGION_SIZE;

		return region.getChunk(localX, localY);
	}
	
	/**
	 * Gets the region containing the specified chunk.
	 * @param chunkX
	 * @param chunkY
	 * @return 
	 */
	private Region getChunkRegion(int chunkX, int chunkY) {
		int regionX = chunkX / Constants.REGION_SIZE; // TODO: optimize idiv
		int regionY = chunkY / Constants.REGION_SIZE;
		return storage.getRegion(regionX, regionY);
	}

	

}
