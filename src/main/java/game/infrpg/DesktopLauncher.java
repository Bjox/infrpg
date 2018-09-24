package game.infrpg;

import static game.infrpg.Globals.resolve;
import game.infrpg.common.util.Arguments;
import game.infrpg.client.ClientInstance;
import game.infrpg.client.util.Constants;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.ConsoleLoggerHandler;
import lib.logger.FileLoggerHandler;
import lib.logger.LoggerLevel;
import lib.logger.Logger;
import lib.logger.PrintStreamLoggerHandler;
import lib.ArgumentParser;
import game.infrpg.server.ServerInstance;
import java.io.IOException;

public class DesktopLauncher {
	
	public static void main(String[] args) {
		setupArguments(args);
		setupLogger();
		
		Logger logger = resolve(Logger.class);
		ArgumentParser<Arguments> arguments = resolve(ArgumentParser.class);
		
		setupConsole(logger);
		
		logger.info(arguments);

		try {
			Instance instance;
			
			if (Constants.SERVER) {
				instance = new ServerInstance(args);
			}
			else {
				ClientConfig clientConfig = new ClientConfig();
				instance = new ClientInstance(args, clientConfig);
			}
			
			instance.start();
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
			Console.showConsole();
			logger.addHandler(new ConsoleLoggerHandler());
		}
	}
}
