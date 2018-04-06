package game.infrpg.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.client.entities.rendering.AnimationRenderable;

/**
 * One file containing keyframes for an animation, in a row/column format.
 * 
 * @author Bjørnar W. Alvestad
 */
public class AnimationSheetAsset extends GraphicsAsset {
	
	public final int rows;
	public final int columns;
	
	public AnimationSheetAsset(String path, int rows, int columns) {
		super(path);
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	public AnimationRenderable loadRenderable() {
		Animation<TextureRegion> animation = GraphicsAssetLoader.loadAnimationSheet(getAtlas(), path, rows, columns);
		return new AnimationRenderable(animation);
	}
	
}
