package game.infrpg.client.world.wangtiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.client.world.ITileProvider;
import game.infrpg.client.world.Tiles;
import game.infrpg.common.util.Globals;

/**
 * http://www.cr31.co.uk/stagecast/wang/2corn.html
 *
 * @author Bjørnar W. Alvestad
 */
public class WangTiles implements ITileProvider
{
	/*
	Contains:
	0
	0, 15
	0, 40, 80
	0, 85, 170, 255
	*/
	private static final int[][] BASE_TILE_LOOKUP;
	
	static
	{
		BASE_TILE_LOOKUP = new int[4][];	
		BASE_TILE_LOOKUP[0] = new int[] { 0 };
		
		for (int i = 1; i <= 3; i++)
		{
			BASE_TILE_LOOKUP[i] = new int[i+1];
			int base = Integer.parseInt("1111", i+1);
			for (int j = 0; j <= i; j++)
			{
				BASE_TILE_LOOKUP[i][j] = base * j;
			}
		}
	}
	
	private final TextureRegion[] tiles;
	public final Variation variation;

	/**
	 * /tilesets/wang/tilesetName/variationT1_variationT2_x
	 *
	 * @param tilesetName
	 * @param variation
	 * @param atlas
	 */
	public WangTiles(String tilesetName, Variation variation, TextureAtlas atlas)
	{
		int permutations = variation.length * variation.length * variation.length * variation.length;
		this.tiles = new TextureRegion[permutations];
		this.variation = variation;

		String tilesetPath = String.format("tilesets/wang/%s", tilesetName);
		for (int i = 0; i < permutations; i++)
		{
			TextureRegion tex;
			Tiles baseTile = isBaseTileIndex(i, variation);
			if (baseTile != null)
			{
				tex = atlas.findRegion(String.format("%s/%s", tilesetPath, baseTile.name().toLowerCase()));
			}
			else
			{
				tex = atlas.findRegion(String.format("%s/%s", tilesetPath, variation.toString()), i);
			}
			
			if (tex == null) Globals.logger().warning(
					String.format("Missing wang tile index %d for tileset %s", i, tilesetName));
			
			tiles[i] = tex;
		}
	}
	
	/**
	 * Returns the appropriate base tile if the provided index pertains to
	 * a base index in a specific variation. Otherwise Returns null if
	 * the index is not a base index.
	 * @param i
	 * @param variation
	 * @return 
	 */
	private static Tiles isBaseTileIndex(int i, Variation variation)
	{
		if (i == 0) // 0 is always a base tile
		{
			return variation.getType(0);
		}
		int[] baseSeries = BASE_TILE_LOOKUP[variation.length - 1];
		for (int j = 1; j < baseSeries.length; j++)
		{
			int baseValue = baseSeries[j];
			if (i == baseValue)
			{
				return variation.getType(j);
			}
		}
		return null;
	}

	@Override
	public TextureRegion getTile(int wangIndex)
	{
		return tiles[wangIndex];
	}

}
