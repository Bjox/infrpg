package game.infrpg.client.util;

import game.infrpg.client.InfrpgGame;
import game.infrpg.common.console.Console;
import game.infrpg.common.console.cmd.Command;
import game.infrpg.common.console.cmd.CommandList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ConsoleCmds extends CommandList {

	
	public static class Maxfps extends Command {

		@Override
		public String getName() {
			return "maxfps";
		}

		@Override
		public void execute(String[] args) {
			try {
				int fps = Integer.parseInt(args[1]);
				InfrpgGame.gameInstance().config.foregroundFPS = fps;
			} catch (Exception e) {
				Console.println("Usage: maxfps <int_arg>");
			}
			
		}
		
	}
	
}
