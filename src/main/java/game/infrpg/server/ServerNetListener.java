package game.infrpg.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.infrpg.server.util.ServerConfig;
import java.io.Closeable;
import java.io.IOException;
import lib.logger.ILogger;


/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerNetListener extends Listener implements Closeable {
	
	private final ILogger logger;
	private final Server server;
	public final int port;
	
	public ServerNetListener(ILogger logger, ServerConfig config) {
		this.logger = logger;
		this.port = config.port;
		
		this.server = new Server();
	}
	
	public void start() throws IOException {
		logger.debug("Starting server endpoint on port " + port);
		
		server.start();
		server.bind(port, port);
		server.addListener(this);
		
		logger.debug("Server endpoint started");
	}
	
	public void stop() {
		logger.debug("Stopping server instance");
		server.stop();
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
		logger.debug("Received " + object.toString() + " on connection " + connection.toString());
	}

	@Override
	public void close() throws IOException {
		logger.debug("Closing ServerNetListener");
		
		try {
			stop();
		}
		catch (Exception e) {
			logger.logException(e);
		}
	}

	
	
}
