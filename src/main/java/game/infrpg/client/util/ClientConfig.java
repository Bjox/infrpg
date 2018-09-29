package game.infrpg.client.util;

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
public class ClientConfig extends AbstractConfig {
	
	@ConfigField
	public int screenWidth = 1000;
	@ConfigField
	public int screenHeight = 800;
	@ConfigField
	public boolean verticalSync = false;
	
	@Inject
	public ClientConfig(IConfigStore configStore, IStorage storage) throws IOException {
		super(configStore, storage);
	}
	
}
