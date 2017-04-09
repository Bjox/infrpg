package game.infrpg;

import console.Console;
import console.cmd.Command;
import console.cmd.CommandList;

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
				MyGdxGame.instance.config.foregroundFPS = fps;
			} catch (Exception e) {
				Console.println("Usage: maxfps <int_arg>");
			}
			
		}
		
	}
	
}
