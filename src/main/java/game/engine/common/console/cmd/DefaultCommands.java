package game.engine.common.console.cmd;

import game.engine.common.console.Console;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;


public class DefaultCommands {
	public static void addDefaultCommands(Console con) {
		Console.addCommand(new ConsoleCommands(con));
		//Console.addCommand(new Exit());
		//Console.addCommand(new Penis());
		Console.addCommand(new Help());
		Console.addCommand(new Clear());
		//Console.addCommand(new SystemCall());
		Console.addCommand(new Async());
	}
	
	public static Color stringToColor(String s) {
		switch (s.toLowerCase()) {
			case "red": return Color.red;
			case "blue": return Color.blue;
			case "green": return Color.green;
			case "magenta": return Color.magenta;
			case "cyan": return Color.cyan;
			case "yellow": return Color.yellow;
			case "gray": return Color.gray;
			case "darkgray": return Color.darkGray;
			case "black": return Color.black;
			case "white": return Color.white;
			case "orange": return Color.orange;
			case "pink": return Color.pink;
			default: return Color.BLACK;
		}
	}
}

class Help extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 1) {  // Display list of available cmds
			String[] commands = Console.getCommandNames();
			for (String s : commands) {
				Console.println(s);
			}
		}
		else {
			String help = Console.getCommandHelpString(args[1]);
			if (help == null)
				Console.println("Command \"" + args[1] + "\" not found.");
			else
				Console.println(help);
		}
	}

	@Override
	public String getHelp() {
		return "Displays available help information for a given command.\n" +
			   "Usage: \"help <command_name>\"";
	}
}

class Clear extends Command {

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public void execute(String[] args) {
		Console.clearScreen();
	}

	@Override
	public String getHelp() {
		return "Clears the console window.";
	}
	
}

class ConsoleCommands extends Command {
	private Console con;
	public ConsoleCommands(Console con) {
		this.con = con;
	}
	
	@Override
	public String getName() {
		return "console";
	}

	@Override
	public void execute(String[] args) {
		if (args.length > 1) {
			switch (args[1]) {
				case "bg":
				case "background":
					if (args.length > 2) {
						Console.setConsoleBackground(DefaultCommands.stringToColor(args[2]));
					}
					else
						Console.println("Missing argument color", Console.ERROR_MSG);
					break;
				case "fg":
				case "foreground":
					if (args.length > 2) {
						Console.setConsoleForeground(DefaultCommands.stringToColor(args[2]));
					}
					else
						Console.println("Missing argument color", Console.ERROR_MSG);
					break;
				default: break;
			}
		}
	}

	@Override
	public String getHelp() {
		return "Change console related settings.";
	}
	
}


class Exit extends Command {
	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public void execute(String[] args) {
		int code = 0;
		if (args.length > 1) {
			try { code = Integer.parseInt(args[1]);}catch(NumberFormatException e){}
		}
		System.exit(code);
	}

	@Override
	public String getHelp() {
		return "Stops the JVM by calling the \"System.exit()\" method.\n" +
				"Usage: exit <status> (optionally)";
	}
}


class Async extends Command {

	@Override
	public String getName() {
		return "async";
	}

	@Override
	public void execute(String[] args) {
		if (args.length == 1) {
			Console.println("Usage: \"async <cmd>\".");
			return;
		}
		Command cmd = Console.getCommand(args[1]);
		if (cmd != null) {
			cmd.executeAsync(Arrays.copyOfRange(args, 1, args.length));
		}
	}

	@Override
	public String getHelp() {
		return "Executes a command in an asynchronous thread.\n"
				+ "The exected command is responsible for termination of the asynchronous thread.\n"
				+ "Usage: \"async <cmd>\".";
	}
	
}


class Penis extends Command {
	@Override
	public String getName() {
		return "penis";
	}

	@Override
	public void execute(String[] args) {
		int length = 2;
		if (args.length > 1) {
			try { length = Integer.parseInt(args[1]);}catch(NumberFormatException e){}
		}
		StringBuilder sb = new StringBuilder("B");
		for (int i = 0; i < length; i++)
			sb.append('=');
		sb.append("D");
		Console.println(sb.toString(), Color.PINK);
	}

	@Override
	public String getHelp() {
		new Thread(() -> {
			try {
				Console.setInputEnabled(false);
				Thread.sleep(2000);
				Console.println("Let me help...");
				Thread.sleep(1000);
				Console.print("p");
				Thread.sleep(100);
				Console.print("e");
				Thread.sleep(200);
				Console.print("n");
				Thread.sleep(200);
				Console.print("i");
				Thread.sleep(100);
				Console.print("s");
				Thread.sleep(400);
				Console.print(" ");
				Console.print("2");
				Thread.sleep(400);
				Console.print("0");
				Thread.sleep(1000);
				Console.print("\n");
				new Penis().execute(new String[]{"penis", "20"});
				
			} catch (Exception e) {
			} finally {
				Console.setInputEnabled(true);
			}
		}).start();
		return "Really? You need help with this?";
	}
}


class SystemCall extends Command {
	@Override
	public String getName() {
		return "call";
	}

	@Override
	public void execute(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i]).append(' ');
		String s;
		try {
			Process p = Runtime.getRuntime().exec(sb.toString());
             
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
 
            while ((s = stdInput.readLine()) != null) {
                Console.println(s, Console.CMD_COLOR);
            }
             
            while ((s = stdError.readLine()) != null) {
                Console.println(s, Console.ERROR_MSG);
            }
		} catch (Exception e) {}
	}

	@Override
	public String getHelp() {
		return "Calls a system command.";
	}
}