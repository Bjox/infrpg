package game.infrpg.server.map;

import java.io.Closeable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapStorage extends Closeable {
	
	void init() throws Exception;
	
	Region getRegion(int x, int y);
	
	void storeRegion(Region region);
	
	boolean isClosed();
	
}
