package game.infrpg.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.Globals;
import game.infrpg.server.service.map.MapService;
import game.infrpg.server.util.ServerConfig;
import java.io.File;
import lib.di.Inject;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InfrpgServer implements ApplicationListener {
	
	private final ILogger logger;
	private final ServerConfig serverConfig;
	
	@Inject
	public InfrpgServer(ILogger logger, ServerConfig config) {
		this.logger = logger;
		this.serverConfig = config;
		
		this.logger.info("Server config: " + config.getConfigKeyValueMap());
	}

	@Override
	public void create() {
		logger.info("Setting up server...");
		
		if (!Globals.HEADLESS) {
			Console.addShutdownHook(() -> Gdx.app.exit());
		}

		try {
			MapService mapservice = new MapService(new File(serverConfig.mapDirectory));
		}
		catch (Exception e) {
			logger.logException(e);
		}
		
		logger.info("Server setup complete");
	}

	@Override
	public void render() {
		
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		logger.debug("Server pause");
	}

	@Override
	public void resume() {
		logger.debug("Server resume");
	}

	@Override
	public void dispose() {
		logger.debug("Server dispose");
	}
	
}
