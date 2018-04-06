package game.infrpg.common;

import game.infrpg.common.console.util.logging.Logger;
import game.infrpg.common.util.ArgumentParser;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Instance {
	
	public final Logger logger;
	public final ArgumentParser args;
	
	public Instance(ArgumentParser args) {
		this.args = args;
		this.logger = Logger.getPublicLogger();
	}
	
	public abstract void start();
	
}
