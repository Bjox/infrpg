package game.infrpg.server;

import game.infrpg.client.logic.Constants;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Property {

	MAP_DIRECTORY("map"),
	PORT(Constants.DEFAULT_PORT),
	;
	
	public final String defaultValue;

	private Property(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	private Property(int defaultValue) {
		this.defaultValue = String.valueOf(defaultValue);
	}

	String key() {
		return this.name().toLowerCase();
	}
	
}
