package game.infrpg.common.net;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface INetHandler {
	
	void handle(NetPacket packet, Connection connection);
	
}
