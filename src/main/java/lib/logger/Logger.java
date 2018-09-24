package lib.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Logger implements ILogger {
	
	@Deprecated
	private static Logger publicLogger;

	private LoggerLevel currentLevel;
	private final ArrayList<LoggerHandler> handlers;
	
	@Deprecated
	public static Logger getPublicLogger() {
		if (publicLogger == null) {
			publicLogger = new Logger();
		}
		return publicLogger;
	}

	public Logger() {
		currentLevel = LoggerLevel.OFF;
		handlers = new ArrayList<>();
	}

	@Override
	public LoggerLevel getCurrentLevel() {
		return currentLevel;
	}

	@Override
	public void setCurrentLevel(LoggerLevel currentLevel) {
		this.currentLevel = currentLevel;
	}

	@Override
	public void addHandler(LoggerHandler handler) {
		handlers.add(handler);
	}

	public void log(LogRecord record) {
		if (currentLevel == LoggerLevel.OFF) {
			return;
		}

		if (currentLevel.value >= record.getLevel().value) {
			handlers.forEach(H -> {
				if (H.isEnabled()) {
					H.handle(record, this);
				}
			});
		}
	}

	private StackTraceElement getStackTraceElement(int stackMovement) {
		return Thread.currentThread().getStackTrace()[stackMovement];
	}

	@Override
	public void debug(Object message) {
		debug(String.valueOf(message), 1);
	}

	public void debug(String message, int stackMovement) {
		log(new LogRecord("", message, "", LoggerLevel.DEBUG, getStackTraceElement(stackMovement + 3)));
	}

	@Override
	public void info(Object message) {
		info(String.valueOf(message), 1);
	}

	public void info(String message, int stackMovement) {
		log(new LogRecord("", message, "", LoggerLevel.INFO, getStackTraceElement(stackMovement + 3)));
	}

	@Override
	public void warning(Object message) {
		warning(String.valueOf(message), 1);
	}

	public void warning(String message, int stackMovement) {
		log(new LogRecord("", message, "", LoggerLevel.WARNING, getStackTraceElement(stackMovement + 3)));
	}

	@Override
	public void error(Object message) {
		error(String.valueOf(message), 1);
	}

	public void error(String message, int stackMovement) {
		log(new LogRecord("", message, "", LoggerLevel.ERROR, getStackTraceElement(stackMovement + 3)));
	}

	@Override
	public void logException(Throwable throwable) {
		if (currentLevel.check(LoggerLevel.DEBUG)) {
			try (
					StringWriter stringWriter = new StringWriter();
					PrintWriter printWriter = new PrintWriter(stringWriter)) {
				throwable.printStackTrace(printWriter);
				printWriter.flush();
				error(stringWriter.toString(), 1);
			}
			catch (IOException ex) {
				error("An exception occurred:");
				error(ex);
				error("while tracking exception:");
				error(throwable);
			}
		}
		else {
			error(throwable.toString());
		}
	}

}
