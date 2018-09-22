package lib.bidirarray;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
@FunctionalInterface
public interface BiDirConsumer2D<T> {

	public void accept(int x, int y, T t);
	
}
