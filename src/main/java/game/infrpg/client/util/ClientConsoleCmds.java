package game.infrpg.client.util;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.infrpg.client.InfrpgGame;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.cmd.Command;
import game.infrpg.common.console.cmd.CommandList;
import game.infrpg.common.util.Constants;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Helpers;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientConsoleCmds extends CommandList {

	public static class Maxfps extends Command {

		@Override
		public String getName() {
			return "maxfps";
		}

		@Override
		public void execute(String[] args) {
			try {
				int foregroundFps = Integer.parseInt(args[1]);
				if (foregroundFps < 0) {
					throw new Exception();
				}
				int backgroundFps = foregroundFps;
				int foregroundLimit = Helpers.getClientForegroundFpsLimit();
				if (foregroundLimit == 0) {
					foregroundLimit = Integer.MAX_VALUE;
				}
				foregroundFps = Math.min(foregroundFps, foregroundLimit);
				int backgroundLimit = Helpers.getClientBackgroundFpsLimit();
				if (backgroundLimit == 0) {
					backgroundLimit = Integer.MAX_VALUE;
				}
				backgroundFps = Math.min(backgroundFps, backgroundLimit);

				Globals.resolve(LwjglApplicationConfiguration.class).foregroundFPS = foregroundFps;
				Globals.resolve(LwjglApplicationConfiguration.class).backgroundFPS = backgroundFps;
			}
			catch (Exception e) {
				Console.println("Usage: maxfps <int_arg>");
			}
		}
	}

	public static class Connect extends Command {

		@Override
		public String getName() {
			return "connect";
		}

		@Override
		public void execute(String[] args) {
			String ip;
			int port = Constants.DEFAULT_PORT;
			
			try {
				if (args.length < 2) {
					throw new Exception();
				}
				
				
				String input = args[1];
				if (input.contains(":")) {
					String[] parts = input.split(":");
					ip = parts[0];
					port = Integer.parseInt(parts[2]);
				}
				else {
					ip = input;
				}
				
			}
			catch (Exception e) {
				Console.println("Usage: connect <ip:port>");
				return;
			}
			
			InfrpgGame.connect(ip, port);
		}
	}

}
