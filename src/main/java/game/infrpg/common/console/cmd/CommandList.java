package game.infrpg.common.console.cmd;

import game.infrpg.common.console.Console;
import static game.infrpg.common.console.Console.COLOR_ERROR_MSG;



public abstract class CommandList {
	
	public void registerCommands() {
		addCommands(getClass());
	}
	
	private static void addCommands(Class<? extends CommandList> list) {
		Class<?>[] subClasses = list.getDeclaredClasses();
		
		for (Class<?> c : subClasses) {
			try {	
				Command cmd = c.asSubclass(Command.class).newInstance();
//				logger.debug("Parsed command \"" + cmd.getName() + "\"", 2);
				Console.addCommand(cmd);
				
			} catch (ClassCastException e) {
				Console.println("Error parsing the command \"" + c.getSimpleName() + "\" in \"" + list.getSimpleName()
						+ "\". Inner class does not implement the Command interface.", COLOR_ERROR_MSG);
			} catch (InstantiationException e) {
				Console.println("Error parsing the command \"" + c.getSimpleName() + "\" in \"" + list.getSimpleName()
						+ "\". Inner Class is not decalred static.", COLOR_ERROR_MSG);
			} catch (IllegalAccessException e) {
				Console.println("An unknown error occurred during command parsing. " + e, COLOR_ERROR_MSG);
			}
		}
	}
	
}
