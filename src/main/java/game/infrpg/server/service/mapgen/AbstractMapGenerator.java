package game.infrpg.server.service.mapgen;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractMapGenerator implements IMapGenerator {
	
	public final String seed;

	public AbstractMapGenerator(ISeedProvider seedProvider) {
		this.seed = seedProvider.getSeed();
	}

	@Override
	public String getSeed() {
		return seed;
	}
	
}
