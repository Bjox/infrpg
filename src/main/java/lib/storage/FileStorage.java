package lib.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FileStorage implements IStorage {
	
	private final File file;

	public FileStorage(File file) {
		this.file = file;
	}
	
	public FileStorage(String pathname) {
		this(new File(pathname));
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return new BufferedInputStream(new FileInputStream(file));
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return new BufferedOutputStream(new FileOutputStream(file));
	}
	
}
