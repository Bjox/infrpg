package game.infrpg.server.map.storage;

import game.infrpg.common.util.Helpers;
import java.io.IOException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractMapStorage implements IMapStorage {
	
	private boolean closed;

	public AbstractMapStorage() {
		this.closed = false;
	}
	
	@Override
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * If overridden, this method must also be called (<code>super.close()</code>).
	 * @throws IOException 
	 */
	@Override
	public void close() throws IOException {
		closed = true;
	}

	/**
	 * Override in subclasses if custom initialization is needed.
	 * @throws Exception 
	 */
	@Override
	public void init() throws Exception {
	}
	
	/**
	 * Call this method before at the start of IO-specific methods to check if the storage has been closed.
	 */
	protected void throwIfStorageIsClosed() {
		if (isClosed()) {
			throw Helpers.wrapInRuntimeException(new IOException("The map storage is closed."));
		}
	}
	
}
