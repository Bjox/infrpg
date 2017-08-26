package game.infrpg.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.logic.Dir;
import static game.infrpg.graphics.GraphicsUtil.FRAME_DURATION;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class OctaDirSprite {

	

	public final String name;
	public final int rows;
	public final int columns;
	public final float fps;
	public final float scale;
	public float x;
	public float y;
	
	protected Dir currentDir;
	private final Animation<TextureRegion>[] animations;
	private final Vector2[] animationOffsets;
	
	
	public OctaDirSprite(String name, TextureAtlas atlas, int rows, int columns, float fps, float scale) {
		this.name = name;
		this.rows = rows;
		this.columns = columns;
		this.fps = fps;
		this.scale = scale;
		this.animations = new Animation[Dir.values().length];
		this.animationOffsets = new Vector2[Dir.values().length];
		this.currentDir = Dir.DOWN;
		
		for (Dir dir : Dir.values()) {
			String texName = name + "_" + dir.name().toLowerCase();
			TextureRegion[] dirTexRegions = GraphicsUtil.getSpritesheetAnimationFrames(atlas, texName, rows, columns);
			int animationIndex = dir.index;
			animations[animationIndex] = new Animation(FRAME_DURATION(fps), (Object[]) dirTexRegions);
			animationOffsets[animationIndex] = new Vector2(
					dirTexRegions[0].getRegionWidth() * -0.5f * scale,
					dirTexRegions[0].getRegionHeight() * -0.5f * scale);
		}
	}
	
	
	public void addOffset(Dir dir, float xOffset, float yOffset) {
		Vector2 off = animationOffsets[dir.index];
		off.x += xOffset;
		off.y += yOffset;
	}
	
	
	public void setDirection(Dir dir) {
		this.currentDir = dir;
	}
	
	
	public void setDirection(int directionMask) {
		Dir newDir = Dir.dirFromMask(directionMask);
		if (newDir != null) setDirection(newDir);
	}
	
	
	/**
	 * Get the current frame of the sprite.
	 * @param elapsedTime Used to decide which frame to select in an animation.
	 * @param frameIndex The index of the current animation/frame.
	 * @return 
	 */
	protected TextureRegion currentFrame(float elapsedTime, int frameIndex) {
		return animations[currentDir.index].getKeyFrame(elapsedTime, true);
	}
	
	
	protected Vector2 currentAnimationOffset(int frameIndex) {
		return animationOffsets[currentDir.index];
	}
	
	
	public void render(SpriteBatch batch, float elapsedTime) {
		int animationIndex = currentDir.index;
		Vector2 offset = currentAnimationOffset(animationIndex);
		TextureRegion tr = currentFrame(elapsedTime, animationIndex);
		batch.draw(tr, x + offset.x, y + offset.y, 0, 0, tr.getRegionWidth(), tr.getRegionHeight(), scale, scale, 0);
	}
	
	
	
	
}
