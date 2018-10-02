package game.infrpg.common.util;

import static game.infrpg.common.util.Constants.*;
import static game.infrpg.common.util.Globals.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Helpers {
	
	public static int getClientForegroundFpsLimit() {
		return DEBUG ? 0 : CLIENT_FOREGROUND_FPS_LIMIT;
	}
	
	public static int getClientBackgroundFpsLimit() {
		return DEBUG ? 0 : CLIENT_BACKGROUND_FPS_LIMIT;
	}
	
	public static RuntimeException wrapInRuntimeException(Throwable t) {
		return new RuntimeException(t);
	}
	
	/**
	 * Private constructor.
	 */
	private Helpers() {
	}
}
