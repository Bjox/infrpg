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
	
	private static final String[] BYTE_UNITS = { "B", "KiB", "MiB", "GiB", "TiB" };
	public static String formatSizeInBytes(long bytes) {
		int unit = 0;
		while (bytes >= 1024 && unit < BYTE_UNITS.length) {
			bytes /= 1024;
			unit++;
		}
		return String.format("%d %s", bytes, BYTE_UNITS[unit]);
	}
	
	/**
	 * Private constructor.
	 */
	private Helpers() {
	}
}
