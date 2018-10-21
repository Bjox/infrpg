package game.infrpg.common.net;

import game.infrpg.common.net.packets.ChunkRequest;
import game.infrpg.common.net.packets.ChunkResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class NetPacket implements Serializable {
	
	private static final Map<Class<? extends NetPacket>, Type> payloadTypeMap = new HashMap<>();
	
	public final Type type;

	public NetPacket() {
		this.type = payloadTypeMap.get(getClass());
	}
	
	public static enum Type {
		CHUNK_REQUEST(ChunkRequest.class),
		CHUNK_RESPONSE(ChunkResponse.class),
		;
		
		private Type(Class<? extends NetPacket> payloadType) {
			payloadTypeMap.put(payloadType, this);
		}		
	}
	
	
}
