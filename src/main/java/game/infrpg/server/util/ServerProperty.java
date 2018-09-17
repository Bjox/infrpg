package game.infrpg.server.util;

import game.infrpg.client.util.Constants;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum ServerProperty {

	MAP_DIRECTORY("map"),
	PORT(Constants.DEFAULT_PORT),
	;
	
	public final String defaultValue;

	private ServerProperty(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	private ServerProperty(int defaultValue) {
		this.defaultValue = String.valueOf(defaultValue);
	}

	String key() {
		return this.name().toLowerCase();
	}
	
}
