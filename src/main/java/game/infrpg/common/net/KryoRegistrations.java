package game.infrpg.common.net;

import com.esotericsoftware.kryo.Kryo;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class KryoRegistrations {
	
	private static final Class[] CLASSES = {
		
	};
	
	public static void register(Kryo kryo, ILogger logger) {
		for (Class c : CLASSES) {
			logger.debug("Registering class \"" + c.getSimpleName() + "\"");
			kryo.register(c);
		}
	}

	
	
	private KryoRegistrations() {
	}
	
}
