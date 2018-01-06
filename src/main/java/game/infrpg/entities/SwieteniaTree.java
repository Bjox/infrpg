package game.infrpg.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import game.engine.entities.SimpleEntity;
import game.engine.graphics.assets.Assets;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SwieteniaTree extends SimpleEntity {
	
	public SwieteniaTree() {
		super(Assets.SWIETENIA_TREE);
		setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		setFps(10);
		setOrigin(0.42f, 0.18f);
	}
	
}
