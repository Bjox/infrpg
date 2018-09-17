package game.infrpg.client.logic;

import com.badlogic.gdx.math.Vector2;
import game.infrpg.common.util.Util;
import java.util.function.Consumer;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Dir {
	UP       (1,  0,  1, 0),
	RIGHT    (2,  1,  0, 2),
	LEFT     (4, -1,  0, 6),
	DOWN     (8,  0, -1, 4),
	UPRIGHT  (UP.mask   | RIGHT.mask, 2,  1, 1),
	UPLEFT   (UP.mask   | LEFT.mask, -2,  1, 7),
	DOWNRIGHT(DOWN.mask | RIGHT.mask, 2, -1, 3),
	DOWNLEFT (DOWN.mask | LEFT.mask, -2, -1, 5);

	
	public final int mask;
	public final int index = ordinal();
	public final int clockwiseOrder;
	private final Vector2 unitDirVector;
	private final Vector2 isoDirVector;
	public static final int NUM_DIRECTIONS = values().length;
	/**
	 * A list of Dir enums in clockwise order, starting with UP.
	 */
	public static final Dir[] IN_ORDER = { UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT, LEFT, UPLEFT };
	//                                     0      1       2         3        4       5       6      7
	
	
	private Dir(int mask, float unitVecX, float unitVecY, int order) {
		this.mask = mask;
		this.clockwiseOrder = order;
		this.unitDirVector = new Vector2(unitVecX, unitVecY).nor();
		this.isoDirVector = new Vector2(unitDirVector);
		Util.cart2iso(isoDirVector);
	}
	
	
	public Vector2 getUnitDirVector() {
		// TODO: repeated Vector2 creation
		return new Vector2(unitDirVector);
	}
	
	
	public Vector2 getIsometricDirVector() {
		return new Vector2(isoDirVector);
	}

	
	public static Dir dirFromMask(int mask) {
		for (Dir dir : Dir.values()) {
			if (dir.mask == mask) {
				return dir;
			}
		}
		return null;
	}
	
	public static void forEach(Consumer<Dir> consumer) {
		for (Dir dir : values()) {
			consumer.accept(dir);
		}
	}
}
