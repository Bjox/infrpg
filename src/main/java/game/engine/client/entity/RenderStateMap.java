package game.engine.client.entity;

import game.engine.client.rendering.renderable.AnimationRenderable;
import game.engine.client.rendering.renderable.Renderable;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class RenderStateMap {
	
	/** The state map. */
	private Renderable[] states;
	/** The render state id counter. */
	private int renderStateCounter;

	/**
	 * Create a new render state map with the specified initial capacity.
	 * @param initialSize 
	 */
	public RenderStateMap(int initialSize) {
		this.states = new Renderable[initialSize];
		Arrays.fill(states, Renderable.NULL_RENDERABLE);
		this.renderStateCounter = 0;
	}
	
	/**
	 * Get the renderable for a specific state.
	 * @param renderState
	 * @return 
	 */
	public Renderable get(int renderState) {
		Renderable r = states[renderState];
		return r == null ? Renderable.NULL_RENDERABLE : r;
	}
	
	/**
	 * Set the renderable for a specific state.
	 * @param renderState
	 * @param renderable 
	 */
	public void set(int renderState, Renderable renderable) {
		if (renderable == null) renderable = Renderable.NULL_RENDERABLE;
		rangeCheck(renderState);
		states[renderState] = renderable;
	}
	
	/**
	 * Register a new render state. The render state key is returned.
	 * @param renderable The renderable to register.
	 * @return The render state key.
	 */
	public int registerRenderState(Renderable renderable) {
		rangeCheck(renderStateCounter);
		states[renderStateCounter] = renderable;
		return renderStateCounter++;
	}
	
	/**
	 * Checks and expands the state map if necessary.
	 * @param index 
	 */
	private void rangeCheck(int index) {
		if (states.length <= index) {
			int oldLength = states.length;
			states = Arrays.copyOf(states, index + 2);
			Arrays.fill(states, oldLength, states.length, Renderable.NULL_RENDERABLE);
		}
	}
	
	/**
	 * Returns a stream of all the states in this render state map.
	 * This includes potential references to <code>Renderable.NULL_RENDERABLE</code>.
	 * @return 
	 */
	public Stream<Renderable> stream() {
		return Stream.of(states);
	}
	
	/**
	 * Perform an action for each state in this render state map.
	 * This will not include <code>Renderable.NULL_RENDERABLE</code>.
	 * @param consumer 
	 */
	public void forEach(Consumer<Renderable> consumer) {
		for (int i = 0; i < states.length; i++) {
			Renderable r = get(i);
			if (r != Renderable.NULL_RENDERABLE) consumer.accept(r);
		}
	}
	
	/**
	 * Perform an action for each <code>AnimationRenderable</code>
	 * in this render state map.
	 * @param consumer 
	 */
	public void forEachAnimation(Consumer<AnimationRenderable> consumer) {
		stream().filter(r -> r instanceof AnimationRenderable)
				.forEach(r -> consumer.accept((AnimationRenderable)r));
	}
	
}
