package game.engine.common.console.cmd;


public abstract class Command {
	
	/**
	 * The name of the command. 
	 * Typing this name in the console will trigger the exec method
	 * of the command.
	 * 
	 * @return Command name
	 */
	public abstract String getName();
	
	
	/**
	 * Execute this command.
	 * 
	 * Console calls this method when invoking a command.
	 * Argument list INCLUDES the command name, meaning args[0] is the
	 * command name and args[1] the first argument in the list.
	 * 
	 * This method will block until return. 
	 * Use <code>executeAsync()</code> on time consuming calls in order to not freeze the console.
	 * 
	 * @param args Argument list
	 */
	public abstract void execute(String[] args);
	
	
	/**
	 * Execute this command in an asynchronous thread.
	 * 
	 * The command is itself responsible for terminating.
	 * 
	 * @param args 
	 */
	public void executeAsync(String[] args) {
		new Thread(() -> execute(args)).start();
	}
	
	
	/**
	 * Returns the help text for this command.
	 * @return 
	 */
	public String getHelp() {
		return "No help information is specified for this command."; // Default message
	}
	
}
