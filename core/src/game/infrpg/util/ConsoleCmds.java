package game.infrpg.util;

import game.infrpg.Infrpg;
import game.infrpg.console.Console;
import game.infrpg.console.cmd.Command;
import game.infrpg.console.cmd.CommandList;

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
				Infrpg.gameInstance().config.foregroundFPS = fps;
			} catch (Exception e) {
				Console.println("Usage: maxfps <int_arg>");
			}
			
		}
		
	}
	
}
