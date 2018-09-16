package game.infrpg.server;

import game.engine.server.util.Property;
import game.engine.server.util.ServerProperties;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import game.engine.client.logic.Constants;
import game.engine.server.map.IMapStorage;
import game.engine.server.map.sqlite.SQLiteMapStorage;
import game.engine.common.Instance;
import game.engine.common.console.Console;
import game.engine.common.util.ArgumentParser;
import game.engine.server.map.Region;
import game.engine.server.service.MapService;
import java.io.File;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InfrpgServer extends Instance implements ApplicationListener {

	private static final int TICKRATE = 20;
	
	public InfrpgServer(ArgumentParser args) {
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
		
//		File dbFile = new File("testmap.db");
//		
//		try (IMapStorage mapStorage = new SQLiteMapStorage(dbFile)) {
//			logger.debug("Map storage created");
//			
////			Region r = new Region(0, 0);
////			mapStorage.storeRegion(r);
//
//			mapStorage.getRegion(0, 0);
//		}
//		catch (Exception e) {
//			logger.trackException(e);
//		}

		try {
			ServerProperties properties = new ServerProperties(new File("server.properties"));
			
			if (Constants.DEBUG) {
				logger.debug(properties);
			}
			
			MapService mapservice = new MapService(new File(properties.getProperty(Property.MAP_DIRECTORY)));
		}
		catch (Exception e) {
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
