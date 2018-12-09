package game.infrpg.server.map;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@FunctionalInterface
public interface TileDataSupplier
{
	public int supplyTileData(int x, int y);
}
