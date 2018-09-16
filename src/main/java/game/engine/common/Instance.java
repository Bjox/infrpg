package game.engine.common;

import game.engine.common.console.logging.Logger;
import game.engine.common.util.ArgumentParser;

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
