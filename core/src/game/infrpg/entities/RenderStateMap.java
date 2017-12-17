package game.infrpg.entities;

import game.infrpg.entities.rendering.Renderable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class RenderStateMap {
	
	/** The state map. */
	private Renderable[] states;
	/** The render state id counter. */
	private int renderStateCounter;

	public RenderStateMap(int initialSize) {
		this.states = new Renderable[initialSize];
		this.renderStateCounter = 0;
	}
	
	public Renderable get(int renderState) {
		Renderable r = states[renderState];
		return r == null ? Renderable.NULL_RENDERABLE : r;
	}
	
	public void set(int renderState, Renderable renderable) {
		if (renderable == null) renderable = Renderable.NULL_RENDERABLE;
		rangeCheck(renderState);
		states[renderState] = renderable;
	}
	
	public int registerRenderState(Renderable renderable) {
		rangeCheck(renderStateCounter);
		states[renderStateCounter] = renderable;
		return renderStateCounter++;
	}
	
	private void rangeCheck(int index) {
		if (states.length <= index) {
			states = Arrays.copyOf(states, index + 2);
		}
	}
	
}
