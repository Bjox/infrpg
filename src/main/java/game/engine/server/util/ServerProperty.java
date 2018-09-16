package game.engine.server.util;

import game.engine.client.logic.Constants;

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
