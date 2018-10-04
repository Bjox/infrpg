package game.infrpg.client.logic.mapold;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Tiles {
	GRASS(0),
	DIRT(1),;

	/**
	 * The tile index of the enum list.
	 */
	public final byte dataValue;

	/**
	 * The number of defined enum tiles.
	 */
	public static final int AMOUNT = values().length;

	private Tiles(int dataValue) {
		if (dataValue > Byte.MAX_VALUE || dataValue < Byte.MIN_VALUE) {
			throw new RuntimeException("Tile index out of range.");
		}
		this.dataValue = (byte) dataValue;
	}
}
