package game.infrpg;

import lib.di.Container;
import lib.di.IContainer;

/**
 * Global static fields for both client and server.
 * 
 * @author Bjørnar W. Alvestad
 */
public final class Globals {
	
	public static final IContainer container = new Container();
	
	/**
	 * Private constructor.
	 */
	private Globals() {}
}
