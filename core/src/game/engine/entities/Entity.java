package game.engine.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import game.infrpg.Infrpg;
import static game.infrpg.Infrpg.logger;
import game.engine.entities.rendering.Renderable;

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
	
	
	/**
	 * Entity constructor.
	 */
	public Entity() {
		this.stateMap = new RenderStateMap(1);
		RS_DEFAULT = registerRenderState(Renderable.NULL_RENDERABLE);
		renderState = RS_DEFAULT;
		
		scale_x = 1f;
		scale_y = 1f;
	}
	
	/**
	 * Register a new render state in the currently active render state map.
	 * @param renderable
	 * @return 
	 */
	protected final int registerRenderState(Renderable renderable) {
		return stateMap.registerRenderState(renderable);
	}
	
	/**
	 * Get the current render state.
	 * @return 
	 */
	public int getRenderState() {
		return renderState;
	}
	
	/**
	 * Set the current entity render state.
	 * @param renderState 
	 */
	public void setRenderState(int renderState) {
		this.renderState = renderState;
	}
	
	/**
	 * Set a rendering state renderable in the currently active render state map.
	 * @param renderState
	 * @param renderable 
	 */
	protected final void setRenderStateRenderable(int renderState, Renderable renderable) {
		stateMap.set(renderState, renderable);
	}
	
	/**
	 * Get the renderable to be drawn.
	 * @return 
	 */
	protected Renderable getRenderable() {
		return this.stateMap.get(this.renderState);
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
	public float getScreenX() {
		return x - y;
	}
	
	/**
	 * Get the screen position of this entity.
	 * @return 
	 */
	public float getScreenY() {
		return (x + y) * 0.5f;
	}
	
	/**
	 * Tick and render this entity, in order.
	 * @param batch 
	 */
	public void tickAndRender(Batch batch) {
		tick();
		render(batch);
	}
	
	/**
	 * Render this entity.
	 * @param batch The batch in which to queue this render.
	 */
	public void render(Batch batch) {
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
	}
	
	/**
	 * Tick this entity.
	 */
	public final void tick() {
		tick(Infrpg.deltaTime());
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
	protected TextureRegion getTextureRegion() {
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

