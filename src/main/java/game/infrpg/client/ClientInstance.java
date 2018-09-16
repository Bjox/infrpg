package game.infrpg.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.engine.client.util.ClientConfig;
import game.engine.common.Instance;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientInstance extends Instance {
	
	private final ClientConfig config;
	
	/**
	 * 
	 * @param args Command line arguments
	 * @param config Client configuration
	 */
	public ClientInstance(String[] args, ClientConfig config) {
		super(args);
		this.config = config;
	}

	@Override
	public void start() {
		logger.debug("Starting client");
		
		LwjglApplicationConfiguration lwjglAppConfig = new LwjglApplicationConfiguration();
		lwjglAppConfig.width = config.screenWidth;
		lwjglAppConfig.height = config.screenHeight;
		lwjglAppConfig.title = config.windowTitle;
		lwjglAppConfig.vSyncEnabled = config.vSync;
		lwjglAppConfig.foregroundFPS = config.fpsForeground;
		lwjglAppConfig.backgroundFPS = config.fpsBackground;
		
		// Bootstraps the game
		new LwjglApplication(new InfrpgGame(lwjglAppConfig), lwjglAppConfig);
	}

}
