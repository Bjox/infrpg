package game.infrpg.server.map.storage;

import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Constants;
import game.infrpg.common.util.Helpers;
import game.infrpg.server.map.Region;
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
public abstract class SerializedMapStorage extends AbstractMapStorage {

	/**
	 * Return value of <code>readSerialized</code> indicating that the requesting region was not found.
	 */
	public static final int READ_SERIALIZED_NOT_FOUND = -1;

	/**
	 * Return value of <code>readSerialized</code> indicating that the supplied buffer is too small.
	 */
	public static final int READ_SERIALIZED_BUFF_OVERFLOW = -2;

	/**
	 * Return value of <code>readSerialized</code> indicating that the underlying stream ended unexpectedly.
	 */
	public static final int READ_SERIALIZED_EOF_EOS = -3;

	/**
	 * The global logger object.
	 */
	private final ILogger logger;

	/**
	 * Deserialization buffer.
	 */
	private final byte[] readBuffer;

	public SerializedMapStorage() {
		this.logger = Globals.logger();
		this.readBuffer = new byte[calcBufferSize()];
	}

	@Override
	public synchronized Region getRegion(int x, int y) {
		throwIfStorageIsClosed();
		try {
			int readResult = readSerialized(x, y, readBuffer);

			switch (readResult) {
				case READ_SERIALIZED_NOT_FOUND:
					logger.debug(String.format("Encountered READ_SERIALIZED_NOT_FOUND when reading region (%d,%d).", x, y));
					return null;
				case READ_SERIALIZED_BUFF_OVERFLOW:
					throw new Exception(String.format("Encountered READ_SERIALIZED_BUFF_OVERFLOW when reading region (%d,%d).", x, y));
				case READ_SERIALIZED_EOF_EOS:
					throw new Exception(String.format("Encountered READ_SERIALIZED_EOF_EOS when reading region (%d,%d).", x, y));
			}

			int length = readResult;
			logger.debug("Deserializing region, length = " + Helpers.formatSizeInBytes(length));
			ByteArrayInputStream bis = new ByteArrayInputStream(readBuffer, 0, length);
			InputStream is = Constants.COMPRESS_REGIONS ? new GZIPInputStream(bis) : bis;

			try (ObjectInputStream ois = new ObjectInputStream(is)) {
				Object obj = ois.readObject();
				Region region = (Region) obj;
				logger.debug("Deserialized region " + region.toString());
				return region;
			}
		}
		catch (Exception e) {
			throw Helpers.wrapInRuntimeException(e);
		}
	}

	@Override
	public synchronized void storeRegion(Region region) {
		throwIfStorageIsClosed();
		try {
			logger.debug("Serializing region " + region.toString());

			ByteArrayOutputStream bos = new ByteArrayOutputStream(calcBufferSize()); // TODO: Optimization, buffer created each time
			OutputStream os = Constants.COMPRESS_REGIONS ? new GZIPOutputStream(bos) : bos;

			try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
				oos.writeObject(region);
				oos.flush();
			}
			
			byte[] bytes = bos.toByteArray();
			logger.debug("Serialized region length = " + Helpers.formatSizeInBytes(bytes.length));
			writeSerialized(region.position.getX(), region.position.getY(), bytes); // TODO: Optimization, byte array is copied
		}
		catch (Exception e) {
			throw Helpers.wrapInRuntimeException(e);
		}
	}

	protected abstract void writeSerialized(int x, int y, byte[] data) throws Exception;

	protected abstract int readSerialized(int x, int y, byte[] buffer) throws Exception;

	private static int calcBufferSize() {
		// 1M, if CHUNK_SIZE is 16 and REGION_SIZE is 32
		int s = Constants.CHUNK_SIZE * Constants.REGION_SIZE;
		return s * s * 4;
	}

}
