package game.infrpg.client.rendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.client.entity.Entity;
import game.infrpg.common.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Camera extends OrthographicCamera {
	
	/** The entity to follow. */
	private Entity followEnt;
	/** Translational offset. */
	public float offset_x, offset_y;
	
	public Camera(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);
	}
	
	
	public void follow(Entity ent) {
		this.followEnt = ent;
	}
	
	
	public void moveTo(Entity ent) {
		position.x = ent.getScreenX() + offset_x;
		position.y = ent.getScreenY() + offset_y;
	}
	

	@Override
	public void update(boolean updateFrustum) {
		if (followEnt != null) {
			moveTo(followEnt);
		}
		super.update(updateFrustum);
	}
	
	/**
	 * Returns a new vector containing the isometric camera position.
	 * @return 
	 */
	public Vector2 getIsometricPosition() {
		Vector2 v = new Vector2(position.x, position.y);
		Util.cart2iso(v);
		return v;
	}
	
	
	/**
	 * Put the isometric camera position in the provided vector.
	 * @param vec 
	 */
	public void getIsometricPosition(Vector2 vec) {
		vec.x = position.x;
		vec.y = position.y;
		Util.cart2iso(vec);
	}
	
}
