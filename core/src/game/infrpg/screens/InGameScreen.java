package game.infrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.infrpg.logic.map.Map;
import game.infrpg.Infrpg;
import static game.infrpg.Infrpg.*;
import game.infrpg.logic.Camera;
import game.infrpg.graphics.testents.Spearman;
import game.infrpg.logic.Dir;
import game.infrpg.logic.Constants;
import game.infrpg.logic.map.Tileset;
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
	
	private TextureRegion crossTex;
	private Spearman player;
	
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
		
		player = new Spearman();
		player.setRenderState(player.RS_DOWN);
		cam.lookAt(player);
		cam.offset_y = 25f;
		
		crossTex = atlas.findRegion("cross");
		
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
		batch.draw(crossTex, 0, 0);
		player.render(batch);
		batch.end();
		
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
		
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
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
				Math.floorDiv((int)isoCamPosBuffer.x, Constants.CHUNK_SIZE * Constants.TILE_SIZE),
				Math.floorDiv((int)isoCamPosBuffer.y, Constants.CHUNK_SIZE * Constants.TILE_SIZE));
		
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
				if (Constants.ENABLE_ZOOM) zoomacc += amount * 0.2;
				return false;
			}
	} 
}
