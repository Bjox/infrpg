package game.infrpg.client.rendering;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface RenderCallCounter {
	
	/**
	 * Get the number of render calls since last render.
	 * @return 
	 */
	public int getRenderCalls();
	
}
