package game.infrpg.common.console.cmd;

import game.infrpg.common.console.Console;
import lib.cmd.CommandObject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ConsoleCommands implements CommandObject {
	
	@lib.cmd.Command
	public void clear() {
		Console.clearScreen();
	}
	
}
