package game.infrpg.server.service.map;

import game.infrpg.server.map.Chunk;
import java.io.Closeable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapService extends Closeable {
	
	void init() throws Exception;
	
	Chunk getChunk(int x, int y);
	
}
