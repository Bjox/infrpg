package lib.bidirarray;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public interface Factory<T> {

	public T makeObject(int i);
	
}
