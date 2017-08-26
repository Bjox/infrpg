package console.util.logging;

import java.util.ArrayList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Logger {
	
	private Level currentLevel;
	private final ArrayList<Handler> handlers;
	
	
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
		if (currentLevel == Level.OFF)
			return;
		
		if (currentLevel.value >= record.getLevel().value) {
			handlers.forEach(H -> {
				if (H.isEnabled()) H.handle(record, this);
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
	
}
