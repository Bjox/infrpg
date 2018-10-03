package game.infrpg.server.map;

import java.io.IOException;
import lib.cache.Cache;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CachedMapStorage extends Cache<String, Region> implements IMapStorage {

	private final IMapStorage backingStorage;
	private final ILogger logger;

	@Inject
	public CachedMapStorage(IMapStorage backingStorage, ILogger logger) {
		super(logger);
		this.backingStorage = backingStorage;
		this.logger = logger;
	}
	
	@Override
	public void init() throws Exception {
		backingStorage.init();
	}

	@Override
	public Region getRegion(int x, int y) {
		logger.debug(String.format("Getting region (%d,%d)", x, y));
		
		String key = getKey(x, y);
		Region region = get(key);
		if (region != null) {
			return region;
		}
		
		region = backingStorage.getRegion(x, y);
		if (region != null) {
			put(key, region);
		}
		
		return region;
	}

	@Override
	public void storeRegion(Region region) {
		logger.debug(String.format("Storing region (%d,%d)", region.position.getX(), region.position.getY()));
		
		backingStorage.storeRegion(region);
		
		String key = getKey(region);
		if (!containsKey(key)) {
			put(key, region);
		}
	}

	@Override
	public void close() throws IOException {
		logger.debug("Writing region cache to storage");
		cachedValuesStream().forEach(backingStorage::storeRegion);
		backingStorage.close();
	}
	
	@Override
	public boolean isClosed() {
		return backingStorage.isClosed();
	}

	@Override
	protected void evictedFromCache(Region region) {
		logger.debug(region.toString() + " evicted from cache");
		backingStorage.storeRegion(region);
	}
	
	private String getKey(int x, int y) {
		return x + "," + y;
	}
	
	private String getKey(Region region) {
		return getKey(region.position.getX(), region.position.getY());
	}
	
}
