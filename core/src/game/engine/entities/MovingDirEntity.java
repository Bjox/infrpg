package game.engine.entities;

import game.engine.entities.rendering.DirRenderable;
import game.engine.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MovingDirEntity extends DirEntity {
	
	/** Moving flag. */
	private boolean moving;
	/** The state map used during movement. */
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
	
	/**
	 * Set the moving flag.
	 * @param moving 
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	/**
	 * Get the moving flag.
	 * @return 
	 */
	public boolean isMoving() {
		return moving;
	}
	
	/**
	 * Get the render state map used during movement.
	 * @return 
	 */
	protected final RenderStateMap getMovingRenderStateMap() {
		return movingStates;
	}

	/**
	 * Overridden method. Returns the moving state map if the moving flag is set.
	 * @return 
	 */
	@Override
	protected RenderStateMap getRenderStateMap() {
		return moving ? movingStates : super.getRenderStateMap();
	}
	
}
