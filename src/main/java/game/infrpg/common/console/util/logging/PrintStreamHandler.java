package game.infrpg.common.console.util.logging;

import java.io.PrintStream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PrintStreamHandler extends Handler {
	
	private final PrintStream stream;
	private final boolean autoCloseStream;
	
	public PrintStreamHandler(PrintStream stream, boolean autoCloseStream) {
		this.stream = stream;
		this.autoCloseStream = autoCloseStream;
	}

	@Override
	public void handle(LogRecord record, Logger logger) {
		this.stream.println(record.toString());
	}

	@Override
	public void close() {
		if (autoCloseStream)
			stream.close();
	}
	
}
