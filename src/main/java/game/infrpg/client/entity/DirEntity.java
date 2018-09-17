package game.infrpg.client.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.client.rendering.renderable.DirRenderable;
import game.infrpg.client.rendering.renderable.Renderable;
import game.infrpg.client.logic.Dir;

/**
 * An entity defining rendering states pertaining to a directional renderable,
 * in addition to the default render state.
 * 
 * @author Bjørnar W. Alvestad
 */
public abstract class DirEntity extends Entity {
	
	public final int RS_UP;
	public final int RS_DOWN;
	public final int RS_LEFT;
	public final int RS_RIGHT;
	public final int RS_UPLEFT;
	public final int RS_UPRIGHT;
	public final int RS_DOWNLEFT;
	public final int RS_DOWNRIGHT;
	
	private Dir direction;
	private final int[] dirStateMap;
	
	/**
	 * 
	 * @param dirRenderable The direction renderable.
	 * @param defaultRenderable Optional renderable to be tied to the <code>RS_DEFAULT</code> state.
	 */
	public DirEntity(DirRenderable dirRenderable, Renderable defaultRenderable) {
		this.direction = Dir.DOWN;
		setRenderStateRenderable(RS_DEFAULT, defaultRenderable);
		
		RS_UP = registerRenderState(dirRenderable.getRenderable(Dir.UP));
		RS_DOWN = registerRenderState(dirRenderable.getRenderable(Dir.DOWN));
		RS_LEFT = registerRenderState(dirRenderable.getRenderable(Dir.LEFT));
		RS_RIGHT = registerRenderState(dirRenderable.getRenderable(Dir.RIGHT));
		RS_UPLEFT = registerRenderState(dirRenderable.getRenderable(Dir.UPLEFT));
		RS_UPRIGHT = registerRenderState(dirRenderable.getRenderable(Dir.UPRIGHT));
		RS_DOWNLEFT = registerRenderState(dirRenderable.getRenderable(Dir.DOWNLEFT));
		RS_DOWNRIGHT = registerRenderState(dirRenderable.getRenderable(Dir.DOWNRIGHT));
		
		this.dirStateMap = new int[Dir.NUM_DIRECTIONS];
		this.dirStateMap[Dir.UP.index] = RS_UP;
		this.dirStateMap[Dir.DOWN.index] = RS_DOWN;
		this.dirStateMap[Dir.LEFT.index] = RS_LEFT;
		this.dirStateMap[Dir.RIGHT.index] = RS_RIGHT;
		this.dirStateMap[Dir.UPLEFT.index] = RS_UPLEFT;
		this.dirStateMap[Dir.UPRIGHT.index] = RS_UPRIGHT;
		this.dirStateMap[Dir.DOWNLEFT.index] = RS_DOWNLEFT;
		this.dirStateMap[Dir.DOWNRIGHT.index] = RS_DOWNRIGHT;
	}
	
	/**
	 * 
	 * @param renderable The direction renderable.
	 */
	public DirEntity(DirRenderable renderable) {
		this(renderable, null);
	}
	
	public final void setDirection(Dir direction) {
		this.direction = direction;
		setRenderState(dirStateMap[direction.index]);
	}
	
	public final Dir getDirection() {
		return direction;
	}
	
}
