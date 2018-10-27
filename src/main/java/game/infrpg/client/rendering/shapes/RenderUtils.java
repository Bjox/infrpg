package game.infrpg.client.rendering.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import java.util.ArrayList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class RenderUtils {

	public static final Matrix4 ISOMETRIC_TRANSFORM;
	
	static {
		ISOMETRIC_TRANSFORM = new Matrix4();
		Affine2 affine = new Affine2();
		affine.m00 = 1;
		affine.m01 = -1;
		affine.m10 = .5f;
		affine.m11 = .5f;
		ISOMETRIC_TRANSFORM.setAsAffine(affine);
	}
	
	private static final ShapeRenderer sr = new ShapeRenderer();
	private static final ArrayList<Shape> outlinedRenderQueue = new ArrayList<>();
	private static final ArrayList<Shape> filledRenderQueue = new ArrayList<>();

	public static void queueOutlinedShape(Shape shape) {
		outlinedRenderQueue.add(shape);
	}

	public static void queueFilledShape(Shape shape) {
		filledRenderQueue.add(shape);
	}

	public static void renderShapes(Matrix4 projectionMatrix) {
		renderShapeQueue(outlinedRenderQueue, ShapeRenderer.ShapeType.Line, projectionMatrix);
		renderShapeQueue(filledRenderQueue, ShapeRenderer.ShapeType.Filled, projectionMatrix);
	}

	private static void renderShapeQueue(ArrayList<Shape> queue, ShapeRenderer.ShapeType type, Matrix4 projectionMatrix) {
		if (queue.isEmpty()) {
			return;
		}

		sr.begin(type);
		sr.identity();
		sr.setProjectionMatrix(projectionMatrix);
		
		for (int i = 0; i < queue.size(); i++) {
			queue.get(i).render(sr);
		}

		sr.end();
		queue.clear();
	}

}
