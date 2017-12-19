package game.engine.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import game.engine.entities.Entity;
import game.infrpg.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Camera extends OrthographicCamera {
	
	/** The entity to follow. */
	private Entity lookAtEnt;
	/** Translational offset. */
	public float offset_x, offset_y;
	
	public Camera(float viewportWidth, float viewportHeight) {
		super(viewportWidth, viewportHeight);
	}
	
	
	public void lookAt(Entity ent) {
		this.lookAtEnt = ent;
	}
	

	@Override
	public void update(boolean updateFrustum) {
		if (lookAtEnt != null) {
			position.x = lookAtEnt.getScreenX() + offset_x;
			position.y = lookAtEnt.getScreenY() + offset_y;
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
	 * Put the isometric camera position in the specified vector.
	 * @param vec 
	 */
	public void getIsometricPosition(Vector2 vec) {
		vec.x = position.x;
		vec.y = position.y;
		Util.cart2iso(vec);
	}
	
}
