package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class AnimatedSprite extends Sprite {
	
	private final Animation<TextureRegion> animation;

	public AnimatedSprite(Animation<TextureRegion> animation) {
		super(animation.getKeyFrames()[0]);
		this.animation = animation;
	}

	@Override
	public void render(SpriteBatch batch, float t, float screenx, float screeny) {
		// Set the correct animation frame
		setTextureRegion(animation.getKeyFrame(t));
		// Render the sprite
		super.render(batch, t);
	}
	
	
	
	
}
