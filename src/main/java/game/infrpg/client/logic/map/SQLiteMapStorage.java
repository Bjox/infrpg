package game.infrpg.client.logic.map;

import game.infrpg.common.console.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class SQLiteMapStorage implements IMapStorage {
	
	private final Connection connection;
	private final Logger logger;
	
	public SQLiteMapStorage(String dbfilename) throws SQLException {
		logger = Logger.getPublicLogger();
		String connectionUrl = String.format("jdbc:sqlite:%s", dbfilename);
		logger.debug("Setting up SQLite connection: " + connectionUrl);
		connection = DriverManager.getConnection(connectionUrl);
	}
	
	@Override
	public Region getRegion(int x, int y) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean storeRegion(Region region) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
