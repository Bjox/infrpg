package lib.cache;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <K> The key type.
 * @param <V> The value type.
 */
public interface ICache<K, V> {
	
	V get(K key);
	
	void put(K key, V element);
	
	boolean containsKey(K key);
	
	void setCachePeriod(long cachePeriod);
	
}
