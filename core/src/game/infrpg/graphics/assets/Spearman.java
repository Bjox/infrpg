package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.graphics.ent.DirSprite;
import game.infrpg.graphics.ent.Sprite;
import game.infrpg.logic.Constants;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Spearman extends DirSprite {
	
	private static final String ASSET_NAME = "animations/Spearman/spearman";
	private static final float SCALE = 0.5f;
	
	private final TextureRegion[] idleFrames;
	private boolean idle;
	
	public Spearman() {
		super(GraphicsAssetLoader.loadDirAnimation(ASSET_NAME, 1, 10));
		
		TextureRegion[] idleFrameUnordered = GraphicsAssetLoader.loadDirSheetSprite(ASSET_NAME + "_idle");
		this.idleFrames = new TextureRegion[idleFrameUnordered.length];
		
		this.idleFrames[Dir.DOWN.index]      = idleFrameUnordered[0];
		this.idleFrames[Dir.DOWNLEFT.index]  = idleFrameUnordered[1];
		this.idleFrames[Dir.LEFT.index]      = idleFrameUnordered[2];
		this.idleFrames[Dir.UPLEFT.index]    = idleFrameUnordered[3];
		this.idleFrames[Dir.UP.index]        = idleFrameUnordered[4];
		this.idleFrames[Dir.UPRIGHT.index]   = idleFrameUnordered[5];
		this.idleFrames[Dir.RIGHT.index]     = idleFrameUnordered[6];
		this.idleFrames[Dir.DOWNRIGHT.index] = idleFrameUnordered[7];
		
		this.idle = true;
		
		setFps(Constants.DEBUG_MOVEMENT_SPEED / 4.3f);
		setScale(SCALE);
		setOrigin(Origin.DOWN);
		origin_y -= 0.15;
	}
	
	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	@Override
	protected TextureRegion getTextureRegion(float t) {
		return idle ? idleFrames[getDirection().index] : super.getTextureRegion(t);
	}

//	@Override
//	public float getOriginX() {
//		return idle ? super.getOriginX() : getDirectionalSprite().getOriginX();
//	}
//
//	@Override
//	public float getOriginY() {
//		return idle ? super.getOriginY() : getDirectionalSprite().getOriginY();
//	}
	
	
}
