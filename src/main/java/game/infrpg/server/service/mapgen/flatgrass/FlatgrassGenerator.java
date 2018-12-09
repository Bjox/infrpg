package game.infrpg.server.service.mapgen.flatgrass;

import game.infrpg.client.world.Tiles;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.service.mapgen.AbstractMapGenerator;
import game.infrpg.server.service.mapgen.ISeedProvider;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FlatgrassGenerator extends AbstractMapGenerator
{
	@Inject
	public FlatgrassGenerator(ISeedProvider seedProvider)
	{
		super(seedProvider);
	}

	@Override
	public Chunk generateChunk(int x, int y)
	{
		Chunk chunk = new Chunk(x, y);
		chunk.setTileData((i, j) -> Tiles.GRASS.dataValue);
		return chunk;
	}

}
