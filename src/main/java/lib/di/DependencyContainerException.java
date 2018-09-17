package lib.di;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DependencyContainerException extends RuntimeException {

	public DependencyContainerException() {
	}
	
	public DependencyContainerException(String message) {
		super(message);
	}
	
	public DependencyContainerException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
