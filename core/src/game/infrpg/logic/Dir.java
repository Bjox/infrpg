package game.infrpg.logic;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Dir {
	UP(1, 0, 1),
	RIGHT(2, 1, 0),
	LEFT(4, -1, 0),
	DOWN(8, 0, -1),
	UPRIGHT(UP.mask | RIGHT.mask, 2, 1),
	UPLEFT(UP.mask | LEFT.mask, -2, 1),
	DOWNRIGHT(DOWN.mask | RIGHT.mask, 2, -1),
	DOWNLEFT(DOWN.mask | LEFT.mask, -2, -1);

	
	public final int mask;
	public final int index = ordinal();
	private final Vector2 unitDirVector;

	
	private Dir(int mask, float unitVecX, float unitVecY) {
		this.mask = mask;
		this.unitDirVector = new Vector2(unitVecX, unitVecY).nor(); // TODO: meby shit to return new vector each time. create immutable vector instead?
	}
	
	
	public Vector2 getUnitDirVector() {
		return new Vector2(unitDirVector);
	}

	
	public static Dir dirFromMask(int mask) {
		for (Dir dir : Dir.values()) {
			if (dir.mask == mask) {
				return dir;
			}
		}
		return null;
	}
}
