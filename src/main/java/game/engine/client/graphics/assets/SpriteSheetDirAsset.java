package game.engine.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.engine.client.rendering.renderable.DirRenderable;
import game.engine.client.rendering.renderable.SpriteRenderable;
import game.engine.client.logic.Dir;

/**
 * A spritesheet of directional sprites.
 * 
 * @author Bjørnar W. Alvestad
 */
public class SpriteSheetDirAsset extends GraphicsAsset {

	public final int rows;
	public final int columns;
	public final Dir startDir;
	public final boolean clockwise;

	public SpriteSheetDirAsset(String path, int rows, int columns, Dir startDir, boolean clockwise) {
		super(path);
		this.rows = rows;
		this.columns = columns;
		this.startDir = startDir;
		this.clockwise = clockwise;
	}
	
	@Override
	public DirRenderable loadRenderable() {
		TextureRegion[] regions = GraphicsAssetLoader.getSpritesheetRegionsFlatten(getAtlas(), path, rows, columns);
		DirRenderable dr = new DirRenderable();
		
		int offset = startDir.clockwiseOrder;
		for (int i = 0; i < Dir.NUM_DIRECTIONS; i++) {
			int index = ((clockwise ? i : -i) + offset + Dir.NUM_DIRECTIONS) % Dir.NUM_DIRECTIONS;
			Dir dir = Dir.IN_ORDER[index];
			dr.setRenderable(dir, new SpriteRenderable(regions[i]));
		}
		
		return dr;
	}
	
}
