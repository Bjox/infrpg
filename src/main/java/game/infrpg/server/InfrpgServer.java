package game.infrpg.server;

import game.infrpg.server.net.ServerNetListener;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.minlog.Log;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Helpers;
import game.infrpg.server.service.map.IMapService;
import game.infrpg.server.util.ServerConfig;
import java.io.Closeable;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InfrpgServer extends ServerApplicationListener {

	private final ILogger logger;
	private final ServerConfig serverConfig;
	private final IMapService mapService;
	private final ServerNetListener net;

	@Inject
	public InfrpgServer(
			ILogger logger,
			ServerConfig config,
			IMapService mapService,
			ServerNetListener net) {
		super(logger);

		this.logger = logger;
		this.serverConfig = config;
		this.mapService = mapService;
		this.net = net;

		this.logger.info("Server config: " + config.getConfigKeyValueMap());
	}

	@Override
	public void create() {
		logger.info("Setting up server...");

		if (!Globals.HEADLESS) {
			Console.addShutdownHook(() -> Gdx.app.exit());
		}
		
		try {
			mapService.init();
			net.start();
		}
		catch (Exception e) {
			throw Helpers.wrapInRuntimeException(e);
		}

		logger.info("Server setup complete");
	}

	@Override
	public void tick(float delta) {

	}

	@Override
	public void dispose() {
		logger.debug("Disposing server...");

		if (!Globals.HEADLESS) {
			Console.destroyConsole();
		}
		
		tryClose(net);
		tryClose(mapService);
	}
	
	private void tryClose(Closeable closeable) {
		String name = closeable.getClass().getSimpleName();
		logger.debug("Closing " + name);
		try {
			closeable.close();
		}
		catch (Exception e) {
			logger.error("An exception occurred while closing: " + name);
			logger.logException(e);
		}
	}

}
