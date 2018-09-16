package game.engine.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.engine.client.rendering.renderable.AnimationRenderable;

/**
 * A series of indexed keyframes denoting an animation.
 * 
 * @author Bjørnar W. Alvestad
 */
public class AnimationIndexedAsset extends GraphicsAsset {

	public AnimationIndexedAsset(String path) {
		super(path);
	}

	@Override
	public AnimationRenderable loadRenderable() {
		Animation<TextureRegion> animation = GraphicsAssetLoader.loadAnimationIndexed(getAtlas(), path);
		return new AnimationRenderable(animation);
	}
	
}
