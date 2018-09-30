package game.infrpg.common.util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum Arguments {
	/** Run application in debug mode. */
	DEBUG("Run application in debug mode."),
	/** Start a Infrpg dedicated server. */
	SERVER("Start a Infrpg dedicated server."),
	/** Starts a headless instance. Can only be used in conjunction with 'server'. */
	HEADLESS("Starts the application in headless mode. Useful when running on a system with no graphics context. This is only applicable when running a server."),
	/** Print application usage with arguments and description. */
	USAGE("Print application usage with arguments and description.")
	;
	
	public final String description;
	
	private Arguments(String description) {
		this.description = description;
	}
}
