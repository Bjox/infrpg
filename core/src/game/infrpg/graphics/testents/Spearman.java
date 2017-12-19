package game.infrpg.graphics.testents;

import game.infrpg.entities.MovingDirEntity;
import game.infrpg.graphics.assets.Assets;
import game.infrpg.logic.Constants;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Spearman extends MovingDirEntity {
	
	private static final float SCALE = 0.5f;
	
	public Spearman() {
		super(Assets.SPEARMAN_IDLE.loadRenderable(), Assets.SPEARMAN_MOVE.loadRenderable());
		
		float fps = Constants.DEBUG_MOVEMENT_SPEED / 4.3f;
		getMovingRenderStateMap().forEachAnimation(a -> a.setFps(fps));
		setOrigin(Origin.DOWN);
		setScale(SCALE);
		origin_y = 0.15f;
	}
	
	public void setIdle(boolean idle) {
		setMoving(!idle);
	}
	
	public boolean isIdle() {
		return isMoving();
	}
	
}