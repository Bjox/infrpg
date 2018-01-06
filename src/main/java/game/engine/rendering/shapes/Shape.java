package game.engine.rendering.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Shape {
	
	private static final Matrix4 IDENTITY_MATRIX = new Matrix4();

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

	
	public static class Line extends Shape {

		private final float x1,y1, x2,y2;

		public Line(float x1, float y1, float x2, float y2, Color color, Matrix4 transformMatrix) {
			super(color, transformMatrix);
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		
		public Line(float x1, float y1, float x2, float y2, Color color) {
			this(x1, y1, x2, y2, color, IDENTITY_MATRIX);
		}
		
		@Override
		protected void renderShape(ShapeRenderer sr) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
		
	}
	
}
