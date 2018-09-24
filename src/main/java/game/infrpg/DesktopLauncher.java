package game.infrpg;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import static game.infrpg.Globals.resolve;
import game.infrpg.common.util.Arguments;
import game.infrpg.client.InfrpgGame;
import game.infrpg.client.util.Constants;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.ConsoleLoggerHandler;
import lib.logger.FileLoggerHandler;
import lib.logger.LoggerLevel;
import lib.logger.Logger;
import lib.logger.PrintStreamLoggerHandler;
import lib.ArgumentParser;
import game.infrpg.server.InfrpgServer;
import java.io.IOException;
import lib.logger.ILogger;

public class DesktopLauncher {
	
	public static void main(String[] args) {
		setupArguments(args);
		setupLogger();
		
		Logger logger = resolve(Logger.class);
		ArgumentParser<Arguments> arguments = resolve(ArgumentParser.class);
		
		setupConsole(logger);
		
		logger.info(arguments);
		
		if (Constants.DEBUG) {
			Console.showConsole();
		}

		try {
			if (Constants.SERVER) {
				startServer();
			}
			else {
				startClient();
			}
		}
		catch (Exception e) {
			logger.logException(e);
		}
	}
	
	private static void setupArguments(String[] args) {
		ArgumentParser<Arguments> arguments = Globals.container.registerInstance(new ArgumentParser<>(args));

		Constants.DEBUG = arguments.isPresent(Arguments.DEBUG);
		Constants.SERVER = arguments.isPresent(Arguments.SERVER);
		Constants.HEADLESS = arguments.isPresent(Arguments.HEADLESS);
	}
	
	private static void setupLogger() {
		Logger logger = Globals.container.registerInstance(new Logger());
		
		logger.addHandler(new PrintStreamLoggerHandler(System.out, false));
		logger.setCurrentLevel(Constants.DEBUG ? LoggerLevel.ALL : LoggerLevel.DEFAULT);

		try {
			logger.addHandler(new FileLoggerHandler(Constants.LOGFILE));
		}
		catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}
	}
	
	private static void setupConsole(Logger logger) {
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
			logger.addHandler(new ConsoleLoggerHandler());
		}
	}
	
	private static void startClient() {
		LwjglApplicationConfiguration lwjglAppConfig = new LwjglApplicationConfiguration();
		
		lwjglAppConfig.width = 1000;
		lwjglAppConfig.height = 800;
		lwjglAppConfig.title = "Infrpg";
		lwjglAppConfig.vSyncEnabled = false;
		lwjglAppConfig.foregroundFPS = Constants.DEBUG ? 0 : 200;
		lwjglAppConfig.backgroundFPS = 30;
		
		Globals.container.registerInstance(lwjglAppConfig);
		
		// Bootstraps the game
		new LwjglApplication(resolve(InfrpgGame.class), lwjglAppConfig);
	}
	
	private static void startServer() {
		resolve(InfrpgServer.class).start();
	}
}
