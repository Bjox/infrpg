package game.infrpg.client.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import game.infrpg.client.graphics.assets.Assets;

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
