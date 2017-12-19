package game.infrpg.console.util.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FileHandler extends Handler {
	
	private final PrintWriter writer;
	
	
	public FileHandler(String filename) throws IOException {
		writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)), false);
	}
	
	
	@Override
	public void handle(LogRecord record, Logger logger) {
		
		writer.printf("[%s] %s %s %s", record.getLevel().name(), record.getPrefix(), record.getMessage(), record.getSuffix());
		
		if (logger.getCurrentLevel().value >= Level.DEBUG.value) {
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
