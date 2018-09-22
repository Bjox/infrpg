package game.infrpg.server.map.sqlite;

import com.badlogic.gdx.Gdx;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.Region;
import lib.logger.Logger;
import game.infrpg.server.map.SerializedMapStorage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SQLiteMapStorage extends SerializedMapStorage {

	private static final String MAPDB_SCHEMA_NAME = "scripts/mapdb_schema.sql";
	private static final int DB_PAGE_SIZE = 8192;

	private static final String SELECT_REGION_SQL
			= "SELECT data FROM Regions WHERE x = ? AND y = ?";

	private static final String INSERT_REGION_SQL
			= "INSERT INTO Regions (x, y, data) VALUES (?, ?, ?)";

	private static final String UPDATE_REGION_SQL
			= "UPDATE Regions SET data = ? WHERE x = ? AND y = ?";

	private final Connection connection;
	private final Logger logger;
	private final PreparedStatement selectRegionStatement;
	private final PreparedStatement insertRegionStatement;
	private final PreparedStatement updateRegionStatement;

	public SQLiteMapStorage(File dbFile) throws SQLException, IOException {
		logger = Logger.getPublicLogger();

		String connectionUrl = String.format("jdbc:sqlite:%s", dbFile.getAbsolutePath());
		logger.debug("Setting up SQLite connection: " + connectionUrl);

		if (dbFile.exists()) {
			logger.debug("Connecting to existing db");
			connection = DriverManager.getConnection(connectionUrl);
		}
		else {
			connection = createDb(connectionUrl);
			logger.debug("Map db setup complete");
		}
		connection.setAutoCommit(false);

		selectRegionStatement = connection.prepareStatement(SELECT_REGION_SQL);
		insertRegionStatement = connection.prepareStatement(INSERT_REGION_SQL);
		updateRegionStatement = connection.prepareStatement(UPDATE_REGION_SQL);
	}

	private final Connection createDb(String connectionUrl) throws SQLException, IOException {
		logger.debug("Setting up new map db");

		SQLiteConfig config = new SQLiteConfig();
		config.setPageSize(DB_PAGE_SIZE);

		logger.debug("db properties: " + config.toProperties());
		Connection connection = DriverManager.getConnection(connectionUrl, config.toProperties());

		// In order for page size change to take effect
		logger.debug("Running VACUUM command on db");
		Statement statement = connection.createStatement();
		statement.executeUpdate("VACUUM");
		statement.close();

		logger.debug("Running db schema script");

		ScriptRunner runner = new ScriptRunner(connection, false, true);
		InputStream instream = Gdx.files.internal(MAPDB_SCHEMA_NAME).read();
		InputStreamReader reader = new InputStreamReader(instream);
		runner.runScript(reader);

		return connection;
	}

	@Override
	protected int readSerialized(int x, int y, byte[] buffer) throws Exception {
		logger.debug("Reading region from db");
		
		selectRegionStatement.setInt(1, x);
		selectRegionStatement.setInt(2, y);

		try (ResultSet result = selectRegionStatement.executeQuery()) {
			if (result.next()) {
				try (
						InputStream in = result.getBinaryStream(1);
						BufferedInputStream bin = new BufferedInputStream(in)) {
					int bytesRead = bin.read(buffer);
					if (bytesRead == -1) {
						return READ_SERIALIZED_EOF_EOS;
					}
					if (bin.available() + bytesRead > buffer.length) {
						return READ_SERIALIZED_BUFF_OVERFLOW;
					}
					return bytesRead;
				}

			}
			return READ_SERIALIZED_NOT_FOUND;
		}
	}

	@Override
	protected void writeSerialized(int x, int y, byte[] data) throws SQLException {
		logger.debug("Writing region to db");

		// Try to update region first
		updateRegionStatement.setBytes(1, data);
		updateRegionStatement.setInt(2, x);
		updateRegionStatement.setInt(3, y);
		int rowsAffected = updateRegionStatement.executeUpdate();

		// Insert region if it does not exist
		if (rowsAffected == 0) {
			logger.debug("Inserting region into db");
			insertRegionStatement.setInt(1, x);
			insertRegionStatement.setInt(2, y);
			insertRegionStatement.setBytes(3, data);
			insertRegionStatement.executeUpdate();
		}

		insertRegionStatement.getConnection().commit();
	}

	@Override
	public void close() throws IOException {
		logger.debug("Closing SQLite map storage db connection");
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException e) {
			throw new IOException(e);
		}
	}

}
