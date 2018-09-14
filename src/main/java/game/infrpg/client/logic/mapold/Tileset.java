package game.infrpg.client.logic.mapold;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static game.infrpg.client.InfrpgGame.logger;
import java.util.HashMap;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Tileset {
	
	/** 
	 * Available tilesets.
	 */
	public enum Tilesets {
		NORMAL,
		SHIT,
		DEV
		;

		/**
		 * Get the Tileset singleton instance.
		 * @return 
		 */
		public Tileset instance() {
			return Tileset.getTileset(this);
		}
	}
	
	/**
	 * Tile definition.
	 */
	public enum Tiles {
		GRASS(0),
		DIRT(1),
		;
		
		/** The tile index of the enum list. */
		public final byte index;
		
		/** The number of defined enum tiles. */
		public static final int AMOUNT = values().length;

		private Tiles(int index) {
			if (index > Byte.MAX_VALUE || index < Byte.MIN_VALUE) {
				throw new RuntimeException("Tile index out of range.");
			}
			this.index = (byte)index;
		}
	}
	
	/** Tileset singletons storage. */
	private static HashMap<String, Tileset> tilesets;
	
	/**
	 * Load all tilesets. Call this once.
	 * @param atlas 
	 */
	public static void loadTilesets(TextureAtlas atlas) {
		logger.debug("Loading tilesets...");
		tilesets = new HashMap<>();
		for (Tilesets tilesets_enum : Tilesets.values()) {
			String tilesetname = tilesets_enum.name().toLowerCase();
			logger.debug(String.format("Adding tileset %s...", tilesets_enum.name()));
			tilesets.put(tilesetname, new Tileset(tilesets_enum, tilesetname, atlas));
		}
	}
	
	/**
	 * Get a Tileset singleton instance.
	 * @param name
	 * @return 
	 */
	public static Tileset getTileset(String name) {
		if (tilesets == null) {
			logger.error("Tilesets has not been loaded!");
			return null;
		}
		Tileset t = tilesets.get(name);
		if (t == null) logger.warning(String.format("Missing tileset \"%s\".", name));
		return t;
	}
	
	/**
	 * Get a Tileset singleton instance.
	 * @param t
	 * @return 
	 */
	public static Tileset getTileset(Tilesets t) {
		return getTileset(t.name().toLowerCase());
	}
	
	
	/** Name of this tileset, which corresponds to the sprite directory. */
	public final String name;
	
	/** The corresponding Tilesets enum. */
	public final Tilesets enumInstance;
	
	/** Array to store the textures for this tileset. */
	private final TextureRegion[] textures;

	/**
	 * Private constructor.
	 * @param name
	 * @param atlas 
	 */
	private Tileset(Tilesets tileset, String name, TextureAtlas atlas) {
		this.name = name;
		this.enumInstance = tileset;
		this.textures = new TextureRegion[Tiles.AMOUNT];
		
		for (Tiles tile : Tiles.values()) {
			String tilename = tile.name().toLowerCase();
			TextureRegion tex = atlas.findRegion(String.format("tilesets/%s/%s", name, tilename));
			if (tex == null) logger.warning(String.format("Missing tile \"%s\" in tileset \"%s\".", tilename, name));
			textures[tile.index] = tex;
		}
	}
	
	public TextureRegion getTexture(int index) {
		return textures[index];
	}
	
	public TextureRegion getTexture(Tiles tile) {
		return textures[tile.index];
	}

	@Override
	public String toString() {
		return String.format("Tileset: %s", name);
	}
	
	
	
}
