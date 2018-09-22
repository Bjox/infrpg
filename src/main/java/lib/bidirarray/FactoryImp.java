package lib.bidirarray;

/**
 *
 * @author Bjørnar W. Alvestad
 */
abstract class FactoryImp<T> implements Factory<T> {

	public final int x;

	public FactoryImp(int x) {
		this.x = x;
	}
	
}
