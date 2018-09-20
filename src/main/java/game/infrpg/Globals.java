package game.infrpg;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * Global static fields for both client and server.
 * 
 * @author Bjørnar W. Alvestad
 */
public final class Globals {
	
	private static Injector injector;
	
	/**
	 * Gets the global injector.
	 * @return 
	 */
	public static Injector injector() {
		if (injector == null) {
			throw new NullPointerException("The injector has not been initialized. Call Globals.setupInjector(module).");
		}
		return injector;
	}
	
	/**
	 * Creates the global injector. Call once at program startup.
	 * @param stage
	 * @param module 
	 */
	public static void setupInjector(Stage stage, Module module) {
		injector = Guice.createInjector(stage, module);
	}
	
	/**
	 * Private constructor.
	 */
	private Globals() {}
}
