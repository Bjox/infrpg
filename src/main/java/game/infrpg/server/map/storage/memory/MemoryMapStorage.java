package game.infrpg.server.map.storage.memory;

import game.infrpg.server.map.storage.AbstractMapStorage;
import game.infrpg.server.map.Region;
import java.io.IOException;
import java.util.HashMap;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MemoryMapStorage extends AbstractMapStorage {

	private final HashMap<String, Region> regions;

	@Inject
	public MemoryMapStorage() {
		this.regions = new HashMap<>();
	}
	
	@Override
	public Region getRegion(int x, int y) {
		throwIfStorageIsClosed();
		return regions.get(getKey(x, y));
	}

	@Override
	public void storeRegion(Region region) {
		throwIfStorageIsClosed();
		regions.put(getKey(region), region);
	}
	
	private String getKey(Region r) {
		return getKey(r.position.getX(), r.position.getY());
	}
	
	private String getKey(int x, int y) {
		return x + "," + y;
	}
	
}
