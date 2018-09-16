package game.infrpg.client.entities;

import game.engine.client.entity.SimpleEntity;
import com.badlogic.gdx.graphics.g2d.Animation;
import game.infrpg.client.InfrpgGame;
import game.engine.client.graphics.assets.Assets;
import game.engine.common.console.logging.Logger;
import game.engine.common.util.Util;
import java.util.function.Consumer;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SwieteniaTree extends SimpleEntity {
	
	private float fps = 10f;
	private final TickAccumulator tickAcc;
	
	public SwieteniaTree() {
		super(Assets.SWIETENIA_TREE);
		setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		setFps(fps);
		setOrigin(0.42f, 0.18f);
		
		tickAcc = new TickAccumulator(0.2f);
	}

	@Override
	protected void tick(float delta_t) {
//		tickAcc.tick(delta_t, new_delta -> {
//			fps *= Util.randomFloat(0.7f, 1.3f);
//			if (fps < 0) fps = 0;
//			setFps(fps);
//			Logger.getPublicLogger().debug(fps);
//		});
	}
	
	
	private static class TickAccumulator {
		
		private float acc;
		private final float target;

		public TickAccumulator(float target) {
			this.acc = 0.0f;
			this.target = target;
		}
		
		public final void tick(float delta, Consumer<Float> callback) {
			acc += delta;
			if (acc >= target) {
				acc -= target;
				callback.accept(target);
			}
		}
		
	}
	
}
