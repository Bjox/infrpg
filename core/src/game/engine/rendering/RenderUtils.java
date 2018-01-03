package game.engine.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import java.util.ArrayList;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class RenderUtils {
	
	private static final ShapeRenderer sr = new ShapeRenderer();
	private static final ArrayList<Shape> outlinedRenderQueue = new ArrayList<>();
	private static final ArrayList<Shape> filledRenderQueue = new ArrayList<>();
	private static final Matrix4 IDENTITY_MATRIX = new Matrix4();
	
	public static void queueOutlinedShape(Shape shape) {
		outlinedRenderQueue.add(shape);
	}
	
	public static void queueFilledShape(Shape shape) {
		filledRenderQueue.add(shape);
	}
	
	public static void render(Matrix4 projectionMatrix) {
		if (!outlinedRenderQueue.isEmpty()) renderQueue(outlinedRenderQueue, ShapeRenderer.ShapeType.Line, projectionMatrix);
		if (!filledRenderQueue.isEmpty()) renderQueue(filledRenderQueue, ShapeRenderer.ShapeType.Filled, projectionMatrix);
	}
	
	private static void renderQueue(ArrayList<Shape> queue, ShapeRenderer.ShapeType type, Matrix4 projectionMatrix) {
		sr.begin(type);
		sr.setProjectionMatrix(projectionMatrix);

		for (int i = 0; i < queue.size(); i++) {
			queue.get(i).render(sr);
		}

		sr.end();
		queue.clear();
	}
	
	
	public static abstract class Shape {
		private final Color color;
		private final Matrix4 transformMatrix;

		public Shape(Color color, Matrix4 transformMatrix) {
			this.color = color;
			this.transformMatrix = transformMatrix;
		}
		
		public void render(ShapeRenderer sr) {
			sr.setColor(color);
			sr.setTransformMatrix(transformMatrix);
			renderShape(sr);
		}
		
		protected abstract void renderShape(ShapeRenderer sr);
		
	}
	
	
	public static class Rect extends Shape {
		
		private final float x, y, width, height;

		public Rect(float x, float y, float width, float height, Color color, Matrix4 transformMatrix) {
			super(color, transformMatrix);
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public Rect(float x, float y, float width, float height, Color color) {
			this(x, y, width, height, color, IDENTITY_MATRIX);
		}
		
		@Override
		public void renderShape(ShapeRenderer sr) {
			sr.rect(x, y, width, height);
		}
		
	}
	
	
	public static class Circle extends Shape {
		
		private final float x, y, radius;

		public Circle(float x, float y, float radius, Color color, Matrix4 transformMatrix) {
			super(color, transformMatrix);
			this.x = x;
			this.y = y;
			this.radius = radius;
		}
		
		public Circle(float x, float y, float radius, Color color) {
			this(x, y, radius, color, IDENTITY_MATRIX);
		}

		@Override
		protected void renderShape(ShapeRenderer sr) {
			sr.circle(x, y, radius);
		}
		
	}
	
	
	public static class Point extends Shape {
		
		private final float x, y;

		public Point(float x, float y, Color color, Matrix4 transformMatrix) {
			super(color, transformMatrix);
			this.x = x;
			this.y = y;
		}
		
		public Point(float x, float y, Color color) {
			this(x, y, color, IDENTITY_MATRIX);
		}
		
		@Override
		protected void renderShape(ShapeRenderer sr) {
			sr.point(x, y, 0);
		}
	}
	
}
