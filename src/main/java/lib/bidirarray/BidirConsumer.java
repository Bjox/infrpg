package lib.bidirarray;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
@FunctionalInterface
public interface BidirConsumer<T> {
	
	public void accept(int x, T t);
	
}
