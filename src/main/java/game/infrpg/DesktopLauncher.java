package game.infrpg;

import game.infrpg.common.Arguments;
import game.infrpg.client.InfrpgClient;
import game.infrpg.client.logic.Constants;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.util.logging.ConsoleHandler;
import game.infrpg.common.console.util.logging.FileHandler;
import game.infrpg.common.console.util.logging.Level;
import game.infrpg.common.console.util.logging.Logger;
import game.infrpg.common.console.util.logging.PrintStreamHandler;
import game.infrpg.common.util.ArgumentParser;
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

		if (Constants.HEADLESS && !Constants.SERVER) {
			logger.warning("Ignoring headless flag because this is a client.");
		}

		if (!Constants.HEADLESS) {
			Console.createConsole("Infrpg console");
			//Console.attachToOut();
			//Console.attachToErr();
			Console.showConsole();
			logger.addHandler(new ConsoleHandler());
		}

		try {
			logger.addHandler(new FileHandler("last_run.log"));
		}
		catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}

		if (Constants.DEBUG) {
			argp.printAllOptions();
		}
		logger.info(argp);

		Instance instance;
		if (argp.isPresent(Arguments.SERVER)) {
			instance = new InfrpgServer(argp);
		}
		else {
			instance = new InfrpgClient(argp);
		}
		instance.start();
	}
}
