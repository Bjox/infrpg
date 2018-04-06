package game.infrpg.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.infrpg.client.InfrpgGame;
import static game.infrpg.client.InfrpgGame.logger;
import game.infrpg.client.entities.rendering.Renderable;

/**
 * The graphics asset defines the type of graphics and how the asset is stored on the filesystem.
 *
 * <h3>Directional</h3>
 * A directional type format has one file for each of the following directions:
 * <ul>
 * <li>up</li>
 * <li>down</li>
 * <li>left</li>
 * <li>right</li>
 * <li>upleft</li>
 * <li>upright</li>
 * <li>downleft</li>
 * <li>downright</li>
 * </ul>
 * The filename of directional assets must be on the format <code>assetname_direction.png</code><br>
 * where <i>direction</i> refers to the directions listed above.
 *
 * <h3>Indexed</h3>
 * An indexed type is typically an animation where each keyframe is stored as a separate file.
 * The file format is: <code>assetname_index.png</code><br>
 * where <i>index</i> is an integer specifying where this frame appear in the animation order.
 *
 * <h3>Sheet</h3>
 * A sheet is a single file containing multiple keyframes side-by-side in a row/column format:<br>
 * -------------<br>
 * | 1 | 2 | 3 |<br>
 * |--- --- ---|<br>
 * | 4 | 5 | 6 |<br>
 * -------------<br>
 *
 * <h3>Sheet directional</h3>
 * A combination of the sheet and directional format. The asset defines each direction according
 * to the directional format (one file per direction). Additionally, each directional file is
 * itself in the sheet format, denoting an animation.
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class GraphicsAsset {

	public final String path;

	public GraphicsAsset(String path) {
		this.path = path;
	}
	
	protected TextureAtlas getAtlas() {
		return InfrpgGame.getAtlas();
	}
	
	public abstract Renderable loadRenderable();
	
	/**
	 * Log a warning message when an asset is not found.
	 * @param name 
	 */
	protected static void warnNotFound(String name) {
		logger.error(String.format("Asset \"%s\" not found!", name));
	}
}
