package game.infrpg.server;

import game.infrpg.client.logic.map.IMapStorage;
import game.infrpg.client.logic.map.SQLiteMapStorage;
import game.infrpg.common.Instance;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.ArgumentParser;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InfrpgServer extends Instance {

	public InfrpgServer(ArgumentParser args) {
		super(args);
		
	}

	@Override
	public void start() {
		try (IMapStorage mapStorage = new SQLiteMapStorage("testmap.db")) {
			
		}
		catch (Exception e) {
			logger.trackException(e);
		}
	}
	
}
