package lib.cmd;

import game.infrpg.common.util.Helpers;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Stream;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandDispatcher
{

	public static final char COMMAND_SEPARATOR = ' ';

	private final CommandObject cmdObject;
	private final Map<String, CommandMethod> cmdMethods;
	private final TypeParser parser;

	@Inject
	public CommandDispatcher(CommandObject cmdObject)
	{
		this.cmdObject = cmdObject;
		this.cmdMethods = new HashMap<>();
		this.parser = new TypeParser();

		Stream.of(cmdObject.getClass().getMethods())
			.filter(this::isCommandMethod)
			.map(cm -> new CommandMethod(cm, parser))
			.forEach(cm -> cmdMethods.put(cm.getName().toLowerCase(), cm));
	}

	private boolean isCommandMethod(Method method)
	{
		return method.isAnnotationPresent(Command.class);
	}

	public CompletableFuture<Void> parse(String command)
	{
		if (command == null || command.equals(""))
		{
			return CompletableFuture.completedFuture(null);
		}

		String[] parts = split(command);

		if (parts.length == 0)
		{
			return CompletableFuture.completedFuture(null);
		}

		String cmdName = parts[0];

		if (cmdName.equalsIgnoreCase("help"))
		{
			String helpArg = parts.length > 1 ? parts[1] : null;
			helpCommand(helpArg);
			return CompletableFuture.completedFuture(null);
		}

		CommandMethod cmdMethod = cmdMethods.get(cmdName.toLowerCase());
		
		return CompletableFuture.runAsync(() ->
		{
			try
			{
				if (cmdMethod == null) throw new CommandParseException("Unrecognized command \"" + cmdName + "\".");
				try
				{
					Object[] args = cmdMethod.parseArguments(parts, parser);
					cmdMethod.invoke(args, cmdObject);
				}
				catch (CommandParseException e)
				{
					throw new CommandParseException("Command \"" + cmdMethod.getName() + "\": " + e.getMessage(), e);
				}
				catch (IllegalAccessException | IllegalArgumentException e)
				{
					throw new CommandParseException("Cannot execute command \"" + cmdMethod.getName() + "\".", e);
				}
				catch (InvocationTargetException e)
				{
					throw new CommandParseException("An exception occurred while executing command \"" + cmdMethod.getName() + "\".", e.getCause());
				}
			}
			catch (CommandParseException e)
			{
				throw Helpers.wrapInRuntimeException(e);
			}
		});
	}

	private String[] split(String str)
	{
		final char splitChar = '\u001F';

		StringBuilder cmdStr = new StringBuilder(str);
		boolean encounteredQuotationMark = false;

		for (int i = 0; i < cmdStr.length(); i++)
		{
			char ch = cmdStr.charAt(i);
			if (ch == '"')
			{
				encounteredQuotationMark = !encounteredQuotationMark;
				cmdStr.deleteCharAt(i);
				i--;
				continue;
			}
			if (ch == COMMAND_SEPARATOR && !encounteredQuotationMark)
			{
				cmdStr.setCharAt(i, splitChar);
			}
		}

		return cmdStr.toString().split(String.valueOf(splitChar));
	}

	public TypeParser getTypeParser()
	{
		return parser;
	}

	public String[] getCommandList()
	{
		return cmdMethods.values().stream()
			.map(cmd -> cmd.toString())
			.toArray(String[]::new);
	}

	@Override
	public String toString()
	{
		return String.join("\n", getCommandList());
	}

	private void helpCommand(String arg)
	{
		if (arg == null)
		{
			System.out.println("Displays available help information for a given command.\nUsage: \"help <command_name>\"");
			System.out.println("Available commands:");
			System.out.println(toString());
			return;
		}
		if (!cmdMethods.containsKey(arg.toLowerCase()))
		{
			System.out.println("Unrecognized command \"" + arg + "\".");
			return;
		}
		System.out.println(cmdMethods.get(arg.toLowerCase()).getDescription());
	}

}
