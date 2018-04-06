package game.infrpg.client.entities;

//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.math.Affine2;
//import game.infrpg.graphics.assets.GraphicsAssetLoader;
//import static game.infrpg.Infrpg.elapsedTime;
//import game.infrpg.graphics.ent.Sprite;
//import game.infrpg.util.Util;
//
///**
// *
// * @author Bjørnar W. Alvestad
// */
//public class Grass extends Sprite {
//	
//	public float swayOffset;
//	public final float amplitude;
//	public final float speed;
//	
//	public Grass() {
//		super(GraphicsAssetLoader.loadSprite("grass"));
//		setScale(1f);
//		setOrigin(Origin.DOWN);
//		this.swayOffset = Util.randomFloat(0f, 1f);
//		this.amplitude = Util.randomFloat(0.05f, 0.1f);
//		this.speed = 3f;//Util.randomFloat(0.8f, 1.5f);
//	}
//
//	@Override
//	public void render(Batch batch) {
//		float t = elapsedTime();
//		shear_x = (float)Math.sin(t * speed + swayOffset) * amplitude;
//		super.render(batch);
//	}
//	
//	
//}
