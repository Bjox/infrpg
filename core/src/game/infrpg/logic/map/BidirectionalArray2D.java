package game.infrpg.logic.map;

import game.infrpg.logic.map.BidirectionalArray2D.BidirectionalArray.Factory;

/**
 * A 2D, bidirectional array using integers as keys.
 * This 2d array accepts both positive and negative indices up to and including the specified initial size.
 * @author Bjørnar W. Alvestad
 * @param <T> The type of objects to store in this array.
 */
public class BidirectionalArray2D<T> {

	private final BidirectionalArray<BidirectionalArray<T>> array;
	private int xSize;
	private int ySize;
	
	
	/**
	 * Create a new bidirectional array.
	 * @param initSizeX The initial map size in the x direction.
	 * @param initSizeY The initial map size in the y direction.
	 * @param factory
	 */
	public BidirectionalArray2D(int initSizeX, int initSizeY, Factory2D<T> factory) {
		this.xSize = initSizeX;
		this.ySize = initSizeY;
		
		array = new BidirectionalArray<>(initSizeX);
		
		for (int i = -initSizeX; i <= initSizeX; i++) {
			array.setElement(i, new BidirectionalArray<>(initSizeY, makeFactoryImp(i, factory)));
		}
		
	}
	
	
	public BidirectionalArray2D(int initSizeX, int initSizeY) {
		this(initSizeX, initSizeY, null);
	}
	
	
	public T getElement(int x, int y) {
		try {
			return array.getElement(x).getElement(y);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("(" + x + ", " + y + "). Size is (" + xSize + ", " + ySize + ").");
		}
	}
	
	
	public void setElement(int x, int y, T element) {
		array.getElement(x).setElement(y, element);
	}
	
	
	public void forEachElement(BiDirConsumer2D<? super T> consumer) {
		array.forEachElement((x, X) -> 
			X.forEachElement((y, E) -> consumer.accept(x, y, E)));
	}
	
	
	/**
	 * Expands the underlaying array to fit the desired dimensions.
	 * The existing array elements are not modified, but the new, empty
	 * spaces are filled using the specified factory.
	 * @param x
	 * @param y 
	 * @param factory 
	 */
	public void ensureCapacity(int x, int y, Factory2D<T> factory) {
		final int x_abs = Math.abs(x);
		final int y_abs = Math.abs(y);
		
		if (xSize < x_abs) {
			array.ensureCapacity(x_abs);
			for (int i = xSize + 1; i <= x_abs; i++) {
				array.setElement(i, new BidirectionalArray<>(Math.max(y_abs, ySize), makeFactoryImp(i, factory)));
				array.setElement(-i, new BidirectionalArray<>(Math.max(y_abs, ySize), makeFactoryImp(-i, factory)));
			}
			xSize = x_abs;
		}
		
		if (ySize < y_abs) {
			array.forEachElement((xx, E) -> E.ensureCapacity(y_abs, makeFactoryImp(xx, factory)));
			ySize = y_abs;
		}
	}
	
	
	/**
	 * Expands the underlaying array to fit the desired dimensions.
	 * The array elements are not modified.
	 * @param x
	 * @param y 
	 */
	public void ensureCapacity(int x, int y) {
		ensureCapacity(x, y, null);
	}
	
	
	/**
	 * Expand the array by the specified amount.
	 * @param x
	 * @param y 
	 */
	public void grow(int x, int y) {
		ensureCapacity(xSize + x, ySize + y);
	}
	
	
	public int getXSize() {
		return xSize;
	}
	
	
	public int getYSize() {
		return ySize;
	}
	
	
	private Factory<T> makeFactoryImp(int x, Factory2D<T> factory) {
		return factory == null ? null : new FactoryImp<T>(x) {
			@Override
			public T makeObject(int x) {
				return factory.makeObject(super.x, x);
			}
		};
	}
	
	
	public static class BidirectionalArray<T> {
		
		private T[] array;

		
		/**
		 * @param initialSize The initial array size, in both directions.
		 */
		public BidirectionalArray(int initialSize) {
			this(initialSize, null);
		}
		
		
		/**
		 * @param initialSize The initial array size, in both directions.
		 * @param factory
		 */
		public BidirectionalArray(int initialSize, Factory<T> factory) {
			array = (T[]) new Object[initialSize * 2 + 1];
			
			if (factory != null) {
				for (int i = 0; i <= initialSize; i++) {
					setElement(i, factory.makeObject(i));
					setElement(-i, factory.makeObject(-i));
				}
			}
		}
		
		
		public T getElement(int i) {
			return (T) array[toArrayIndex(i)];
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
		
		
		public void forEachElement(BiDirConsumer<? super T> consumer) {
			for (int i = 0; i < array.length; i++) {
				T t = array[i];
				if (t != null) consumer.accept(fromArrayIndex(i), t);
			}
		}
		
		
		public void ensureCapacity(int capacity, Factory<T> factory) {
			int capAbs = Math.abs(capacity);
			
			if (toArrayIndex(capacity) < array.length) return; // Already big enough
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
			ensureCapacity(capacity, null);
		}
		
		
		public static interface Factory<T> {
			public T makeObject(int x);
		}
		
		
		@FunctionalInterface
		public static interface BiDirConsumer<T> {
			public void accept(int x, T t);
		}
	}
	
	
	public static interface Factory2D<T> {
		public T makeObject(int x, int y);
	}
	
	
	@FunctionalInterface
	public static interface BiDirConsumer2D<T> {
		public void accept(int x, int y, T t);
	}
	
	
	private static abstract class FactoryImp<T> implements Factory<T> {
		private final int x;
		public FactoryImp(int x) {
			this.x = x;
		}
	}
	
}