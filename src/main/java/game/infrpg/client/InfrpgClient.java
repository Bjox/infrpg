package game.infrpg.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.infrpg.common.Instance;
import game.infrpg.common.util.ArgumentParser;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InfrpgClient extends Instance {

	public InfrpgClient(ArgumentParser args) {
		super(args);
	}

	@Override
	public void start() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = 800;
		config.title = "infRpg";
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = 30;
		
		InfrpgGame game = new InfrpgGame(config);
		
		// Bootstraps the game
		new LwjglApplication(game, config);
	}

}
