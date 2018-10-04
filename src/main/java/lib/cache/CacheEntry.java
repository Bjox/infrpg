package lib.cache;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CacheEntry<T> {
	
	private final T value;
	private long lastAccessTimestamp;

	public CacheEntry(T value) {
		this.value = value;
		updateAccessTimestamp();
	}
	
	public long getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}
	
	public T getValue() {
		updateAccessTimestamp();
		return value;
	}
	
	public T peekValue() {
		return value;
	}
	
	private void updateAccessTimestamp() {
		this.lastAccessTimestamp = System.nanoTime();
	}
}
