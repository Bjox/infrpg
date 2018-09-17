package game.infrpg.common;

import game.infrpg.common.console.logging.Logger;
import lib.ArgumentParser;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Instance {
	
	public final Logger logger;
	public final ArgumentParser args;
	
	public Instance(String[] args) {
		this.args = new ArgumentParser(args);
		this.logger = Logger.getPublicLogger();
	}
	
	public abstract void start();
	
}
