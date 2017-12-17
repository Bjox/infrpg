package game.infrpg.entities.rendering;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static game.infrpg.Infrpg.logger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SpritesheetRenderable implements Renderable {

	private final TextureRegion[][] regions;
	/** Number of rows in this spritesheet. */
	public final int rows;
	/** Number of columns in this spritesheet. */
	public final int columns;
	/** Row selector. */
	public int row;
	/** Column selector. */
	public int column;

	public SpritesheetRenderable(TextureRegion sheet, int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		
		int tileWidth = sheet.getRegionWidth() / columns;
		int tileHeight = sheet.getRegionHeight() / rows;
		
		if (sheet.getRegionWidth() % columns != 0 || sheet.getRegionHeight() % rows != 0)
			logger.warning("Spritesheet dimensions are not evenly divided by specified rows:columns (" + rows + ":" + columns + ").");
		
		this.regions = sheet.split(tileWidth, tileHeight);
	}
	
	@Override
	public TextureRegion getTextureRegion() {
		return regions[row][column];
	}
	
}
