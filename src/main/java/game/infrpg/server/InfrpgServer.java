package game.infrpg.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import game.infrpg.client.logic.Constants;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.sqlite.SQLiteMapStorage;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.ArgumentParser;
import game.infrpg.server.map.Region;
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
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		config.renderInterval = 1f / TICKRATE;
		new HeadlessApplication(this, config);
		
		if (!Constants.HEADLESS) {
			Console.addShutdownHook(() -> Gdx.app.exit());
		}
		
		File dbFile = new File("testmap.db");
		
		try (IMapStorage mapStorage = new SQLiteMapStorage(dbFile)) {
			logger.debug("Map storage created");
			
//			Region r = new Region(0, 0);
//			mapStorage.storeRegion(r);

			mapStorage.getRegion(0, 0);
		}
		catch (Exception e) {
			logger.trackException(e);
		}
	}

	@Override
	public void create() {
		logger.debug("Server create");
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
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
