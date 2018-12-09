package game.infrpg.server.map;

import java.io.Serializable;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractChunk implements Serializable
{
	// Increment this if non-backwards compatible changes are made on the class.
	private static final long serialVersionUID = 2L;
	
	/** Calculated from chunk position. */
	public final long id;
	
	/** The chunk position. */
	public final ReadablePoint position;

	/**
	 * Empty constructor for kryo.
	 */
	public AbstractChunk()
	{
		this.id = 0;
		this.position = null;
	}
	
	public AbstractChunk(int x, int y)
	{
		this.id = calculateChunkId(x, y);
		this.position = new Point(x, y);
	}
	
	public AbstractChunk(AbstractChunk orig)
	{
		this.id = orig.id;
		this.position = orig.position;
	}
	
	public long getId()
	{
		return id;
	}
	
	public static long calculateChunkId(int x, int y)
	{
		return Integer.toUnsignedLong(x) << 32 | Integer.toUnsignedLong(y);
	}
	
	public static long calculateChunkId(Chunk chunk)
	{
		return calculateChunkId(chunk.position.getX(), chunk.position.getY());
	}
	
	
}
