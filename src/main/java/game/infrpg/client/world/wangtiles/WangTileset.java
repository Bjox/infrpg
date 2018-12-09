package game.infrpg.client.world.wangtiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.client.world.ITileProvider;
import game.infrpg.client.world.Tiles;
import static game.infrpg.client.world.Tiles.*;
import game.infrpg.common.util.Globals;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class WangTileset implements ITileProvider
{
	public static final String[] TILESET_NAMES =
	{
		"overworld"
	};
	public static final Variation[] VARIATIONS =
	{
		// Don't reorder these
		new Variation(WATER, GRASS),
		new Variation(WATER, SAND)
	};

	private static final Map<String, WangTileset> wangTilesets = new HashMap<>();

	public static void loadWangTilesets(TextureAtlas atlas)
	{
		Globals.logger().debug("Loading wang tilesets");

		Stream.of(TILESET_NAMES).forEach(tilesetName
			-> addWangTileset(atlas, tilesetName, VARIATIONS));
	}

	private static void addWangTileset(TextureAtlas atlas, String name, Variation[] variations)
	{
		Globals.logger().debug(String.format("Adding wang tileset %s...", name));
		wangTilesets.put(name, new WangTileset(atlas, name, variations));
	}

	public static WangTileset getWangTileset(String tilesetName)
	{
		if (!wangTilesets.containsKey(tilesetName))
		{
			Globals.logger().warning("Missing tileset " + tilesetName);
		}
		return wangTilesets.get(tilesetName);
	}

	private static int findFirstMatchingVariation(int variationMask)
	{
		for (int i = 0; i < VARIATIONS.length; i++)
		{
			int testMask = VARIATIONS[i].mask;
			if ((testMask & variationMask ^ variationMask) == 0)
			{
				return i;
			}
		}
		return -1;
	}

	public static int process(Tiles c1, Tiles c2, Tiles c4, Tiles c8)
	{
		int variationMask = c1.mask | c2.mask | c4.mask | c8.mask;
		int variationIndex = findFirstMatchingVariation(variationMask);

		if (variationIndex == -1)
		{
			Globals.logger().error("Missing wang tiles for variation mask "
				+ Tiles.getTilesFromMask(variationMask).stream().map(Object::toString).collect(Collectors.joining("_")));
			return 0;
		}

		int wangIndex = 0;
		Tiles baseTile = VARIATIONS[variationIndex].getType(0);
		if (c1 != baseTile) wangIndex += 1;
		if (c2 != baseTile) wangIndex += 2;
		if (c4 != baseTile) wangIndex += 4;
		if (c8 != baseTile) wangIndex += 8;

		return ((variationIndex & 0xFF) << 8) | (wangIndex & 0xFF);
	}

	// ----------------------------------------------------------------------------------------
	
	private final WangTiles[] tiles;

	public WangTileset(TextureAtlas atlas, String tilesetName, Variation[] variations)
	{
		this.tiles = new WangTiles[variations.length];

		for (int i = 0; i < variations.length; i++)
		{
			Variation variation = variations[i];
			WangTiles wangTiles = new WangTiles(tilesetName, variation, atlas);
			tiles[i] = wangTiles;
		}
	}

	@Override
	public TextureRegion getTile(int dataValue)
	{
		int tileDataLow = dataValue & 0xFF;
		int tileDataHigh = dataValue >>> 8 & 0xFF;
		return tiles[tileDataHigh].getTile(tileDataLow);
	}

}
