package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapChunk
{
	void render(ITileProvider tileProvider, SpriteBatch batch);
	long getId();
}
