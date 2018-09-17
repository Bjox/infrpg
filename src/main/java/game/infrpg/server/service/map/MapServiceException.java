package game.infrpg.server.service.map;

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
