package game.infrpg.server.service.mapgen;

import game.infrpg.server.service.Service;
import java.io.IOException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractMapGenerator extends Service implements IMapGenerator {
	
	private final String seed;

	public AbstractMapGenerator(ISeedProvider seedProvider) {
		this.seed = seedProvider.getSeed();
	}

	@Override
	public final String getSeed() {
		return seed;
	}

	@Override
	public void close() throws IOException {
	}
	
}
