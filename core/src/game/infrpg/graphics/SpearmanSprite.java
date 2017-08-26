package game.infrpg.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SpearmanSprite extends OctaDirSprite {

	private boolean idle;
	private final TextureRegion[] idleFrames;
	private final Vector2[] idleFramesOffsets;
	
	
	public SpearmanSprite(TextureAtlas atlas, float fps, float scale) {
		super("animations/spearman", atlas, 1, 10, fps, scale);
		this.idle = true;
		TextureRegion[] idleFrameUnordered = GraphicsUtil.getSpritesheetAnimationFrames(atlas, "animations/spearman_idle", 1, 8);
		this.idleFrames = new TextureRegion[idleFrameUnordered.length];
		
		this.idleFrames[Dir.DOWN.index] = idleFrameUnordered[0];
		this.idleFrames[Dir.DOWNLEFT.index] = idleFrameUnordered[1];
		this.idleFrames[Dir.LEFT.index] = idleFrameUnordered[2];
		this.idleFrames[Dir.UPLEFT.index] = idleFrameUnordered[3];
		this.idleFrames[Dir.UP.index] = idleFrameUnordered[4];
		this.idleFrames[Dir.UPRIGHT.index] = idleFrameUnordered[5];
		this.idleFrames[Dir.RIGHT.index] = idleFrameUnordered[6];
		this.idleFrames[Dir.DOWNRIGHT.index] = idleFrameUnordered[7];
		
		this.idleFramesOffsets = new Vector2[idleFrames.length];
		for (int i = 0; i < idleFrames.length; i++) {
			idleFramesOffsets[i] = new Vector2(
					idleFrames[i].getRegionWidth() * -0.5f * scale,
					idleFrames[i].getRegionHeight() * -0.5f * scale);
		}
		idleFramesOffsets[Dir.UP.index].y += -4;
	}
	
	
	public void setIdle(boolean idle) {
		this.idle = idle;
	}
	

	@Override
	protected TextureRegion currentFrame(float elapsedTime, int frameIndex) {
		if (idle) return idleFrames[currentDir.ordinal()];
		return super.currentFrame(elapsedTime, frameIndex);
	}

	
	@Override
	protected Vector2 currentAnimationOffset(int frameIndex) {
		if (idle) return idleFramesOffsets[frameIndex];
		return super.currentAnimationOffset(frameIndex);
	}
	
	
}
