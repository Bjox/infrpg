package console.util.logging;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Handler {
	
	private boolean enabled;
	
	
	public Handler() {
		enabled = true;
	}
	
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	public boolean isEnabled() {
		return enabled;
	}
	
	
	/**
	 * Handle a log event.
	 * @param record the record to be logged.
	 * @param logger the Logger instance that requested this log.
	 */
	public abstract void handle(LogRecord record, Logger logger);
	
	
	/**
	 * Close and release all resources associated with this handler, if any.
	 */
	public abstract void close();

}
