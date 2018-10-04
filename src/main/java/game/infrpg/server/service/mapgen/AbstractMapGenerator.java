package game.infrpg.server.service.mapgen;

import game.infrpg.server.service.Service;
import java.io.IOException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractMapGenerator extends Service implements IMapGenerator {
	
	public final String seed;

	public AbstractMapGenerator(ISeedProvider seedProvider) {
		this.seed = seedProvider.getSeed();
	}

	@Override
	public String getSeed() {
		return seed;
	}

	@Override
	public void close() throws IOException {
	}
	
}
