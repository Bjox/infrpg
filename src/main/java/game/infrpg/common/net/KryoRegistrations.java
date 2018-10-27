package game.infrpg.common.net;

import game.infrpg.common.net.packets.ChunkRequest;
import game.infrpg.common.net.packets.ChunkResponse;
import com.esotericsoftware.kryo.Kryo;
import game.infrpg.server.map.Chunk;
import lib.logger.ILogger;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class KryoRegistrations {
	
	private static final Class[] CLASSES = {
		NetPacket.class,
		NetPacket.Type.class,
		
		ChunkRequest.class,
		ChunkResponse.class,
		
		byte[].class,
		byte[][].class,
		
		Chunk.class,
		Point.class
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
