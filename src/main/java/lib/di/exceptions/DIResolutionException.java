package lib.di.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DIResolutionException extends DIException {

	public DIResolutionException(String message) {
		super(message);
	}
	
	public DIResolutionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DIResolutionException(Class<?> type, String message) {
		this("\nCannot resolve type " + type.getName() + ": " + message);
	}
	
}
