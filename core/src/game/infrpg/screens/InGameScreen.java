package game.infrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.infrpg.logic.map.Map;
import game.infrpg.Infrpg;
import static game.infrpg.Infrpg.*;
import game.infrpg.graphics.Camera;
import game.infrpg.graphics.assets.Grass;
import game.infrpg.graphics.assets.Spearman;
import game.infrpg.logic.Dir;
import game.infrpg.graphics.assets.SwieteniaTree;
import game.infrpg.logic.Constants;
import game.infrpg.logic.map.Tileset;
import game.infrpg.util.Util;
import java.util.ArrayList;
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
	private SwieteniaTree tree;
	private Spearman player;
//	private Grass grass1;
	private ArrayList<Grass> grass;
	
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
		
		tree = new SwieteniaTree();
		tree.x = 100;
		tree.y = -100;
		
		player = new Spearman();
		cam.lookAt(player);
		cam.offset_y = 25f;
	
		int n = 10;
		grass = new ArrayList<>(n*n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Grass g = new Grass();
				g.x = i * 25 + Util.randomFloat(-5, 5);
				g.y = j * 25 + Util.randomFloat(-5, 5);
				g.swayOffset = g.y * 0.01f + Util.randomFloat(0.3f, 0.5f);
				grass.add(g);
			}
		}
		java.util.Collections.sort(grass, (Grass o1, Grass o2) -> {
			if (o1.getScreenY() > o2.getScreenY()) return -1;
			else return 1;
		});
		
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
//		batch.setTransformMatrix(IDENTITY);
		
		batch.begin();
//		player.render(batch);
		tree.render(batch);
		
//		Matrix4 shear = new Matrix4();
//		shear.val[Matrix4.M01] = 1f; // x
//		shear.val[Matrix4.M10] = 0; // y
//		batch.setTransformMatrix(shear);
		
//		grass1.render(batch);
		boolean pRendered = false;
		for (Grass g : grass) {
			if (!pRendered && g.getScreenY() < player.getScreenY()) {
				player.render(batch);
				pRendered = true;
			}
			g.render(batch);
		}
		if (!pRendered) player.render(batch);
		
		batch.draw(crossTex, 0, 0);
		
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
