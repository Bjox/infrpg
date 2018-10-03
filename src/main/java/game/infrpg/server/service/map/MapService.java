package game.infrpg.server.service.map;

import game.infrpg.common.util.Constants;
import game.infrpg.server.map.CachedMapStorage;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.Region;
import game.infrpg.server.service.Service;
import game.infrpg.server.service.mapgen.IMapGenerator;
import java.io.IOException;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapService extends Service implements IMapService {

	private final ILogger logger;
	private final IMapStorage mapStorage;
	private final IMapGenerator mapGenerator;
	
	public MapService(ILogger logger, IMapStorage mapStorage, IMapGenerator mapGenerator) {
		this.logger = logger;
		this.mapStorage = mapStorage;
		this.mapGenerator = mapGenerator;
	}

	@Inject
	public MapService(ILogger logger, CachedMapStorage mapStorage, IMapGenerator mapGenerator) {
		this.logger = logger;
		this.mapStorage = mapStorage;
		this.mapGenerator = mapGenerator;		
	}

	@Override
	public void init() throws Exception {
		mapStorage.init();
	}
	
	@Override
	public void close() throws IOException {
		logger.debug("Closing MapService");
		mapStorage.close();
	}

	@Override
	public Chunk getChunk(int x, int y) {
		Region region = getChunkRegion(x, y);
		
		int localX = x % Constants.REGION_SIZE; // TODO: optimize irem
		int localY = y % Constants.REGION_SIZE;
		
		Chunk chunk = region.getChunk(localX, localY);
		
		if (chunk != null) {
			return chunk;
		}
		
		chunk = mapGenerator.generateChunk(x, y);
		region.setChunk(localX, localY, chunk);
		
		return chunk;
	}
	
	/**
	 * Gets/creates the region containing the specified chunk.
	 * @param chunkX
	 * @param chunkY
	 * @return 
	 */
	private Region getChunkRegion(int chunkX, int chunkY) {
		int regionX = chunkX / Constants.REGION_SIZE; // TODO: optimize idiv
		int regionY = chunkY / Constants.REGION_SIZE;
		
		Region region = mapStorage.getRegion(regionX, regionY);
		
		if (region == null) {
			region = new Region(regionX, regionY);
			mapStorage.storeRegion(region);
		}
		
		return region;
	}

	

}
