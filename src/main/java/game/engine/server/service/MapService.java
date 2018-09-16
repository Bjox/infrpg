package game.engine.server.service;

import game.engine.common.console.logging.Logger;
import game.engine.server.map.IMapStorage;
import game.engine.server.map.sqlite.SQLiteMapStorage;
import java.io.File;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapService extends Service {

	private final IMapStorage storage;
	private final File mapdir;
	private final Logger logger;
	
	public MapService(File mapdir) throws MapServiceException {
		this.logger = Logger.getPublicLogger();
		
		if (mapdir.isFile()) {
			throw new MapServiceException("The supplied map directory is pointing to a file.");
		}
		
		this.mapdir = mapdir;
		
		if (!mapdir.exists()) {
			mapdir.mkdirs();
		}
		
		try {
			logger.debug("Creating map storage");
			this.storage = new SQLiteMapStorage(new File(mapdir, "map.db"));
		}
		catch (Exception ex) {
			throw new MapServiceException("An exception occurred while creating the map storage.", ex);
		}
	}
	
	
	
}
