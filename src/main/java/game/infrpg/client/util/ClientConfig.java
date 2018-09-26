package game.infrpg.client.util;

import java.io.IOException;
import lib.config.AbstractConfig;
import lib.config.ConfigClass;
import lib.config.IConfigStore;
import lib.storage.IStorage;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@ConfigClass
public class ClientConfig extends AbstractConfig {
	
	
	
	public ClientConfig(IConfigStore configStore, IStorage storage) throws IOException {
		super(configStore, storage);
	}
	
}
