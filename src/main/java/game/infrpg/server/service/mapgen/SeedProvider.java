package game.infrpg.server.service.mapgen;

import game.infrpg.common.util.Util;
import game.infrpg.server.util.ServerConfig;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SeedProvider implements ISeedProvider {
	
	private final ServerConfig config;
	
	@Inject
	public SeedProvider(ServerConfig config) {
		this.config = config;
	}
	
	@Override
	public String getSeed() {
		if (config.mapSeed.equals("")) {
			return String.valueOf(Util.randomLong());
		}
		return config.mapSeed;
	}
	
}
