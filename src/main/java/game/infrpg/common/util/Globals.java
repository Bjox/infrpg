package game.infrpg;

import lib.di.Container;
import lib.di.IContainer;
import lib.logger.ILogger;
import lib.logger.Logger;

/**
 * Global static fields for both client and server.
 * 
 * @author Bjørnar W. Alvestad
 */
public final class Globals {
	
	/**
	 * The global dependency container.
	 */
	public static final IContainer container = new Container();
	
	/**
	 * Convenienvce method for resolving types from the global dependency container.
	 * @param <T>
	 * @param type
	 * @return 
	 */
	public static <T> T resolve(Class<T> type) {
		return container.resolve(type);
	}
	
	/**
	 * Convenience method for getting the global logger.
	 * @return 
	 */
	public static ILogger logger() {
		return resolve(Logger.class);
	}
	
	/**
	 * Private constructor.
	 */
	private Globals() {}
}
