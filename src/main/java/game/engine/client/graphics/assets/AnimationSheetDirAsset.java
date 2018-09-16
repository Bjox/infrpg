package game.engine.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.engine.client.rendering.renderable.AnimationRenderable;
import game.engine.client.rendering.renderable.DirRenderable;
import game.engine.client.logic.Dir;

/**
 * A series directional sheets.
 * 
 * @author Bjørnar W. Alvestad
 */
public class AnimationSheetDirAsset extends GraphicsAsset {
	
	public final int rows;
	public final int columns;
	
	public AnimationSheetDirAsset(String path, int rows, int columns) {
		super(path);
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	public DirRenderable loadRenderable() {
		Animation<TextureRegion>[] anis = GraphicsAssetLoader.loadAnimationSheetDirectional(getAtlas(), path, rows, columns);
		DirRenderable dr = new DirRenderable();
		Dir.forEach((dir) -> {
			dr.setRenderable(dir, new AnimationRenderable(anis[dir.index]));
		});
		return dr;
	}
	
}
