package lib.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PropertiesConfigStore extends Properties implements IConfigStore {
	
	public final String comment;
	
	/**
	 * Creates a new PropertiesConfigStore.
	 * @param comment A description of the properties.
	 */
	public PropertiesConfigStore(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Creates a new PropertiesConfigStore.
	 */
	public PropertiesConfigStore() {
		this(null);
	}

	@Override
	public void store(OutputStream out) throws IOException {
		store(out, comment);
	}
	
}
