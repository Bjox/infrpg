package game.infrpg.server.util;

import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.memory.MemoryMapStorage;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Constants {
	
	public static final Class<? extends IMapStorage> MAP_STORAGE_TYPE = MemoryMapStorage.class;
	
	
	private Constants() {
	}
	
}
