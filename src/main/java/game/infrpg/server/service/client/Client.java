package game.infrpg.server.service.client;

import com.esotericsoftware.kryonet.Connection;
import java.io.Closeable;
import java.io.IOException;

/**
 * Represents a client connected to the server.
 * 
 * @author Bjørnar W. Alvestad
 */
public class Client implements Closeable {
	
	private final Connection connection;
	private final String hostnameString;
	
	public Client(Connection connection) {
		this.connection = connection;
		this.hostnameString = connection.getRemoteAddressTCP().getHostString();
	}

	@Override
	public void close() throws IOException {
		connection.close();
	}

	@Override
	public String toString() {
		return String.format("Client %s", hostnameString);
	}
	
}
