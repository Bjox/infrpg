package game.infrpg.server.map.memory;

import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.Region;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MemoryMapStorage implements IMapStorage {

	private final HashMap<String, Region> regions;

	public MemoryMapStorage() {
		this.regions = new HashMap<>();
	}
	
	@Override
	public Region getRegion(int x, int y) throws Exception {
		return regions.get(getKey(x, y));
	}

	@Override
	public void storeRegion(Region region) throws Exception {
		regions.put(getKey(region), region);
	}

	@Override
	public void close() throws IOException {
	}
	
	private String getKey(Region r) {
		return getKey(r.position.getX(), r.position.getY());
	}
	
	private String getKey(int x, int y) {
		return x + "," + y;
	}
	
}
