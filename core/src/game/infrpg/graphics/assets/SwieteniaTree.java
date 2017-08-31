package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static game.infrpg.graphics.GraphicsUtil.FRAME_DURATION;
import game.infrpg.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SwieteniaTree {
	
	public float x;
	public float y;
	public float scale;
	private final float width;
	private final float height;
	private final Animation<TextureRegion> animation;
	private final float animationOffset;
	
	
	public SwieteniaTree(TextureAtlas atlas) {
		float frameDuration = FRAME_DURATION(15);
		this.animation = new Animation(frameDuration,
				atlas.findRegions("animations/Swietenia_tree/_test_tree-animation"), Animation.PlayMode.LOOP_PINGPONG);
		
		this.animationOffset = Util.randomFloat() * animation.getAnimationDuration(); // TODO: random call
		this.width = animation.getKeyFrame(0).getRegionWidth();
		this.height = animation.getKeyFrame(0).getRegionHeight();
		this.scale = 0.6f;
	}
	
	
	public void render(SpriteBatch batch, float elapsedTime) {
		batch.draw(animation.getKeyFrame(
				elapsedTime + animationOffset), x, y, 0, 0, width, height, scale, scale, 0);
	}
	
}
