package lib.cmd;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandException extends RuntimeException {

	public CommandException(String message) {
		super(message);
	}

	public CommandException() {
	}
	
}
