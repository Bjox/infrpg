package game.infrpg.client.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.infrpg.common.net.INetHandler;
import game.infrpg.common.net.KryoRegistrations;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.util.Constants;
import java.io.Closeable;
import java.io.IOException;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientNetListener extends Listener implements Closeable {
	
	private final ILogger logger;
	private final Client client;
	private final INetHandler netHandler;
	
	@Inject
	public ClientNetListener(
		ILogger logger,
		INetHandler netHandler
	) {
		this.logger = logger;
		this.client = new Client();
		this.netHandler = netHandler;
		KryoRegistrations.register(client.getKryo(), logger);
	}
	
	public void connect(String ip, int port) throws IOException {
		logger.debug("Connecting client");
		client.start();
		client.connect(Constants.CLIENT_CONNECTION_TIMEOUT, ip, port, port);
		client.addListener(this);
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
		
		if (!(object instanceof NetPacket)) {
			logger.debug("Received object not instance of NetPacket. Discarding.");
			return;
		}
		
		netHandler.handle((NetPacket) object, connection);
	}

	@Override
	public void close() throws IOException {
		logger.debug("Stopping client");
		client.stop();
	}
	
	
	
}
