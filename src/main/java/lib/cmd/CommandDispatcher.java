package lib.cmd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public class CommandDispatcher<T extends CommandObject> {

	public static final char COMMAND_SEPARATOR = ' ';

	private final T cmdObject;
	private final Map<String, CommandMethod> cmdMethods;
	private final TypeParser parser;

	public CommandDispatcher(T cmdObject) {
		this.cmdObject = cmdObject;
		this.cmdMethods = new HashMap<>();
		this.parser = new TypeParser();

		Stream.of(cmdObject.getClass().getMethods())
			.filter(this::isCommandMethod)
			.map(cm -> new CommandMethod(cm, parser))
			.forEach(cm -> cmdMethods.put(cm.getName(), cm));
	}

	private boolean isCommandMethod(Method method) {
		return method.isAnnotationPresent(Command.class);
	}

	public void parse(String command) throws CommandParseException {
		if (command == null || command.equals("")) {
			return;
		}

		final char splitChar = '\u001F';

		StringBuilder cmdStr = new StringBuilder(command);
		boolean encounteredQuotationMark = false;
		for (int i = 0; i < cmdStr.length(); i++) {
			char ch = cmdStr.charAt(i);
			if (ch == '"') {
				encounteredQuotationMark = !encounteredQuotationMark;
				cmdStr.deleteCharAt(i);
				i--;
				continue;
			}
			if (ch == COMMAND_SEPARATOR && !encounteredQuotationMark) {
				cmdStr.setCharAt(i, splitChar);
			}
		}

		String[] parts = cmdStr.toString().split(String.valueOf(splitChar));
		if (parts.length == 0) {
			return;
		}

		String cmdName = parts[0];
		CommandMethod cmdMethod = cmdMethods.get(cmdName);
		if (cmdMethod == null) {
			throw new CommandParseException("Unrecognized command \"" + cmdName + "\".");
		}

		Object[] args = cmdMethod.parseArguments(parts, parser);

		try {
			cmdMethod.invoke(args, cmdObject);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CommandParseException("Cannot execute command \"" + cmdMethod.getName() + "\".", e);
		}
	}

	public TypeParser getTypeParser() {
		return parser;
	}
}
