package game.infrpg.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.infrpg.common.console.util.logging.Logger;
import java.io.IOException;


/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerListener extends Listener {
	
	private final Logger logger;
	private final Server server;
	public final int port;
	
	public ServerListener(int port) {
		this.logger = Logger.getPublicLogger();
		this.server = new Server();
		this.port = port;
	}
	
	public void start() throws IOException {
		logger.debug("Starting server endpoint on port " + port);
		
		server.start();
		server.bind(port, port);
		server.addListener(this);
		
		logger.debug("Server endpoint started");
	}

	@Override
	public void connected(Connection connection) {
		logger.debug("Connected " + connection.toString());
	}

	@Override
	public void disconnected(Connection connection) {
		logger.debug("Disconnected " + connection.toString());
	}

	@Override
	public void received(Connection connection, Object object) {
	}

	
	
}
