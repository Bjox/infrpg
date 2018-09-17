package game.infrpg;

import game.infrpg.common.util.args.Arguments;
import game.infrpg.client.ClientInstance;
import game.infrpg.client.util.Constants;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.logging.ConsoleHandler;
import game.infrpg.common.console.logging.FileHandler;
import game.infrpg.common.console.logging.Level;
import game.infrpg.common.console.logging.Logger;
import game.infrpg.common.console.logging.PrintStreamHandler;
import game.infrpg.common.util.args.ArgumentParser;
import game.infrpg.server.ServerInstance;
import java.io.IOException;

public class DesktopLauncher {

	public static void main(String[] args) {
		ArgumentParser argp = new ArgumentParser(args);

		Logger logger = Logger.getPublicLogger();
		logger.addHandler(new PrintStreamHandler(System.out, false));

		Constants.DEBUG = argp.isPresent(Arguments.DEBUG);
		Constants.SERVER = argp.isPresent(Arguments.SERVER);
		Constants.HEADLESS = argp.isPresent(Arguments.HEADLESS);
		
		logger.setCurrentLevel(Constants.DEBUG ? Level.ALL : Level.DEFAULT);
		
		if (Constants.HEADLESS) {
			logger.info("Running in headless mode");
			if (!Constants.SERVER) {
				logger.warning("Headless flag is not applicable on a client instance");
			}
		}
		else {
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
				clientConfig.windowTitle = "Infrpg";
				
				instance = new ClientInstance(args, clientConfig);
			}
			
			instance.start();
		}
		catch (Exception e) {
			logger.logException(e);
		}
	}
}
