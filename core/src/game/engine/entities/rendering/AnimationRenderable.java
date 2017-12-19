package game.engine.entities.rendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.Infrpg;
import game.engine.graphics.assets.GraphicsAssetLoader;

/**
 * A renderable that represents an animation, which is
 * a series of texture region key frames.
 * 
 * @author Bjørnar W. Alvestad
 */
public class AnimationRenderable implements Renderable {
	
	private final Animation<TextureRegion> animation;

	public AnimationRenderable(Animation<TextureRegion> animation) {
		this.animation = animation;
	}
	
	public void setFps(float fps) {
		animation.setFrameDuration(GraphicsAssetLoader.FRAME_DURATION(fps));
	}
	
	public void setPlayMode(Animation.PlayMode playmode) {
		animation.setPlayMode(playmode);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return animation.getKeyFrame(Infrpg.elapsedTime()); // TODO: Animation state time taken from game elapsed time.
	}
	
}
