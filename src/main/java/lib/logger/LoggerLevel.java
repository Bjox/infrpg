package lib.logger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum LoggerLevel {
	
	/** Specifies that all messages should be logged (INF). */
	ALL(Integer.MAX_VALUE),
	/** Detailed message, useful when debugging (400). */
	DEBUG(400),
	/** Confirmation and other useful messages (300). */
	INFO(300),
	/** Warning message that indicates that something unexpected happened (200). */
	WARNING(200),
	/** An error messages that indicates a serious problem that will halt the application. (100) */
	ERROR(100),
	/** Turns off all logging (0). */
	OFF(0),
	;
	
	
	/** The default logging level (INFO). */
	public static final LoggerLevel DEFAULT = INFO;
	public final int value;
	
	private LoggerLevel(int value) {
		this.value = value;
	}
	
	public boolean check(LoggerLevel level) {
		return this.value >= level.value;
	}
}
