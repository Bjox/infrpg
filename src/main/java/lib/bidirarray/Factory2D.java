package lib.bidirarray;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface Factory2D<T> {

	public T makeObject(int x, int y);
	
}
