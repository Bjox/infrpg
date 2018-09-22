package lib.logger;

import java.io.PrintStream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PrintStreamLoggerHandler extends LoggerHandler {
	
	private final PrintStream stream;
	private final boolean autoCloseStream;
	
	public PrintStreamLoggerHandler(PrintStream stream, boolean autoCloseStream) {
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
