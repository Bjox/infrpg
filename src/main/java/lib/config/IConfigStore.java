package lib.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IConfigStore extends Map<Object, Object> {
	
	default String getString(Object key) {
		return String.valueOf(get(key));
	}
	
	void store(OutputStream out) throws IOException;
	
	void load(InputStream in) throws IOException;
	
}
