package game.infrpg.client.entity;

import game.infrpg.client.rendering.renderable.AnimationRenderable;
import game.infrpg.client.rendering.renderable.Renderable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public class EnumRenderStateMap<T extends Enum<T>> {
	
	/** The state map. */
	private final EnumMap<T, Renderable> states;
	
	/** The key type used in the state map. */
	private final Class<T> stateKeyType;
	
	/** The render state id counter. */
	private int renderStateCounter;

	/**
	 * Create a new render state map with the specified initial capacity.
	 * @param stateKeyType
	 * @param initialSize 
	 */
	public EnumRenderStateMap(Class<T> stateKeyType, int initialSize) {
		//this.states = new Renderable[initialSize];
		//Arrays.fill(states, Renderable.NULL_RENDERABLE);
		this.stateKeyType = stateKeyType;
		this.states = new EnumMap(stateKeyType);
		this.renderStateCounter = 0;
	}
	
	/**
	 * Get the renderable for a specific state.
	 * @param renderState
	 * @return 
	 */
	public Renderable get(T renderState) {
		Renderable r = states.get(renderState);
		return r == null ? Renderable.NULL_RENDERABLE : r;
	}
	
	/**
	 * Set the renderable for a specific state.
	 * @param renderState
	 * @param renderable 
	 */
	public void set(T renderState, Renderable renderable) {
		if (renderable == null) renderable = Renderable.NULL_RENDERABLE;
		states.put(renderState, renderable);
	}
	
	/**
	 * Returns a stream of all the states in this render state map.
	 * This includes potential references to <code>Renderable.NULL_RENDERABLE</code>.
	 * @return 
	 */
	public Stream<Renderable> stream() {
		return states.values().stream();
	}
	
	/**
	 * Perform an action for each state in this render state map.
	 * This will not include <code>Renderable.NULL_RENDERABLE</code>.
	 * @param consumer 
	 */
	public void forEach(Consumer<Renderable> consumer) {
		stream().filter(r -> r != Renderable.NULL_RENDERABLE)
				.forEach(consumer);
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
