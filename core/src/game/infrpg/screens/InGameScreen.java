package game.infrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.engine.logic.map.Map;
import game.infrpg.Infrpg;
import static game.infrpg.Infrpg.*;
import game.engine.logic.Camera;
import game.infrpg.entities.Spearman;
import game.engine.logic.Dir;
import game.engine.logic.Constants;
import game.engine.logic.map.Tileset;
import game.engine.rendering.shapes.RenderUtils;
import game.infrpg.entities.SwieteniaTree;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InGameScreen extends AbstractScreen {

	private final Map map;
	private final Camera cam;
	private float zoomacc = 0.0f;
	private int renderCalls;

	private final SpriteBatch batch;
	private final Vector2 isoCamPosBuffer;

	private Spearman player;
	private SwieteniaTree tree;

	private ShapeRenderer shapeRenderer;

	public InGameScreen(Infrpg game) {
		super(game);

		TextureAtlas atlas = getAtlas();

		this.isoCamPosBuffer = new Vector2();
		Tileset.loadTilesets(atlas);
		batch = new SpriteBatch();

		map = new Map("penis".hashCode()); // TODO: Hardcoded map seed

		cam = new Camera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		cam.zoom = 0.5f;

		cam.update();

		shapeRenderer = new ShapeRenderer();

		player = new Spearman();
		player.setRenderState(player.RS_DOWN);
		cam.lookAt(player);
		cam.offset_y = 25f;

		tree = new SwieteniaTree();
		tree.x = 100;

		Gdx.input.setInputProcessor(new InGameInput());
	}

	@Override
	public void render(float delta) {
		renderCalls = 0;
		handleInput(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Math.abs(zoomacc) > 0.01f) {
			float zoomspeed = zoomacc * delta * 10.0f;
			zoomacc -= zoomspeed;
			cam.zoom *= 1.0f + zoomspeed;
		}

		cam.update();
		map.render(cam);

		batch.setProjectionMatrix(cam.combined);

		batch.begin();
		player.render(batch);
		tree.render(batch);
		batch.end();

		RenderUtils.renderShapes(cam.combined);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setProjectionMatrix(cam.combined);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(player.getScreenX(), player.getScreenY(), 1f);
//		shapeRenderer.end();
		renderCalls += map.getRenderCalls();
		renderCalls += batch.renderCalls;
	}

	@Override
	public void pause() {
		logger.debug("Pause");
	}

	@Override
	public void resume() {
		logger.debug("Resume");
	}

	@Override
	public void dispose() {
		logger.debug("Disposing InGameScreen...");
		batch.dispose();
		map.dispose();
	}

	private void handleInput(float delta) {
		int directionMask = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			directionMask |= Dir.UP.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			directionMask |= Dir.LEFT.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			directionMask &= ~Dir.UP.mask;
			directionMask |= Dir.DOWN.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			directionMask &= ~Dir.LEFT.mask;
			directionMask |= Dir.RIGHT.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			map.setTileset(Tileset.Tilesets.SHIT);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			map.setTileset(Tileset.Tilesets.NORMAL);
		}

		Dir moveDir = Dir.dirFromMask(directionMask);

		if (moveDir != null) {
			Vector2 dirVector = moveDir.getIsometricDirVector();
			dirVector.scl(Constants.DEBUG_MOVEMENT_SPEED * delta);
			player.x += dirVector.x;
			player.y += dirVector.y;
			player.setDirection(moveDir);
			player.setIdle(false);
		} else {
			player.setIdle(true);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public int getRenderCalls() {
		return renderCalls;
	}

	@Override
	public String debugRenderText() {
		StringBuilder str = new StringBuilder();

		cam.getIsometricPosition(isoCamPosBuffer);
		str.append(String.format("%-15s (%.1f, %.1f)\n", "Screen pos:", cam.position.x, cam.position.y));
		str.append(String.format("%-15s (%.1f, %.1f)\n", "Isometric pos:", isoCamPosBuffer.x, isoCamPosBuffer.y));

		float chunkX = isoCamPosBuffer.x / (Constants.CHUNK_SIZE * Constants.TILE_SIZE);
		float chunkY = isoCamPosBuffer.y / (Constants.CHUNK_SIZE * Constants.TILE_SIZE);
		Point centerChunk = new Point(
				Math.floorDiv((int) isoCamPosBuffer.x, Constants.CHUNK_SIZE * Constants.TILE_SIZE),
				Math.floorDiv((int) isoCamPosBuffer.y, Constants.CHUNK_SIZE * Constants.TILE_SIZE));

		str.append(String.format("%-15s %.2f, %.2f  [%d, %d]\n", "Chunk pos:",
				chunkX, chunkY, centerChunk.getX(), centerChunk.getY()));

		str.append(String.format("%-15s %d\n", "Chunks:", map.getChunkCount()));

		return str.toString();
	}

	public Vector3 getCameraPosition() {
		return cam.position;
	}

	private class InGameInput implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			switch (keycode) {
				case Input.Keys.F1:
					Constants.RENDER_DEBUG_TEXT = !Constants.RENDER_DEBUG_TEXT;
					return true;
					
				case Input.Keys.F2:
					Constants.RENDER_ENTITY_ORIGIN = !Constants.RENDER_ENTITY_ORIGIN;
					return true;
					
				case Input.Keys.F3:
					Constants.RENDER_ENTITY_OUTLINE = !Constants.RENDER_ENTITY_OUTLINE;
					return true;
			}

			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			if (Constants.ENABLE_ZOOM) {
				zoomacc += amount * 0.2;
				return true;
			}

			return false;
		}
	}
}
