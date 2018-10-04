package game.infrpg.server.service.mapgen;

import game.infrpg.client.logic.mapold.Tiles;
import game.infrpg.server.map.Chunk;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FlatgrassGenerator extends AbstractMapGenerator {

	@Inject
	public FlatgrassGenerator(ISeedProvider seedProvider) {
		super(seedProvider);
	}

	@Override
	public Chunk generateChunk(int x, int y) {
		Chunk chunk = new Chunk(x, y);
		chunk.setTiles((i, j) -> Tiles.GRASS.dataValue);
		return chunk;
	}
	
}
