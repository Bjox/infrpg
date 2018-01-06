package game.engine.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import game.engine.graphics.assets.GraphicsAsset;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SimpleEntity extends Entity {

	/**
	 * 
	 * @param g 
	 */
	public SimpleEntity(GraphicsAsset g) {
		setRenderStateRenderable(RS_DEFAULT, g.loadRenderable());
	}
	
	/**
	 * Set the framerate for all animations in this entity.
	 * @param fps 
	 */
	public final void setFps(float fps) {
		getBaseRenderStateMap().forEachAnimation(a -> a.setFps(fps));
	}
	
	/**
	 * Set the play mode for all animations in this entity.
	 * @param mode 
	 */
	public final void setPlayMode(Animation.PlayMode mode) {
		getBaseRenderStateMap().forEachAnimation(a -> a.setPlayMode(mode));
	}
}
