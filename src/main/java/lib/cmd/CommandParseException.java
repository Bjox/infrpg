package lib.cmd;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandParseException extends Exception {

	public CommandParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandParseException(String message) {
		super(message);
	}
	
}
