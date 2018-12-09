package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface ITileProvider
{
	TextureRegion getTile(int tileRenderValue);
}
