package game.infrpg;

import static game.infrpg.common.util.Globals.resolve;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Arguments;
import game.infrpg.client.InfrpgGame;
import game.infrpg.common.util.Constants;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.ConsoleLoggerHandler;
import game.infrpg.server.InfrpgServer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import lib.logger.FileLoggerHandler;
import lib.logger.LoggerLevel;
import lib.logger.Logger;
import lib.logger.PrintStreamLoggerHandler;
import lib.util.ArgumentParser;
import java.io.IOException;
import lib.logger.ILogger;
import lib.util.IArgumentParser;

public class DesktopLauncher {
	
	public static void main(String[] args) {
		setupArguments(args);
		setupLogger();
		ILogger logger = Globals.logger();
		setupConsole(logger);
		
		logger.debug(resolve(ArgumentParser.class));
		
		if (Globals.DEBUG) {
			Console.showConsole();
		}

		if (Globals.SERVER) {
			logger.info("Infrpg server");
			serverRegistrations();
			startServer();
		}
		else {
			logger.info("Infrpg client");
			clientRegistrations();
			startClient();
		}
	}
	
	private static void setupArguments(String[] args) {
		Globals.container.registerType(IArgumentParser.class, ArgumentParser.class);
		IArgumentParser<Arguments> arguments = Globals.container.registerInstance(new ArgumentParser<>(args));

		Globals.DEBUG = arguments.isPresent(Arguments.DEBUG);
		Globals.SERVER = arguments.isPresent(Arguments.SERVER);
		Globals.HEADLESS = arguments.isPresent(Arguments.HEADLESS);
	}
	
	private static void setupLogger() {
		Globals.container.registerSingleton(ILogger.class, Logger.class);
		ILogger logger = Globals.container.resolve(ILogger.class);
		
		logger.addHandler(new PrintStreamLoggerHandler(System.out, false));
		logger.setCurrentLevel(Globals.DEBUG ? LoggerLevel.ALL : LoggerLevel.DEFAULT);

		try {
			logger.addHandler(new FileLoggerHandler(Constants.LOGFILE));
		}
		catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}
	}
	
	private static void setupConsole(ILogger logger) {
		if (Globals.HEADLESS) {
			logger.info("Running in headless mode");
			if (!Globals.SERVER) {
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
	
	private static void clientRegistrations() {
	}
	
	private static void serverRegistrations() {
	}
	
	private static void startClient() {
		LwjglApplicationConfiguration lwjglAppConfig = new LwjglApplicationConfiguration();
		
		lwjglAppConfig.width = 1000;
		lwjglAppConfig.height = 800;
		lwjglAppConfig.title = "Infrpg";
		lwjglAppConfig.vSyncEnabled = false;
		lwjglAppConfig.foregroundFPS = Globals.DEBUG ? 0 : 200;
		lwjglAppConfig.backgroundFPS = 30;
		
		Globals.container.registerInstance(lwjglAppConfig);
		
		new LwjglApplication(resolve(InfrpgGame.class), lwjglAppConfig);
	}
	
	private static void startServer() {
		resolve(InfrpgServer.class).start();
	}
}
