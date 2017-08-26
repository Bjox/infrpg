package game.infrpg.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static game.infrpg.MyGdxGame.logger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class GraphicsUtil {
	
	public static float FRAME_DURATION(float FPS) { return 1 / (float)FPS; }
	
	
	public static TextureRegion[] getSpritesheetAnimationFrames(TextureAtlas atlas, String textureName, int rows, int columns) {
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
