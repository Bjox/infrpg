package game.engine.client.logic;

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
