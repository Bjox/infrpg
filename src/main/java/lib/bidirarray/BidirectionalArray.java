package lib.bidirarray;

/**
 * Bidirectional array
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public class BidirectionalArray<T> {

	private T[] array;
	private final Factory<T> factory;

	/**
	 * Create a new Bidirectional array with range: [-initialSize,initialSize] (Inclusive)
	 * @param initialSize The initial array size, in both directions.
	 */
	public BidirectionalArray(int initialSize) {
		this(initialSize, null);
	}

	/**
	 * Create a new Bidirectional array with range: [-initialSize,initialSize] (Inclusive)
	 * @param initialSize The initial array size, in both directions.
	 * @param factory
	 */
	@SuppressWarnings("unchecked")
	public BidirectionalArray(int initialSize, Factory<T> factory) {
		this.array = (T[]) new Object[initialSize * 2 + 1];
		this.factory = factory;
		
		if (factory != null) {
			for (int i = 0; i <= initialSize; i++) {
				setElement(i, factory.makeObject(i));
				setElement(-i, factory.makeObject(-i));
			}
		}
	}

	public T getElement(int i) {
		return array[toArrayIndex(i)];
	}

	public final void setElement(int i, T element) {
		array[toArrayIndex(i)] = element;
	}

	private int toArrayIndex(int i) {
		return (i < 0) ? (i * -2 - 1) : (i * 2);
	}

	private int fromArrayIndex(int i) {
		boolean neg = (i & 1) == 1; //er
		i >>>= 1;
		return neg ? ~i : i;
	}
	
	public int getSize() {
		return (array.length - 1) / 2;
	}

	public void forEachElement(BidirConsumer<? super T> consumer) {
		for (int i = 0; i < array.length; i++) {
			T t = array[i];
			if (t != null) {
				consumer.accept(fromArrayIndex(i), t);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void ensureCapacity(int capacity, Factory<T> factory) {
		int capAbs = Math.abs(capacity);

		if (toArrayIndex(capacity) < array.length) {
			return; // Already big enough
		}
		int oldCap = (array.length - 1) >>> 1;

		T[] tmp = array;
		array = (T[]) new Object[capAbs * 2 + 1];

		System.arraycopy(tmp, 0, array, 0, tmp.length);

		if (factory != null) {
			for (int i = oldCap + 1; i <= capacity; i++) {
				setElement(i, factory.makeObject(i));
				setElement(-i, factory.makeObject(-i));
			}
		}
	}

	public void ensureCapacity(int capacity) {
		ensureCapacity(capacity, this.factory);
	}
	
	/**
	 * Expand the array by the specified amount.
	 * @param x
	 */
	public void grow(int x) {
		ensureCapacity(getSize() + x);
	}

}
