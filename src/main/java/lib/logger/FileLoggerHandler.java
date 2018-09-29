package lib.logger;

import game.infrpg.common.util.Globals;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FileLoggerHandler extends LoggerHandler {

	private final PrintWriter writer;

	public FileLoggerHandler(String filename) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)), false);
	}

	@Override
	public void handle(LogRecord record, ILogger logger) {
		SimpleDateFormat dateFormat = Globals.resolve(SimpleDateFormat.class);
		
		writer.printf("%s  %-7s %s %s %s",
				dateFormat.format(record.getTimestamp()), record.getLevel().name(), record.getPrefix(), record.getMessage(), record.getSuffix());

		if (logger.getCurrentLevel().check(LoggerLevel.DEBUG)) {
			writer.printf(" (%s:%d)", record.getFileName(), record.getLine());
		}

		writer.println();
		writer.flush();
	}

	@Override
	public void close() {
		writer.flush();
		writer.close();
	}

}
