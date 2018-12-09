package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.common.util.Globals;
import java.util.HashMap;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Tileset implements ITileProvider
{
	/** Tileset singletons storage. */
	private static HashMap<String, Tileset> tilesets;

	/**
	 * Load all tilesets. Call this once.
	 *
	 * @param atlas
	 */
	public static void loadTilesets(TextureAtlas atlas)
	{
		Globals.logger().debug("Loading tilesets...");
		tilesets = new HashMap<>();
		for (Tilesets tilesets_enum : Tilesets.values())
		{
			String tilesetname = tilesets_enum.name().toLowerCase();
			Globals.logger().debug(String.format("Adding tileset %s...", tilesets_enum.name()));
			tilesets.put(tilesetname, new Tileset(tilesets_enum, tilesetname, atlas));
		}
	}

	/**
	 * Get a Tileset singleton instance.
	 *
	 * @param name
	 * @return
	 */
	public static Tileset getTileset(String name)
	{
		if (tilesets == null)
		{
			Globals.logger().error("Tilesets has not been loaded!");
			return null;
		}
		Tileset t = tilesets.get(name);
		if (t == null) Globals.logger().warning(String.format("Missing tileset \"%s\".", name));
		return t;
	}

	/**
	 * Get a Tileset singleton instance.
	 *
	 * @param t
	 * @return
	 */
	public static Tileset getTileset(Tilesets t)
	{
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
	 *
	 * @param name
	 * @param atlas
	 */
	private Tileset(Tilesets tileset, String name, TextureAtlas atlas)
	{
		this.name = name;
		this.enumInstance = tileset;
		this.textures = new TextureRegion[Tiles.LENGTH];

		for (Tiles tile : Tiles.values())
		{
			String tilename = tile.name().toLowerCase();
			TextureRegion tex = atlas.findRegion(String.format("tilesets/plain/%s/%s", name, tilename));
			if (tex == null) Globals.logger().warning(
					String.format("Missing tile \"%s\" in tileset \"%s\".", tilename, name));
			textures[tile.dataValue] = tex;
		}
	}
	
	@Override
	public TextureRegion getTile(int dataValue)
	{
		return textures[dataValue];
	}

	public TextureRegion getTexture(Tiles tile)
	{
		return textures[tile.dataValue];
	}

	@Override
	public String toString()
	{
		return String.format("Tileset: %s", name);
	}

	/**
	 * Available tilesets.
	 */
	public enum Tilesets
	{
		NORMAL,
		SHIT,
		DEV;

		/**
		 * Get the Tileset singleton instance.
		 *
		 * @return
		 */
		public Tileset instance()
		{
			return Tileset.getTileset(this);
		}
	}

}
