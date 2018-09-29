package game.infrpg.server.map;

import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Constants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class SerializedMapStorage implements IMapStorage {

	/** Return value of <code>readSerialized</code> indicating that the requesting region was not found. */
	public static final int READ_SERIALIZED_NOT_FOUND = -1;
	
	/** Return value of <code>readSerialized</code> indicating that the supplied buffer is too small. */
	public static final int READ_SERIALIZED_BUFF_OVERFLOW = -2;
	
	/** Return value of <code>readSerialized</code> indicating that the underlying stream ended unexpectedly. */
	public static final int READ_SERIALIZED_EOF_EOS = -3;
	
	
	/** The global logger object. */
	private final ILogger logger;
	
	/** Deserialization buffer. */
	private final byte[] readBuffer;

	public SerializedMapStorage() {
		this.logger = Globals.logger();
		this.readBuffer = new byte[calcBufferSize()];
	}

	@Override
	public Region getRegion(int x, int y) throws Exception {
		int readResult = readSerialized(x, y, readBuffer);

		switch (readResult) {
			case READ_SERIALIZED_NOT_FOUND:
				logger.debug(String.format("Encountered READ_SERIALIZED_NOT_FOUND when reading region [%d,%d].", x, y));
				return null;
			case READ_SERIALIZED_BUFF_OVERFLOW:
				throw new Exception(String.format("Encountered READ_SERIALIZED_BUFF_OVERFLOW when reading region [%d,%d].", x, y));
			case READ_SERIALIZED_EOF_EOS:
				throw new Exception(String.format("Encountered READ_SERIALIZED_EOF_EOS when reading region [%d,%d].", x, y));
		}
		
		int length = readResult;
		logger.debug("Deserializing region, length = " + (length/1024) + "KiB");
		ByteArrayInputStream bis = new ByteArrayInputStream(readBuffer, 0, length);
		InputStream is = Constants.COMPRESS_REGIONS ? new GZIPInputStream(bis) : bis;
		
		try (ObjectInputStream ois = new ObjectInputStream(is)) {
			Object obj = ois.readObject();
			Region region = (Region)obj;
			logger.debug("Deserialized region " + region.toString());
			return region;
		}
	}

	@Override
	public void storeRegion(Region region) throws Exception {
		logger.debug("Serializing region " + region.toString());

		ByteArrayOutputStream bos = new ByteArrayOutputStream(calcBufferSize());
		OutputStream os = Constants.COMPRESS_REGIONS ? new GZIPOutputStream(bos) : bos;

		try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
			oos.writeObject(region);
			oos.flush();
		}
		writeSerialized(region.position.getX(), region.position.getY(), bos.toByteArray()); // TODO: Optimization, byte array is copied
	}

	protected abstract void writeSerialized(int x, int y, byte[] data) throws Exception;

	protected abstract int readSerialized(int x, int y, byte[] buffer) throws Exception;

	private static int calcBufferSize() {
		int s = Constants.CHUNK_SIZE * Constants.REGION_SIZE;
		return s * s * 3;
	}

}
