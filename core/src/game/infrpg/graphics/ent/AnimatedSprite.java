package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.graphics.GraphicsUtil;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class AnimatedSprite extends Sprite {
	
	private final Animation<TextureRegion> animation;

	public AnimatedSprite(Animation<TextureRegion> animation) {
		super(animation.getKeyFrame(0));
		this.animation = animation;
	}

	@Override
	protected TextureRegion getTextureRegion(float t) {
		return animation.getKeyFrame(t);
	}
	
	public final void setFps(float fps) {
		animation.setFrameDuration(GraphicsUtil.FRAME_DURATION(fps));
	}
	
	public final void setPlayMode(Animation.PlayMode playMode) {
		animation.setPlayMode(playMode);
	}
	
}
