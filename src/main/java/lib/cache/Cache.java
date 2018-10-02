package lib.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 * Thread-safe, generic cache. Cached values that are not accessed for a specified
 * period of time will be evicted from the cache at periodic cleanup intervals.
 * 
 * @author Bjørnar W. Alvestad
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class Cache<K, V> implements ICache<K, V> {

	/**
	 * Default cleanup period in milliseconds.
	 */
	private static final long DEFAULT_CLEANUP_PERIOD = 60000;
	
	/**
	 * Default cache period in milliseconds.
	 */
	private static final long DEFAULT_CACHE_PERIOD = 59000;

	private final ILogger logger;
	private final Map<K, CacheEntry<V>> cache;
	private long cachePeriod;
	
	/**
	 * Create a new Cache using the default cache nad cleanup periods.
	 * 
	 * @param logger 
	 */
	@Inject
	public Cache(ILogger logger) {
		this(logger, DEFAULT_CACHE_PERIOD, DEFAULT_CLEANUP_PERIOD);
	}
	
	/**
	 * Create a new Cache using the default cleanup period.
	 *
	 * @param logger
	 * @param cachePeriod The cache period in milliseconds.
	 */
	public Cache(ILogger logger, long cachePeriod) {
		this(logger, cachePeriod, DEFAULT_CLEANUP_PERIOD);
	}

	/**
	 * Create a new Cache.
	 *
	 * @param logger
	 * @param cachePeriod The cache period in milliseconds.
	 * @param cleanupPeriod Cleanup period in milliseconds.
	 */
	public Cache(ILogger logger, long cachePeriod, long cleanupPeriod) {
		this.cache = new HashMap<>();
		this.cachePeriod = cachePeriod;
		this.logger = logger;

		Timer cleanupTimer = new Timer("Cache-cleanup", true);
		TimerTask cleanupTask = new TimerTask() {
			@Override
			public void run() {
				cleanup();
			}
		};
		cleanupTimer.scheduleAtFixedRate(cleanupTask, cleanupPeriod, cleanupPeriod);
	}

	/**
	 * Gets a value from the cache.
	 * This will refresh the cache period for the returned value.
	 * @param key
	 * @return The cached value, or null if the value does not exist.
	 */
	@Override
	public V get(K key) {
		CacheEntry<V> entry;
		synchronized (cache) {
			entry = cache.get(key);
		}
		if (entry == null) {
			return null;
		}
		return entry.getValue();
	}

	/**
	 * Cache a value. Any existing values with the same key will be overwritten.
	 * @param key
	 * @param element 
	 */
	@Override
	public void put(K key, V element) {
		CacheEntry<V> entry = new CacheEntry<>(element);
		synchronized (cache) {
			cache.put(key, entry);
		}
	}
	
	/**
	 * Sets the cache period. Any overdue cache values will be removed
	 * at the next cleanup.
	 * @param cachePeriod 
	 */
	@Override
	public void setCachePeriod(long cachePeriod) {
		this.cachePeriod = cachePeriod;
	}
	
	/**
	 * Checks if the provided key is mapped to a cached value.
	 * In principle, this method checks if <code>get(key) != null</code>,
	 * except for not refreshing the cache period for any value mapped to key.
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}
	
	/**
	 * Returns a stream of every value in the cache. Cache periods are not refreshed.
	 * @return 
	 */
	public Stream<V> cachedValuesStream() {
		return cache.values().stream().map(e -> e.peekValue());
	}

	private void cleanup() {
		int elementsRemoved;
		
		synchronized (cache) {
			int sizeBefore = cache.size();
			long now = System.nanoTime();
			cache.values().removeIf(entry -> (now - entry.getLastAccessTimestamp()) > cachePeriod);
			elementsRemoved = sizeBefore - cache.size();
		}

		logger.debug(elementsRemoved + " elements removed from cache.");
	}

	

}
