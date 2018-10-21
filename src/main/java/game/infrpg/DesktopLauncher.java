package game.infrpg;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import static game.infrpg.common.util.Globals.resolve;
import static game.infrpg.common.util.Globals.container;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Arguments;
import game.infrpg.client.InfrpgGame;
import game.infrpg.common.util.Constants;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.ConsoleLoggerHandler;
import game.infrpg.server.InfrpgServer;
import game.infrpg.client.ClientCommands;
import game.infrpg.client.net.ClientNetHandler;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.util.Helpers;
import game.infrpg.server.map.storage.IMapStorage;
import game.infrpg.server.net.ServerNetHandler;
import game.infrpg.server.service.client.ClientService;
import game.infrpg.server.service.client.IClientService;
import game.infrpg.server.service.map.IMapService;
import game.infrpg.server.service.map.MapService;
import game.infrpg.server.service.mapgen.FlatgrassGenerator;
import game.infrpg.server.service.mapgen.IMapGenerator;
import game.infrpg.server.service.mapgen.ISeedProvider;
import game.infrpg.server.service.mapgen.SeedProvider;
import game.infrpg.server.util.ServerConfig;
import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Stream;
import lib.cache.Cache;
import lib.logger.FileLoggerHandler;
import lib.logger.LoggerLevel;
import lib.logger.Logger;
import lib.logger.PrintStreamLoggerHandler;
import lib.util.ArgumentParser;
import lib.config.IConfigStore;
import lib.config.PropertiesConfigStore;
import lib.logger.ILogger;
import lib.storage.FileStorage;
import lib.storage.IStorage;
import lib.util.IArgumentParser;
import lib.cache.ICache;
import lib.cmd.CommandDispatcher;
import lib.cmd.CommandObject;
import game.infrpg.common.net.INetHandler;
import game.infrpg.server.net.ServerNetListener;

public class DesktopLauncher {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		setupArguments(args);

		IArgumentParser<Arguments> arguments = resolve(IArgumentParser.class);
		if (arguments.isPresent(Arguments.USAGE)) {
			printUsage();
			return;
		}
		else {
			System.out.println("Run with -usage to get a list of possible command line arguments.");
		}

		setupLogger();
		ILogger logger = Globals.logger();
		Locale.setDefault(Locale.ENGLISH);

		setupConsole(logger);
		if (Globals.DEBUG && !Globals.HEADLESS) {
			Console.showConsole();

			if (Globals.SERVER) {
				Console.setConsoleSize(new Dimension(1000, 600));
			}
		}

		logger.debug(arguments);

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

	private static void printUsage() {
		System.out.println("--Infrpg usage" + String.join("", Collections.nCopies(58, "-")));
		Stream.of(Arguments.values())
			.map(arg -> String.format("   -%-15s%s\n\n", arg.name().toLowerCase(), arg.description))
			.forEach(System.out::printf);
	}

	private static void setupArguments(String[] args) {
		IArgumentParser<Arguments> arguments = new ArgumentParser<>(args);

		Globals.DEBUG = arguments.isPresent(Arguments.DEBUG);
		Globals.SERVER = arguments.isPresent(Arguments.SERVER);
		Globals.HEADLESS = arguments.isPresent(Arguments.HEADLESS);

		container.registerSingleton(IArgumentParser.class, arguments);
	}

	private static void setupLogger() {
		container.registerInstance(new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN));

		container.registerSingleton(ILogger.class, Logger.class);
		ILogger logger = container.resolve(ILogger.class);

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

			return;
		}

		Console.attachToOut();
		Console.attachToErr();
		logger.addHandler(new ConsoleLoggerHandler());
	}

	// TODO: registering generic IStorage to ClientConfig specific FileStorage
	private static void clientRegistrations() throws Exception {
		container.registerSingleton(IConfigStore.class, new PropertiesConfigStore("Infrpg client configuration"));
		container.registerSingleton(IStorage.class, new FileStorage(Constants.CLIENT_CONFIG_PATHNAME));
		container.resolveAndRegisterInstance(ClientConfig.class).initConfig();

		container.registerSingleton(CommandObject.class, ClientCommands.class);
		container.registerSingleton(CommandDispatcher.class);
		
		container.registerType(INetHandler.class, ClientNetHandler.class);
	}

	// TODO: registering generic IStorage to ServerConfig specific FileStorage
	private static void serverRegistrations() throws Exception {
		container.registerSingleton(IConfigStore.class, new PropertiesConfigStore("Infrpg server configuration"));
		container.registerSingleton(IStorage.class, new FileStorage(Constants.SERVER_CONFIG_PATHNAME));
		container.resolveAndRegisterInstance(ServerConfig.class).initConfig();

		container.registerType(IMapGenerator.class, FlatgrassGenerator.class);
		container.registerType(ISeedProvider.class, SeedProvider.class);
		container.registerSingleton(IMapStorage.class, Constants.MAP_STORAGE_TYPE);
		container.registerSingleton(IMapService.class, MapService.class);

		container.registerType(ICache.class, Cache.class);

		container.registerSingleton(ServerNetListener.class);
		container.registerSingleton(IClientService.class, ClientService.class);
		container.registerType(INetHandler.class, ServerNetHandler.class);
	}

	private static void startClient(ClientConfig clientConfig) {
		LwjglApplicationConfiguration lwjglAppConfig = new LwjglApplicationConfiguration();
		lwjglAppConfig.width = clientConfig.screenWidth;
		lwjglAppConfig.height = clientConfig.screenHeight;
		lwjglAppConfig.title = Constants.CLIENT_WINDOW_TITLE;
		lwjglAppConfig.vSyncEnabled = clientConfig.verticalSync;
		lwjglAppConfig.foregroundFPS = Helpers.getClientForegroundFpsLimit();
		lwjglAppConfig.backgroundFPS = Helpers.getClientBackgroundFpsLimit();

		container.registerInstance(lwjglAppConfig);

		new LwjglApplication(container.resolveAndRegisterInstance(InfrpgGame.class), lwjglAppConfig);
	}

	private static void startServer() {
		HeadlessApplicationConfiguration headlessAppConfig = new HeadlessApplicationConfiguration();
		headlessAppConfig.renderInterval = 1f / Constants.SERVER_TICKRATE;

		container.registerInstance(headlessAppConfig);

		new HeadlessApplication(container.resolveAndRegisterInstance(InfrpgServer.class), headlessAppConfig);
	}
}
