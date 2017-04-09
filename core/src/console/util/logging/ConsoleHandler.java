package console.util.logging;

import console.Console;
import java.awt.Color;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ConsoleHandler extends Handler {

	// [type/level] prefix message suffix (class.method:line)
	
	@Override
	public void handle(LogRecord record, Logger logger) {
		Color levelCode;
		switch (record.getLevel()) {
			case DEBUG:
				levelCode = Color.CYAN;
				break;
			case INFO:
				levelCode = Color.WHITE;
				break;
			case WARNING:
				levelCode = Color.YELLOW;
				break;
			case ERROR:
				levelCode = Color.RED;
				break;
			default:
				levelCode = Color.WHITE;
		}

		Console.print("[" + record.getLevel().name() + "] ", levelCode);
		Console.print(record.getPrefix() + " ", Color.GRAY);
		Console.print(record.getMessage() + " ", Color.WHITE);
		Console.print(record.getSuffix() + " ", Color.GRAY);
		
		if (logger.getCurrentLevel().value >= Level.DEBUG.value)
			Console.print("(" + record.getFileName() + ":" + record.getLine() + ")", Color.GRAY);
		
		Console.println("");
	}
	

	@Override
	public void close() {
	}

}
