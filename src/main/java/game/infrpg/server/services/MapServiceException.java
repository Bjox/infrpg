package game.infrpg.server.services;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapServiceException extends Exception {

	public MapServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapServiceException(String message) {
		super(message);
	}
	
}
