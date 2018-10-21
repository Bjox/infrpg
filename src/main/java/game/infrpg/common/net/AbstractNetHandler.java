package game.infrpg.common.net;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractNetHandler implements INetHandler {
	
	protected void tcpResponse(Connection connection, NetPacket packet) {
		connection.sendTCP(packet);
	}
	
	protected void udpResponse(Connection connection, NetPacket packet) {
		connection.sendUDP(packet);
	}
	
}
