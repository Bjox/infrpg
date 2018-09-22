package lib.di.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DIException extends RuntimeException {

	public DIException() {
	}
	
	public DIException(String message) {
		super(message);
	}
	
	public DIException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
