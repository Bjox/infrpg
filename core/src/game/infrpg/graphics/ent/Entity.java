package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Entity {
	
	/** Position on the isometric map. */
	public float x, y;

	public Entity() {
	}
	
//	private float getScreenX() {
//		return x - y;
//	}
//	
//	private float getScreenY() {
//		return (x + y) * 0.5f;
//	}
	
	public final void render(SpriteBatch batch, float t) {
		float screenx = x - y;
		float screeny = (x + y) * 0.5f;
		render(batch, t, screenx, screeny);
	}
	
	protected abstract void render(SpriteBatch batch, float t, float screenx, float screeny);
	
}
