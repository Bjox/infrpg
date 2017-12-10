package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import static game.infrpg.Infrpg.*;
import game.infrpg.graphics.ent.AnimatedSprite;
import game.infrpg.logic.Constants;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class GraphicsAssetLoader {

	/** Calculate the frame duration in seconds for a given framerate.
	 * @param FPS
	 * @return  Frame duration in seconds.
	 */
	public static float FRAME_DURATION(float FPS) { return 1 / (float)FPS; }
	
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
				FRAME_DURATION(Constants.DEFAULT_ANIMATION_FRAMERATE), regions, Constants.DEFAULT_ANIMATION_PLAYMODE);
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
			trs = getSpritesheetRegions(getAtlas(), name, rows, columns);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		Array<TextureRegion> arr = new Array((Object[])trs);
		Animation<TextureRegion> animation = new Animation(
				FRAME_DURATION(Constants.DEFAULT_ANIMATION_FRAMERATE), arr, Constants.DEFAULT_ANIMATION_PLAYMODE);
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
			trs = getSpritesheetRegions(getAtlas(), name, 1, Dir.NUM_DIRECTIONS);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		return trs;
	}
	
	/**
	 * Get a spritesheet as an array of separate texture regions.
	 * @param atlas
	 * @param textureName
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static TextureRegion[] getSpritesheetRegions(TextureAtlas atlas, String textureName, int rows, int columns) {
		TextureRegion tex = atlas.findRegion(textureName);
		if (tex == null)
			throw new NullPointerException("Texture \"" + textureName + "\" not found.");
		
		int tileWidth = tex.getRegionWidth() / columns;
		int tileHeight = tex.getRegionHeight() / rows;
		
		if (tex.getRegionWidth() % columns != 0 || tex.getRegionHeight() % rows != 0)
			logger.warning("Spritesheet \"" + textureName + "\" dimensions are not evenly divided by specified rows:columns (" + rows + ":" + columns + ").");
		
		TextureRegion[][] texRegions = tex.split(tileWidth, tileHeight);
		return flatten2Darray(texRegions);
	}
	
	
	private static TextureRegion[] flatten2Darray(TextureRegion[][] array) {
		TextureRegion[] ret = new TextureRegion[array.length * array[0].length];
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				ret[index++] = array[i][j];
			}
		}
		return ret;
	}
	
}
