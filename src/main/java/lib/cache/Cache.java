package lib.cache;

import java.util.HashMap;
import java.util.Iterator;
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
public class Cache<K, V> implements ICache<K, V>
{
	/**
	 * DefaultValue cleanup period in milliseconds.
	 */
	private static final long DEFAULT_CLEANUP_PERIOD = 60_000;

	/**
	 * DefaultValue cache period in milliseconds.
	 */
	private static final long DEFAULT_CACHE_PERIOD = 59_000;

	private final ILogger logger;
	private final Map<K, CacheEntry<V>> cache;
	private long cachePeriod; // in ns
	private volatile int elementCount;

	/**
	 * Create a new Cache using the default cache nad cleanup periods.
	 *
	 * @param logger
	 */
	@Inject
	public Cache(ILogger logger)
	{
		this(logger, DEFAULT_CACHE_PERIOD, DEFAULT_CLEANUP_PERIOD);
	}

	/**
	 * Create a new Cache using the default cleanup period.
	 *
	 * @param logger
	 * @param cachePeriod The cache period in milliseconds.
	 */
	public Cache(ILogger logger, long cachePeriod)
	{
		this(logger, cachePeriod, DEFAULT_CLEANUP_PERIOD);
	}

	/**
	 * Create a new Cache.
	 *
	 * @param logger
	 * @param cachePeriod The cache period in milliseconds.
	 * @param cleanupPeriod Cleanup period in milliseconds.
	 */
	public Cache(ILogger logger, long cachePeriod, long cleanupPeriod)
	{
		this.cache = new HashMap<>();
		this.cachePeriod = cachePeriod * 1000000;
		this.logger = logger;

		Timer cleanupTimer = new Timer("Cache-cleanup", true);
		TimerTask cleanupTask = new TimerTask()
		{
			@Override
			public void run()
			{
				if (skipCleanup())
				{
					logger.debug("Skipping cache cleanup");
					return;
				}
				cleanup();
			}
		};
		cleanupTimer.scheduleAtFixedRate(cleanupTask, cleanupPeriod, cleanupPeriod);
	}

	/**
	 * Gets a value from the cache.
	 * This will refresh the cache period for the returned value.
	 *
	 * @param key
	 * @return The cached value, or null if the value does not exist.
	 */
	@Override
	public V get(K key)
	{
		CacheEntry<V> entry;
		synchronized (cache)
		{
			entry = cache.get(key);
		}
		if (entry == null)
		{
			return null;
		}
		return entry.getValue();
	}

	/**
	 * Cache a value. Any existing values with the same key will be overwritten.
	 *
	 * @param key
	 * @param element
	 */
	@Override
	public void put(K key, V element)
	{
		if (element == null)
		{
			return;
		}
		CacheEntry<V> entry = new CacheEntry<>(element);
		synchronized (cache)
		{
			cache.put(key, entry);
			elementCount++;
		}
	}

	/**
	 * Sets the cache period. Any overdue cache values will be removed
	 * at the next cleanup.
	 *
	 * @param cachePeriod
	 */
	@Override
	public void setCachePeriod(long cachePeriod)
	{
		this.cachePeriod = cachePeriod;
	}

	/**
	 * Checks if the provided key is mapped to a cached value.
	 * In principle, this method checks if <code>get(key) != null</code>,
	 * except for not refreshing the cache period for any value mapped to key.
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean containsKey(K key)
	{
		synchronized (cache)
		{
			return cache.containsKey(key);
		}
	}

	/**
	 * Returns a stream of every value in the cache. Cache periods are not refreshed.
	 *
	 * @return
	 */
	@Deprecated
	public Stream<V> cachedValuesStream()
	{
		return cache.values().stream().map(e -> e.peekValue());
	}

	@Override
	public void flush()
	{
		synchronized (cache)
		{
			Iterator<CacheEntry<V>> it = cache.values().iterator();
			while (it.hasNext())
			{
				CacheEntry<V> entry = it.next();
				evictedFromCache(entry.peekValue());
				it.remove();
			}
			elementCount = 0;
		}
	}

	private void cleanup()
	{
		int elementsRemoved;

		synchronized (cache)
		{
			int sizeBefore = cache.size();
			long now = System.nanoTime();

			Iterator<CacheEntry<V>> it = cache.values().iterator();
			while (it.hasNext())
			{
				CacheEntry<V> entry = it.next();
				if ((now - entry.getLastAccessTimestamp()) > cachePeriod)
				{
					evictedFromCache(entry.peekValue());
					it.remove();
				}
			}

			elementsRemoved = sizeBefore - cache.size();
			elementCount -= elementsRemoved;
		}

		logger.debug(elementsRemoved + " element(s) removed from cache.");
	}
	
	public int getElementCount()
	{
		return elementCount;
	}

	/**
	 * Override this method in order to process objects that are evicted from the cache.
	 *
	 * @param value
	 */
	protected void evictedFromCache(V value)
	{
	}

	/**
	 * Override to skip cache cleanup.
	 *
	 * @return
	 */
	protected boolean skipCleanup()
	{
		return false;
	}

}
