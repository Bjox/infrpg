package game.engine.server.map;

import java.io.Closeable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapStorage extends Closeable {
	
	public Region getRegion(int x, int y) throws Exception;
	public void storeRegion(Region region) throws Exception;
	
}
