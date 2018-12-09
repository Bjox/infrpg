package game.infrpg.server.service.mapgen.terrainv1;

import game.infrpg.client.world.Tiles;
import game.infrpg.common.util.Constants;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.service.mapgen.AbstractMapGenerator;
import game.infrpg.server.service.mapgen.ISeedProvider;
import lib.di.Inject;
import lib.logger.ILogger;
import lib.util.Util;
import lib.util.noise.FractalBrownianMotion;
import lib.util.noise.OpenSimplexNoise;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class TerrainV1Generator extends AbstractMapGenerator
{
	private final ILogger logger;
	private final OpenSimplexNoise simplexNoise;
	private final FractalBrownianMotion fbm;
	
	private final double waterLevel = 50;
	
	@Inject
	public TerrainV1Generator(ISeedProvider seedProvider, ILogger logger)
	{
		super(seedProvider);
		this.logger = logger;
		this.simplexNoise = new OpenSimplexNoise(getSeed().hashCode());
		this.fbm = new FractalBrownianMotion(8, 0.5, 0.015, simplexNoise);
		this.logger.debug("Terrain provided by TerrainV1");
		this.logger.debug("Map seed: " + getSeed());
	}
	
	@Override
	public Chunk generateChunk(int x, int y)
	{
		return new Chunk(x, y).setTileData((local_x, local_y) -> {
			int global_x = x * Constants.CHUNK_SIZE + local_x;
			int global_y = y * Constants.CHUNK_SIZE + local_y;
			return determineTile(global_x, global_y);
		});
	}
	
	private byte determineTile(int x, int y)
	{
		double noiseValue = fbm.eval(x, y);
		double height = Util.mapToRange(noiseValue, -1, 1, 0, 100);
		return heightFilter(height).dataValue;
	}
	
	private Tiles heightFilter(double height)
	{
		if (height < waterLevel) return Tiles.WATER;
		//if (height < waterLevel * 1.2) return Tiles.SAND;
		else return Tiles.GRASS;
	}
}
