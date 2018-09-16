package game.engine.common.console.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Logger {

	private static Logger publicLogger;

	private Level currentLevel;
	private final ArrayList<Handler> handlers;

	public static Logger getPublicLogger() {
		if (publicLogger == null) {
			publicLogger = new Logger();
		}
		return publicLogger;
	}

	public Logger() {
		currentLevel = Level.OFF;
		handlers = new ArrayList<>();
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void addHandler(Handler handler) {
		handlers.add(handler);
	}

	public void log(LogRecord record) {
		if (currentLevel == Level.OFF) {
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

	public void debug(String message) {
		debug(message, 1);
	}

	public void debug(Object o) {
		debug(String.valueOf(o));
	}

	public void debug(String message, int stackMovement) {
		log(new LogRecord("", message, "", Level.DEBUG, getStackTraceElement(stackMovement + 3)));
	}

	public void info(String message) {
		info(message, 1);
	}

	public void info(Object o) {
		info(String.valueOf(o));
	}

	public void info(String message, int stackMovement) {
		log(new LogRecord("", message, "", Level.INFO, getStackTraceElement(stackMovement + 3)));
	}

	public void warning(String message) {
		warning(message, 1);
	}

	public void warning(Object o) {
		warning(String.valueOf(o));
	}

	public void warning(String message, int stackMovement) {
		log(new LogRecord("", message, "", Level.WARNING, getStackTraceElement(stackMovement + 3)));
	}

	public void error(String message) {
		error(message, 1);
	}

	public void error(Object o) {
		error(String.valueOf(o));
	}

	public void error(String message, int stackMovement) {
		log(new LogRecord("", message, "", Level.ERROR, getStackTraceElement(stackMovement + 3)));
	}

	public void trackException(Throwable throwable) {
		if (currentLevel.check(Level.DEBUG)) {
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
