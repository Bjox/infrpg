package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.server.map.AbstractChunk;
import java.time.Duration;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class RequestedMapChunk extends AbstractChunk implements IMapChunk
{
	public final long timestamp;

	public RequestedMapChunk(int x, int y)
	{
		super(x, y);
		this.timestamp = now();
	}

	public Duration getTimeSinceRequested()
	{
		return Duration.ofNanos(now() - timestamp);
	}

	private long now()
	{
		return System.nanoTime();
	}

	@Override
	public void render(Tileset tileset, SpriteBatch batch)
	{
	}
	
}
