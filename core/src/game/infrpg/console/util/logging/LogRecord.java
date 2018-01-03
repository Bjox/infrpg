package game.infrpg.console.util.logging;

import java.util.Date;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class LogRecord {
	
	private final Date timestamp;
	private final String prefix;
	private final String message;
	private final String suffix;
	private final Level level;
	private final StackTraceElement stackTrace;


	public LogRecord(String prefix, String message, String suffix, Level level, StackTraceElement stackTrace) {
		this.timestamp = new Date();
		this.prefix = prefix == null ? "" : prefix;
		this.message = message == null ? "" : message;
		this.suffix = suffix == null ? "" : suffix;
		this.level = level == null ? Level.INFO : level;
		this.stackTrace = stackTrace == null ? new StackTraceElement("", "", "", -1) : stackTrace;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getMessage() {
		return message;
	}

	public String getSuffix() {
		return suffix;
	}

	public Level getLevel() {
		return level;
	}
	
	public String getClassName() {
		return stackTrace.getClassName();
	}

	public int getLine() {
		return stackTrace.getLineNumber();
	}
	
	public String getMethodName() {
		return stackTrace.getMethodName();
	}
	
	public String getFileName() {
		return stackTrace.getFileName();
	}
	
}