package game.infrpg.server.service.client;

import com.esotericsoftware.kryonet.Connection;
import game.infrpg.server.service.Service;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientService extends Service implements IClientService {

	private final ILogger logger;
	private final Map<Integer, Client> clients;

	@Inject
	public ClientService(ILogger logger) {
		this.logger = logger;
		this.clients = new HashMap<>();
	}

	@Override
	public void clientConnected(Connection connection) {
		int id = connection.getID();
		
		if (id == -1) {
			logger.debug("Connected with connection id -1.");
			return;
		}
		if (clients.containsKey(id)) {
			logger.warning("Client id " + id + " already added.");
			return;
		}
		
		Client client = new Client(connection);
		clients.put(id, client);
		
		logger.info("Client connected: " + client);
	}

	@Override
	public void clientDisconnected(Connection connection) {
		int id = connection.getID();
		
		if (id == -1) {
			logger.warning("Disconnected with connection id -1.");
			return;
		}
		if (!clients.containsKey(id)) {
			logger.warning("Disconnected client was not present in the clients collection.");
			return;
		}
		
		Client removedClient = clients.remove(id);
		
		logger.info("Client disconnected: " + removedClient);
	}

	@Override
	public void close() throws IOException {
		clients.values().forEach((client) -> {
			try {
				client.close();
			}
			catch (IOException e) {
				logger.error("Error closing client: " + client);
			}
		});
	}
}
