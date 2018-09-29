package game.infrpg.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Constants;
import game.infrpg.client.logic.Dir;
import java.util.function.BiConsumer;

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
		Globals.logger().error(String.format("Asset \"%s\" not found!", name));
	}
	
	/**
	 * Define the loader methods here.
	 */
	
	/**
	 * Loads a single sprite in the form of a texture region.
	 * @param atlas
	 * @param name
	 * @return 
	 */
	public static TextureRegion loadSprite(TextureAtlas atlas, String name) {
		TextureRegion tr = atlas.findRegion(name);
		if (tr == null) warnNotFound(name);
		return tr;
	}
	
	/**
	 * Loads a directional sprite from a series of files.
	 * @param atlas
	 * @param name
	 * @return 
	 */
	public static TextureRegion[] loadSpriteDirectional(TextureAtlas atlas, String name) {
		TextureRegion[] trs = new TextureRegion[Dir.NUM_DIRECTIONS];
		forEachDirName(name, (dir, fullName) -> {
			trs[dir.index] = loadSprite(atlas, fullName);
		});
		return trs;
	}
	
	/**
	 * Load directional texture regions from a single image,
	 * where the directional texture regions are laid out
	 * on a single row. The image must be on the format:<br>
	 * 1 2 3 ... n, where n is the number of directions defined
	 * in the Dir enum.
	 * @param atlas
	 * @param name
	 * @return An array of texture regions, with length n.
	 */
	public static TextureRegion[] loadSpriteDirectionalSheet(TextureAtlas atlas, String name) {
		TextureRegion[] trs;
		try {
			trs = getSpritesheetRegionsFlatten(atlas, name, 1, Dir.NUM_DIRECTIONS);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		return trs;
	}
	
	/**
	 * Load an animation with separate, indexed key frame images.<br>
	 * ani_01.png, ani_02.png, ani_03.png, ...<br>
	 * There can only be one key frame per image.
	 * @param atlas
	 * @param name
	 * @return 
	 */
	public static Animation<TextureRegion> loadAnimationIndexed(TextureAtlas atlas, String name) {
		Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);
		if (regions.size == 0) warnNotFound(name);
		Animation<TextureRegion> animation = new Animation<>(
				FRAME_DURATION(Constants.DEFAULT_ANIMATION_FRAMERATE), regions, Constants.DEFAULT_ANIMATION_PLAYMODE);
		return animation;
	}
	
	/**
	 * Load an animation from a spritesheet, where keyframes
	 * are ordered in rows and columns. The keyframe-row/column
	 * order is:<br>
	 * 1 2 3<br>
	 * 4 5 6
	 * @param atlas
	 * @param name
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static Animation<TextureRegion> loadAnimationSheet(TextureAtlas atlas, String name, int rows, int columns) {
		TextureRegion[] trs;
		try {
			trs = getSpritesheetRegionsFlatten(atlas, name, rows, columns);
		} catch (NullPointerException e) {
			warnNotFound(name);
			return null;
		}
		Array<TextureRegion> arr = new Array<>(trs);
		Animation<TextureRegion> animation = new Animation<>(
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
	 * @param atlas
	 * @param name
	 * @param rows
	 * @param columns
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static Animation<TextureRegion>[] loadAnimationSheetDirectional(TextureAtlas atlas, String name, int rows, int columns) {
		Animation[] anis = new Animation[Dir.NUM_DIRECTIONS];
		forEachDirName(name, (dir, fullName) -> {
			Animation<TextureRegion> an = loadAnimationSheet(atlas, fullName, rows, columns);
			anis[dir.index] = an;
		});
		return anis;
	}
	
	/**
	 * Get a spritesheet as an array of separate texture regions.
	 * @param atlas
	 * @param textureName
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static TextureRegion[] getSpritesheetRegionsFlatten(TextureAtlas atlas, String textureName, int rows, int columns) {
		return flatten2Darray(getSpritesheetRegions(atlas, textureName, rows, columns));
	}
	
	/**
	 * Get a spritesheet as an array of separate texture regions.
	 * @param atlas
	 * @param textureName
	 * @param rows
	 * @param columns
	 * @return 
	 */
	public static TextureRegion[][] getSpritesheetRegions(TextureAtlas atlas, String textureName, int rows, int columns) {
		TextureRegion tex = atlas.findRegion(textureName);
		if (tex == null)
			throw new NullPointerException("Texture \"" + textureName + "\" not found.");
		
		int tileWidth = tex.getRegionWidth() / columns;
		int tileHeight = tex.getRegionHeight() / rows;
		
		if (tex.getRegionWidth() % columns != 0 || tex.getRegionHeight() % rows != 0)
			Globals.logger().warning("Spritesheet \"" + textureName + "\" dimensions are not evenly divided by specified rows:columns (" + rows + ":" + columns + ").");
		
		return tex.split(tileWidth, tileHeight);
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
	
	private static void forEachDirName(String baseName, BiConsumer<Dir, String> consumer) {
		for (Dir dir : Dir.values()) {
			consumer.accept(dir, baseName.concat("_").concat(dir.name().toLowerCase()));
		}
	}
	
}
