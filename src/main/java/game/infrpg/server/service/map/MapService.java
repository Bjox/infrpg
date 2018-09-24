package game.infrpg.server.service.map;

import game.infrpg.Globals;
import lib.logger.Logger;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.memory.MemoryMapStorage;
import game.infrpg.server.map.sqlite.SQLiteMapStorage;
import game.infrpg.server.service.Service;
import game.infrpg.server.util.Constants;
import java.io.File;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapService extends Service {

	private final IMapStorage storage;
	private final File mapdir;
	private final ILogger logger;
	
	public MapService(File mapdir) throws MapServiceException {
		this.logger = Globals.logger();
		
		if (mapdir.isFile()) {
			throw new MapServiceException("The supplied map directory is pointing to a file.");
		}
		
		this.mapdir = mapdir;
		
		if (!mapdir.exists()) {
			mapdir.mkdirs();
		}
		
		try {
			logger.debug("Creating map storage");
			
			if (Constants.MAP_STORAGE_TYPE == SQLiteMapStorage.class) {
				this.storage = MapStorageFactory.createSQLiteMapStorage(this.mapdir);
			}
			else if (Constants.MAP_STORAGE_TYPE == MemoryMapStorage.class) {
				this.storage = MapStorageFactory.createMemoryMapStorage();
			}
			else {
				throw new Exception("Invalid map storage type: " + String.valueOf(Constants.MAP_STORAGE_TYPE));
			}
			
		}
		catch (Exception ex) {
			throw new MapServiceException("An exception occurred while creating the map service.", ex);
		}
	}
	
	
	
}
