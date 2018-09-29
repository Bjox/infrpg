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
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.util.Helpers;
import lib.logger.FileLoggerHandler;
import lib.logger.LoggerLevel;
import lib.logger.Logger;
import lib.logger.PrintStreamLoggerHandler;
import lib.util.ArgumentParser;
import java.io.IOException;
import java.util.Locale;
import lib.config.IConfigStore;
import lib.config.PropertiesConfigStore;
import lib.logger.ILogger;
import lib.storage.FileStorage;
import lib.storage.IStorage;
import lib.util.IArgumentParser;

public class DesktopLauncher {

	public static void main(String[] args) {
		setupArguments(args);
		setupLogger();
		ILogger logger = Globals.logger();
		setupConsole(logger);
		Locale.setDefault(Locale.ENGLISH);
		
		logger.debug(resolve(ArgumentParser.class));

		if (Globals.DEBUG) {
			Console.showConsole();
		}

		try {
			if (Globals.SERVER) {
				logger.info("Infrpg server");
				serverRegistrations();
				startServer();
			}
			else {
				logger.info("Infrpg client");
				clientRegistrations();
				startClient(resolve(ClientConfig.class));
			}
		}
		catch (Exception e) {
			logger.logException(e);
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

	private static void clientRegistrations() throws Exception {
		Globals.container.registerSingleton(IConfigStore.class, new PropertiesConfigStore("Infrpg client configuration"));
		// TODO: registering generic IStorage to ClientConfig specific FileStorage
		Globals.container.registerSingleton(IStorage.class, new FileStorage(Constants.CLIENT_CONFIG_PATHNAME)); 
		Globals.container.resolveAndRegisterInstance(ClientConfig.class).initConfig();
	}

	private static void serverRegistrations() throws Exception {
	}

	private static void startClient(ClientConfig clientConfig) {
		LwjglApplicationConfiguration lwjglAppConfig = new LwjglApplicationConfiguration();
		
		lwjglAppConfig.width = clientConfig.screenWidth;
		lwjglAppConfig.height = clientConfig.screenHeight;
		lwjglAppConfig.title = Constants.CLIENT_WINDOW_TITLE;
		lwjglAppConfig.vSyncEnabled = clientConfig.verticalSync;
		lwjglAppConfig.foregroundFPS = Helpers.getClientForegroundFpsLimit();
		lwjglAppConfig.backgroundFPS = Helpers.getClientBackgroundFpsLimit();

		Globals.container.registerInstance(lwjglAppConfig);

		new LwjglApplication(Globals.container.resolveAndRegisterInstance(InfrpgGame.class), lwjglAppConfig);
	}

	private static void startServer() {
		resolve(InfrpgServer.class).start();
	}
}
