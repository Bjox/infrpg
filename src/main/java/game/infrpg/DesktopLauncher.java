package game.infrpg;

import com.google.inject.Stage;
import game.infrpg.common.util.Arguments;
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
import lib.ArgumentParser;
import game.infrpg.server.ServerInstance;
import java.io.IOException;
import java.util.stream.Stream;

public class DesktopLauncher {
	
	public static void main(String[] args) {
		InitializeGlobals(args);
		
		ArgumentParser<Arguments> arguments = new ArgumentParser<>(args);
		
		Logger logger = Logger.getPublicLogger();
		logger.addHandler(new PrintStreamHandler(System.out, false));

		Constants.DEBUG = arguments.isPresent(Arguments.DEBUG);
		Constants.SERVER = arguments.isPresent(Arguments.SERVER);
		Constants.HEADLESS = arguments.isPresent(Arguments.HEADLESS);
		
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
		
		logger.info(arguments);

		try {
			Instance instance;
			
			if (arguments.isPresent(Arguments.SERVER)) {
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
	
	private static void InitializeGlobals(String[] args) {
//		boolean debug = Stream.of(args).anyMatch(arg -> "-debug".equals(arg));
//		Stage stage = debug ? Stage.DEVELOPMENT : Stage.PRODUCTION;
//		Globals.setupInjector(stage, new CommonModule(args));
	}
}
