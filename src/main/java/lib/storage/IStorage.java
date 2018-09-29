package lib.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IStorage {
	
	InputStream getInputStream() throws IOException;
	
	OutputStream getOutputStream() throws IOException;
	
}
