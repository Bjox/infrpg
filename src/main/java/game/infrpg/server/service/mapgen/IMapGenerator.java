package game.infrpg.server.service.mapgen;

import game.infrpg.server.map.Chunk;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapGenerator {
	
	String getSeed();
	
	Chunk generateChunk(int x, int y);
	
}
