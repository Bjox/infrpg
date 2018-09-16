package game.engine.client.graphics.assets;

import game.engine.client.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Assets {

	public static final SpritesheetAsset TEST_SPRITESHEET = ssa("a", 2, 2);

	public static final AnimationSheetDirAsset SPEARMAN_MOVE = asda("animations/Spearman/spearman", 1, 10);
	public static final SpriteSheetDirAsset SPEARMAN_IDLE = ssda("animations/Spearman/spearman_idle", 1, 8, Dir.DOWN, true);
	
	public static final AnimationIndexedAsset SWIETENIA_TREE = aia("animations/Swietenia_tree/_test_tree-animation");
	

	private static SpriteAsset sa(String path) {
		return new SpriteAsset(path);
	}

	private static SpriteDirAsset sda(String path) {
		return new SpriteDirAsset(path);
	}
	
	private static SpriteSheetDirAsset ssda(String path, int rows, int columns, Dir startDir, boolean clockWise) {
		return new SpriteSheetDirAsset(path, rows, columns, startDir, clockWise);
	}

	private static SpritesheetAsset ssa(String path, int rows, int columns) {
		return new SpritesheetAsset(path, rows, columns);
	}

	private static AnimationIndexedAsset aia(String path) {
		return new AnimationIndexedAsset(path);
	}

	private static AnimationSheetAsset asa(String path, int rows, int columns) {
		return new AnimationSheetAsset(path, rows, columns);
	}

	private static AnimationSheetDirAsset asda(String path, int rows, int columns) {
		return new AnimationSheetDirAsset(path, rows, columns);
	}
	
	/**
	 * Used for doc purposes.
	 * @deprecated
	 */
	@Deprecated
	public static enum Format {
		/**
		 * A regular, single framed sprite.
		 */
		SPRITE,
		/**
		 * A series of sprites, each denoting a direction.
		 */
		SPRITE_DIRECTIONAL,
		/**
		 * A spritesheet of directional sprites.
		 */
		SPRITE_SHEET_DIRECTIONAL,
		/**
		 * A spritesheet containing multiple sprites, structured by rows/columns.
		 */
		SPRITESHEET,
		/**
		 * A series of indexed keyframes denoting an animation.
		 */
		ANIMATION_INDEXED,
		/**
		 * One file containing keyframes for an animation, in a row/column format.
		 */
		ANIMATION_SHEET,
		/**
		 * A series directional sheets.
		 */
		ANIMATION_SHEET_DIRECTIONAL
	}

	/**
	 * Private constructor.
	 */
	private Assets() {
	}

}
