package game.infrpg.server;

import com.badlogic.gdx.Gdx;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.Globals;
import game.infrpg.server.service.map.IMapService;
import game.infrpg.server.util.ServerConfig;
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
	
	@Inject
	public InfrpgServer(ILogger logger, ServerConfig config, IMapService mapService) {
		super(logger);
		
		this.logger = logger;
		this.serverConfig = config;
		this.mapService = mapService;
		
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
		}
		catch (Exception e) {
			throw new RuntimeException(e);
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
		
		try {
			mapService.close();
		}
		catch (Exception e) {
			logger.logException(e);
		}
	}
	
}
