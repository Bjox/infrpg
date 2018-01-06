package game.engine.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.engine.entities.rendering.SpritesheetRenderable;

/**
 * A spritesheet containing multiple sprites, structured by rows/columns.
 * 
 * @author Bjørnar W. Alvestad
 */
public class SpritesheetAsset extends GraphicsAsset {
	
	public final int rows;
	public final int columns;
	
	public SpritesheetAsset(String path, int rows, int columns) {
		super(path);
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	public SpritesheetRenderable loadRenderable() {
		TextureRegion sheetRegion = GraphicsAssetLoader.loadSprite(getAtlas(), path);
		return new SpritesheetRenderable(sheetRegion, rows, columns);
	}
	
}
