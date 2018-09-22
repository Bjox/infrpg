package lib.di.exceptions;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DIRegistrationException extends DIException {
	
	public DIRegistrationException(String message) {
		super(message);
	}

	public DIRegistrationException(Class<?> type, String message) {
		this("Cannot register type " + type.getName() + ": " + message);
	}
	
	public DIRegistrationException(Class<?> interfaceType, Class<?> implementationType, String message) {
		this("Cannot register type mapping " + interfaceType.getName() + " -> " + implementationType.getName() + ": " + message);
	}
	
}
