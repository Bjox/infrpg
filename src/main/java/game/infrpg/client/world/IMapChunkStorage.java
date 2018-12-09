package game.infrpg.client.world;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapChunkStorage
{
	IMapChunk getIMapChunk(int x, int y);
	
	void storeMapChunk(IMapChunk chunk);
	
	boolean containsChunk(int x, int y);
	
	int getCount();
	
	default MapChunk getMapChunk(int x, int y)
	{
		IMapChunk chunk = getIMapChunk(x, y);
		if (chunk == null || !(chunk instanceof MapChunk))
		{
			return null;
		}
		return (MapChunk)chunk;
	}
	
}
