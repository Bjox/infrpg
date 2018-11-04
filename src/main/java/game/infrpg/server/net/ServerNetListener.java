package game.infrpg.server.net;

import game.infrpg.common.net.INetHandler;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import game.infrpg.common.net.KryoRegistrations;
import game.infrpg.common.net.NetPacket;
import game.infrpg.common.util.Constants;
import game.infrpg.server.service.client.IClientService;
import game.infrpg.server.util.ServerConfig;
import java.io.Closeable;
import java.io.IOException;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerNetListener extends Listener implements Closeable
{

	private final ILogger logger;
	private final IClientService clientService;
	private final INetHandler netHandler;

	private final Server server;
	private final int port;

	@Inject
	public ServerNetListener(
		ILogger logger,
		ServerConfig config,
		IClientService clientService,
		INetHandler netHandler)
	{
		this.logger = logger;
		this.port = config.port;
		this.clientService = clientService;
		this.netHandler = netHandler;

		this.server = new Server();
		KryoRegistrations.register(server.getKryo(), logger);
	}

	public void start() throws IOException
	{
		logger.debug("Starting server endpoint on port " + port);

		server.start();
		server.bind(port, port);
		server.addListener(this);

		logger.debug("Server endpoint started");
	}

	@Override
	public void connected(Connection connection)
	{
		logger.debug("Connected " + connection.toString());
		clientService.clientConnected(connection);
	}

	@Override
	public void disconnected(Connection connection)
	{
		logger.debug("Disconnected " + connection.toString());
		clientService.clientDisconnected(connection);
	}

	@Override
	public void received(Connection connection, Object object)
	{
		if (Constants.NET_TRACE)
			logger.debug("Received " + object.toString() + " on connection " + connection.toString());
		
		if (object instanceof FrameworkMessage)
		{
			return;
		}

		if (!(object instanceof NetPacket))
		{
			logger.debug("Received object not instance of NetPacket. Discarding: " + object);
			return;
		}

		netHandler.handle((NetPacket) object, connection);
	}

	@Override
	public void close() throws IOException
	{
		logger.debug("Stopping server instance");
		server.stop();
	}

}
