package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IMapChunk
{
	void render(Tileset tileset, SpriteBatch batch);
	long getId();
}
