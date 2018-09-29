package game.infrpg.client.util;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
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
				if (foregroundFps < 0) throw new Exception();
				int backgroundFps = foregroundFps;
				int foregroundLimit = Helpers.getClientForegroundFpsLimit();
				if (foregroundLimit == 0) foregroundLimit = Integer.MAX_VALUE;
				foregroundFps = Math.min(foregroundFps, foregroundLimit);
				int backgroundLimit = Helpers.getClientBackgroundFpsLimit();
				if (backgroundLimit == 0) backgroundLimit = Integer.MAX_VALUE;
				backgroundFps = Math.min(backgroundFps, backgroundLimit);
				
				Globals.resolve(LwjglApplicationConfiguration.class).foregroundFPS = foregroundFps;
				Globals.resolve(LwjglApplicationConfiguration.class).backgroundFPS = backgroundFps;
			} catch (Exception e) {
				Console.println("Usage: maxfps <int_arg>");
			}
			
		}
		
	}
	
}
