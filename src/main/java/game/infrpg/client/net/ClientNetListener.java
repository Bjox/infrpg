package game.infrpg.client.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
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
public class ClientNetListener extends Listener implements Closeable
{
	private final ILogger logger;
	private final Client client;
	private final ClientNetHandler netHandler;

	@Inject
	public ClientNetListener(
		ILogger logger,
		ClientNetHandler netHandler)
	{
		this.logger = logger;
		this.client = new Client();
		this.netHandler = netHandler;
		KryoRegistrations.register(client.getKryo(), logger);
	}
	
	public void sendTCP(NetPacket packet)
	{
		if (!isConnected())
		{
			logger.error("Cannot send packet " + packet + ". Client is not connected.");
			return;
		}
		client.sendTCP(packet);
	}
	
	public void sendUDP(NetPacket packet)
	{
		if (!isConnected())
		{
			logger.error("Cannot send packet " + packet + ". Client is not connected.");
			return;
		}
		client.sendUDP(packet);
	}

	public void connect(String ip, int port) throws IOException
	{
		client.stop();
		
		logger.debug("Connecting client");
		
		client.start();
		client.connect(Constants.CLIENT_CONNECTION_TIMEOUT, ip, port, port);
		client.addListener(this);
	}
	
	private boolean isConnected()
	{
		return client != null && client.isConnected();
	}

	@Override
	public void connected(Connection connection)
	{
		logger.debug("Connected " + connection.toString());
	}

	@Override
	public void disconnected(Connection connection)
	{
		logger.debug("Disconnected " + connection.toString());
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
		logger.debug("Stopping client");
		client.stop();
	}

	public ClientNetHandler getHandler()
	{
		return netHandler;
	}

}
