package game.infrpg.server.util;

import game.infrpg.common.util.Constants;
import java.io.IOException;
import lib.config.AbstractConfig;
import lib.config.ConfigClass;
import lib.config.ConfigField;
import lib.config.IConfigStore;
import lib.di.Inject;
import lib.storage.IStorage;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@ConfigClass
public class ServerConfig extends AbstractConfig {
	
	@ConfigField
	public String mapDirectory = Constants.DEFAULT_MAP_DIRECTORY;
	@ConfigField
	public int port = Constants.DEFAULT_PORT;
	@ConfigField
	public String mapSeed = "";
	
	@Inject
	public ServerConfig(IConfigStore configStore, IStorage storage) throws IOException {
		super(configStore, storage);
	}
	
}
