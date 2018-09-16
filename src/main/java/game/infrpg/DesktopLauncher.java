package game.infrpg;

import game.engine.common.util.Arguments;
import game.infrpg.client.InfrpgClient;
import game.engine.client.logic.Constants;
import game.engine.common.Instance;
import game.engine.common.console.Console;
import game.engine.common.console.logging.ConsoleHandler;
import game.engine.common.console.logging.FileHandler;
import game.engine.common.console.logging.Level;
import game.engine.common.console.logging.Logger;
import game.engine.common.console.logging.PrintStreamHandler;
import game.engine.common.util.ArgumentParser;
import game.infrpg.server.InfrpgServer;
import java.io.IOException;

public class DesktopLauncher {

	public static void main(String[] arg) {
//		TexturePacker.Settings texSettings = new TexturePacker.Settings();
//		TexturePacker.process(texSettings, "raw/", "packed/", "pack");

		ArgumentParser argp = new ArgumentParser(arg);

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
				instance = new InfrpgServer(argp);
			}
			else {
				instance = new InfrpgClient(argp);
			}
			instance.start();
		}
		catch (Exception e) {
			logger.trackException(e);
		}
	}
}
