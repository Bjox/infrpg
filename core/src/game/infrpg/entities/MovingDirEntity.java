package game.infrpg.entities;

import game.infrpg.entities.rendering.DirRenderable;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MovingDirEntity extends DirEntity {
	
	private boolean moving;
	private final RenderStateMap movingStates;
	
	public MovingDirEntity(DirRenderable idleRenderable, DirRenderable moveRenderable) {
		super(idleRenderable);
		this.movingStates = new RenderStateMap(Dir.NUM_DIRECTIONS);
		movingStates.set(RS_UP, moveRenderable.getRenderable(Dir.UP));
		movingStates.set(RS_DOWN, moveRenderable.getRenderable(Dir.DOWN));
		movingStates.set(RS_LEFT, moveRenderable.getRenderable(Dir.LEFT));
		movingStates.set(RS_RIGHT, moveRenderable.getRenderable(Dir.RIGHT));
		movingStates.set(RS_UPLEFT, moveRenderable.getRenderable(Dir.UPLEFT));
		movingStates.set(RS_UPRIGHT, moveRenderable.getRenderable(Dir.UPRIGHT));
		movingStates.set(RS_DOWNLEFT, moveRenderable.getRenderable(Dir.DOWNLEFT));
		movingStates.set(RS_DOWNRIGHT, moveRenderable.getRenderable(Dir.DOWNRIGHT));
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public boolean isMoving() {
		return moving;
	}

	@Override
	protected RenderStateMap getRenderStateMap() {
		return moving ? movingStates : super.getRenderStateMap();
	}
	
}
