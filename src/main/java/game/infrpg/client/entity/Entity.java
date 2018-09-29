package game.infrpg.client.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import game.infrpg.common.util.Globals;
import game.infrpg.client.InfrpgGame;
import game.infrpg.client.rendering.renderable.Renderable;
import game.infrpg.client.rendering.shapes.RenderUtils;
import game.infrpg.client.rendering.shapes.Shape;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Entity {
	
	/** The intermediary affine transformation used when rendering entities. */
	private static final Affine2 RENDER_MAT = new Affine2();
	
	/** Position on the isometric map. */
	public float x, y;
	/** Render scale. */
	public float scale_x, scale_y;
	/** Origin relative to the bottom left corner. */
	public float origin_x, origin_y;
	/** Shear to apply when rendering. */
	public float shear_x, shear_y;
	
	/** The default render state. */
	public final int RS_DEFAULT;
	/** The render state map. */
	private final RenderStateMap stateMap;
	/** Current render state. */
	private int renderState;
	
	private final ILogger logger;
	
	
	/**
	 * Entity constructor.
	 */
	public Entity() {
		this.stateMap = new RenderStateMap(1);
		this.logger = Globals.logger();
		
		RS_DEFAULT = registerRenderState(Renderable.NULL_RENDERABLE);
		renderState = RS_DEFAULT;
		
		scale_x = 1f;
		scale_y = 1f;
		
	}
	
	/**
	 * Register a new render state in the currently active render state map.
	 * @param renderable
	 * @return The render state key.
	 */
	protected final int registerRenderState(Renderable renderable) {
		return stateMap.registerRenderState(renderable);
	}
	
	/**
	 * Get the current render state.
	 * @return The current render state key.
	 */
	public final int getRenderState() {
		return renderState;
	}
	
	/**
	 * Set the current entity render state.
	 * @param renderState The render state key.
	 */
	public final void setRenderState(int renderState) {
		this.renderState = renderState;
	}
	
	/**
	 * Set a rendering state renderable in the render state map.
	 * @param renderState
	 * @param renderable 
	 */
	protected final void setRenderStateRenderable(int renderState, Renderable renderable) {
		stateMap.set(renderState, renderable);
	}
	
	/**
	 * Get the render state map to be used for rendering.
	 * Override this method in derived classes in order to provide
	 * alternative state maps in certain situations.
	 * @return 
	 */
	protected RenderStateMap getRenderStateMap() {
		return getBaseRenderStateMap();
	}
	
	/**
	 * Get the base render state map.
	 * @return 
	 */
	protected final RenderStateMap getBaseRenderStateMap() {
		return stateMap;
	}
	
	/**
	 * Set the entity scale.
	 * @param s 
	 */
	public final void setScale(float s) {
		this.scale_x = s;
		this.scale_y = s;
	}
	
	/**
	 * Set the entity scale.
	 * @param x
	 * @param y 
	 */
	public final void setScale(float x, float y) {
		this.scale_x = x;
		this.scale_y = y;
	}
	
	/**
	 * Set the entity origin.
	 * @param x
	 * @param y 
	 */
	public final void setOrigin(float x, float y) {
		this.origin_x = x;
		this.origin_y = y;
	}
	
	/**
	 * Sets the origin of this entity. See the entity class
	 * for possible values.
	 * @param origin 
	 */
	public final void setOrigin(Origin origin) {
		switch (origin) {
			case CENTER:    setOrigin(0.5f, 0.5f); break;
			case DOWN:      setOrigin(0.5f, 0.0f); break;
			case DOWNLEFT:  setOrigin(0.0f, 0.0f); break;
			case DOWNRIGHT: setOrigin(1.0f, 0.0f); break;
			case LEFT:      setOrigin(0.0f, 0.5f); break;
			case RIGHT:     setOrigin(1.0f, 0.5f); break;
			case UP:        setOrigin(0.5f, 1.0f); break;
			case UPLEFT:    setOrigin(0.0f, 1.0f); break;
			case UPRIGHT:   setOrigin(1.0f, 1.0f); break;
			default: logger.warning("Unknown origin passed to setOrigin.");
		}
	}
	
	/**
	 * Get the screen position of this entity.
	 * @return 
	 */
	public final float getScreenX() {
		return x - y;
	}
	
	/**
	 * Get the screen position of this entity.
	 * @return 
	 */
	public final float getScreenY() {
		return (x + y) * 0.5f;
	}
	
	/**
	 * Tick and render this entity, in order.
	 * @param batch 
	 */
	public final void tickAndRender(Batch batch) {
		tick();
		render(batch);
	}
	
	/**
	 * Render this entity.
	 * @param batch The batch in which to queue this render.
	 */
	public final void render(Batch batch) {
		TextureRegion tr = getTextureRegion();
		if (tr == null) return;
		
		float width = tr.getRegionWidth();
		float height = tr.getRegionHeight();
		
		RENDER_MAT.idt()
			.translate(
					getScreenX() - origin_x * width * scale_x,
					getScreenY() - origin_y * height * scale_y)
			.shear(
					shear_x,
					shear_y)
			.scale(
					scale_x,
					scale_y);
		
		batch.draw(tr, width, height, RENDER_MAT);
		
		if (Globals.RENDER_ENTITY_OUTLINE) {
			RenderUtils.queueOutlinedShape(new Shape.Rect(0, 0, width, height, Color.RED, new Matrix4().set(RENDER_MAT)));
		}
		
		if (Globals.RENDER_ENTITY_ORIGIN) {
			RenderUtils.queueFilledShape(new Shape.Point(getScreenX(), getScreenY(), Color.CYAN));
		}
	}
	
	/**
	 * Tick this entity.
	 */
	public final void tick() {
		tick(InfrpgGame.deltaTime());
	}
	
	/**
	 * Override this method in derived classes in order to
	 * implement tick logic.
	 * @param delta_t 
	 */
	protected void tick(float delta_t) {
	}
	
	/**
	 * Get the texture region to be drawn.
	 * @return A texture region, or null if nothing is to be rendered.
	 */
	private TextureRegion getTextureRegion() {
		return getRenderStateMap().get(renderState).getTextureRegion();
	}
	
	
	public enum Origin {
		LEFT,
		UPLEFT,
		UP,
		UPRIGHT,
		RIGHT,
		DOWNRIGHT,
		DOWN,
		DOWNLEFT,
		CENTER
	}
	
}

