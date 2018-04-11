package game.infrpg.server;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Property {

	MAP_DIRECTORY("map"),
	;
	
	public final String defaultValue;

	private Property(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	String key() {
		return this.name().toLowerCase();
	}
	
}
