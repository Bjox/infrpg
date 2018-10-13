package game.infrpg.server.service.client;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IClientService {
	
	void clientConnected(Connection connection);
	
	void clientDisconnected(Connection connection);
	
}
