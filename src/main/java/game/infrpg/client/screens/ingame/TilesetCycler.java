package game.infrpg.client.screens.ingame;

import game.infrpg.client.world.ClientMapService;
import game.infrpg.client.world.Tileset.Tilesets;
import java.util.Arrays;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class TilesetCycler {
	
	private int currentIndex;
	private final int numberOfTilesets;
	
	public TilesetCycler(ClientMapService map) {
		this.numberOfTilesets = Tilesets.values().length;
		currentIndex = indexOfTilesetEnum(map.getTileset());
	}
	
	public Tilesets getNextTileset() {
		currentIndex++;
		if (currentIndex >= numberOfTilesets) currentIndex = 0;
		return Tilesets.values()[currentIndex];
	}
	
	public Tilesets getPreviousTileset() {
		currentIndex--;
		if (currentIndex < 0) currentIndex = numberOfTilesets - 1;
		return Tilesets.values()[currentIndex];
	}
	
	private int indexOfTilesetEnum(Tilesets tileset) {
		return Arrays.asList(Tilesets.values()).indexOf(tileset);
	}
	
}
