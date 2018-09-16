package game.infrpg;

import game.engine.common.util.Arguments;
import game.infrpg.client.ClientInstance;
import game.engine.client.logic.Constants;
import game.engine.client.util.ClientConfig;
import game.engine.common.Instance;
import game.engine.common.console.Console;
import game.engine.common.console.logging.ConsoleHandler;
import game.engine.common.console.logging.FileHandler;
import game.engine.common.console.logging.Level;
import game.engine.common.console.logging.Logger;
import game.engine.common.console.logging.PrintStreamHandler;
import game.engine.common.util.ArgumentParser;
import game.infrpg.server.ServerInstance;
import java.io.IOException;

public class DesktopLauncher {

	public static void main(String[] args) {
		ArgumentParser argp = new ArgumentParser(args);

		Logger logger = Logger.getPublicLogger();
		logger.addHandler(new PrintStreamHandler(System.out, false));

		Constants.DEBUG = argp.isPresent(Arguments.DEBUG);
		logger.setCurrentLevel(Constants.DEBUG ? Level.ALL : Level.DEFAULT);

		Constants.SERVER = argp.isPresent(Arguments.SERVER);
		Constants.HEADLESS = argp.isPresent(Arguments.HEADLESS);
		
		if (Constants.HEADLESS) {
			logger.info("Running in headless mode");
			if (!Constants.SERVER) {
				logger.warning("Headless flag is not applicable on a client instance");
			}
		}

		if (!Constants.HEADLESS) {
			Console.createConsole("Infrpg console");
			Console.attachToOut();
			Console.attachToErr();
			Console.showConsole();
			logger.addHandler(new ConsoleHandler());
		}

		try {
			logger.addHandler(new FileHandler("last_run.log"));
		}
		catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}
		
		logger.info(argp);

		try {
			Instance instance;
			if (argp.isPresent(Arguments.SERVER)) {
				instance = new ServerInstance(args);
			}
			else {
				ClientConfig clientConfig = new ClientConfig();
				
				clientConfig.screenWidth = 1000;
				clientConfig.screenHeight = 800;
				clientConfig.fpsForeground = 0;
				clientConfig.fpsBackground = 30;
				clientConfig.vSync = false;
				clientConfig.windowTitle = "Game";
				
				instance = new ClientInstance(args, clientConfig);
			}
			instance.start();
		}
		catch (Exception e) {
			logger.trackException(e);
		}
	}
}
