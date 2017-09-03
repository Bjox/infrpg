package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import static game.infrpg.graphics.GraphicsUtil.FRAME_DURATION;
import static game.infrpg.MyGdxGame.*;
import game.infrpg.graphics.GraphicsUtil;
import game.infrpg.graphics.ent.AnimatedSprite;
import game.infrpg.graphics.ent.Sprite;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class GraphicsAssetLoader {
	
	/** 
	 * Default FPS used when loding animations.
	 * Frame duration can later be changed in the returned animation object.
	 */
	public static float defaultAnimationFps = 20;
	/**
	 * Default playmode used when loading animations.
	 * This can later be changed in the returned animation object.
	 */
	public static Animation.PlayMode defaultAnimationPlaymode = Animation.PlayMode.LOOP;
	
	/**
	 * Private constructor.
	 */
	private GraphicsAssetLoader() {
	}
	
	/**
	 * Log a warning message when an asset is not found.
	 * @param name 
	 */
	private static void warnNotFound(String name) {
		logger.error(String.format("Asset \"%s\" not found!", name));
	}
	
	/**
	 * Define the loader methods here.
	 */
	
	/**
	 * Load an animation with separate, indexed key frame images.<br>
	 * ani_01.png, ani_02.png, ani_03.png, ...<br>
	 * There can only be one key frame per image.
	 * @param name
	 * @return 
	 */
	public static Animation<TextureRegion> loadIndexedAnimation(String name) {
		Array<TextureAtlas.AtlasRegion> regions = getAtlas().findRegions(name);
		if (regions.size == 0) warnNotFound(name);
		Animation<TextureRegion> animation = new Animation(
				FRAME_DURATION(defaultAnimationFps), regions, defaultAnimationPlaymode);
		return animation;
	}
	
	/**
	 * Loads a single sprite in the form of a texture region.
	 * @param name
	 * @return 
	 */
	public static TextureRegion loadSprite(String name) {
		TextureRegion tr = getAtlas().findRegion(name);
		if (tr == null) warnNotFound(name);
		return tr;
	}
	
	/**
	 * Load an animation from a spritesheet, where keyframes
	 * are ordered in rows and columns. The keyframe-row/column
	 * order is:<br>
	 * 1 2 3<br>
	 * 4 5 6
	 * @param name
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static Animation<TextureRegion> loadSheetAnimation(String name, int rows, int columns) {
		TextureRegion[] trs;
		try {
			trs = GraphicsUtil.getSpritesheetAnimationFrames(getAtlas(), name, rows, columns);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		Array<TextureRegion> arr = new Array((Object[])trs);
		Animation<TextureRegion> animation = new Animation(
				FRAME_DURATION(defaultAnimationFps), arr, defaultAnimationPlaymode);
		return animation;
	}
	
	/**
	 * Load a directional animation where each direction is
	 * defined in a separate image with direction specified
	 * in their filename. E.g. the animation "ani":<br>
	 * ani_down.png, ani_downleft.png, ani_upright, ...<br>
	 * Each image define a row/column ordered animation in the format:<br>
	 * 1 2 3<br>
	 * 4 5 6
	 * @param name
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static AnimatedSprite[] loadDirAnimation(String name, int rows, int columns) {
		AnimatedSprite[] anis = new AnimatedSprite[Dir.NUM_DIRECTIONS];
		for (Dir dir : Dir.values()) {
			String dirname = name + "_" + dir.name().toLowerCase();
			anis[dir.index] = new AnimatedSprite(loadSheetAnimation(dirname, rows, columns));
		}
		return anis;
	}
	
	/**
	 * Load directional texture regions from a single image,
	 * where the directional texture regions are laid out
	 * on a single row. The image must be on the format:<br>
	 * 1 2 3 ... n, where n is the number of directions defined
	 * in the Dir enum.
	 * @param name
	 * @return An array of texture regions, with length n.
	 */
	public static TextureRegion[] loadDirSheetSprite(String name) {
		TextureRegion[] trs;
		try {
			trs = GraphicsUtil.getSpritesheetAnimationFrames(getAtlas(), name, 1, Dir.NUM_DIRECTIONS);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		return trs;
	}
	
}
