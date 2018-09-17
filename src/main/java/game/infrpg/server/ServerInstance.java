package game.infrpg.server;

import game.infrpg.server.util.ServerProperty;
import game.infrpg.server.util.ServerProperties;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import game.infrpg.client.util.Constants;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.sqlite.SQLiteMapStorage;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.ArgumentParser;
import game.infrpg.server.map.Region;
import game.infrpg.server.service.map.MapService;
import java.io.File;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ServerInstance extends Instance implements ApplicationListener {

	private static final int TICKRATE = 20;
	
	public ServerInstance(String[] args) {
		super(args);
		logger.info("Infrpg Server");
	}

	@Override
	public void start() {
		logger.info("Setting up server...");
		
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		config.renderInterval = 1f / TICKRATE;
		
		new HeadlessApplication(this, config);
		
		if (!Constants.HEADLESS) {
			Console.addShutdownHook(() -> Gdx.app.exit());
		}

		try {
			ServerProperties properties = new ServerProperties(new File("server.properties"));
			
			if (Constants.DEBUG) {
				logger.debug(properties);
			}
			
			MapService mapservice = new MapService(new File(properties.getProperty(ServerProperty.MAP_DIRECTORY)));
		}
		catch (Exception e) {
			logger.logException(e);
		}
		
		logger.info("Server setup complete");

	}

	@Override
	public void create() {
		logger.debug("Server create");
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
