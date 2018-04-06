package game.infrpg.client.logic.map;

import java.io.Closeable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapStorage extends Closeable {
	
	public Region getRegion(int x, int y);
	public boolean storeRegion(Region region);
	
}
