package game.infrpg.server.service.map;

import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.memory.MemoryMapStorage;
import game.infrpg.server.map.sqlite.SQLiteMapStorage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class MapStorageFactory {

	
	
	private MapStorageFactory() {
	}
	
	public static SQLiteMapStorage createSQLiteMapStorage(File mapdir) throws IOException, SQLException {
		return new SQLiteMapStorage(new File(mapdir, "map.db"));
	}
	
	public static IMapStorage createMemoryMapStorage() {
		return new MemoryMapStorage();
	}
	
}
