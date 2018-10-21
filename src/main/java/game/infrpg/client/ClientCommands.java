package game.infrpg.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.infrpg.common.console.Console;
import game.infrpg.common.util.Constants;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Helpers;
import java.util.concurrent.CompletableFuture;
import lib.cmd.Command;
import lib.cmd.CommandException;
import lib.cmd.CommandObject;
import lib.cmd.Default;
import lib.di.Inject;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientCommands implements CommandObject {

	@Inject
	public ClientCommands() {
	}

	@Command
	public void maxFps(int fps) {
		int foregroundFps = fps;

		if (foregroundFps < 0) {
			throw new CommandException("fps must be positive.");
		}

		int backgroundFps = foregroundFps;
		int foregroundLimit = Helpers.getClientForegroundFpsLimit();
		if (foregroundLimit == 0) {
			foregroundLimit = Integer.MAX_VALUE;
		}
		foregroundFps = Math.min(foregroundFps, foregroundLimit);
		int backgroundLimit = Helpers.getClientBackgroundFpsLimit();
		if (backgroundLimit == 0) {
			backgroundLimit = Integer.MAX_VALUE;
		}
		backgroundFps = Math.min(backgroundFps, backgroundLimit);

		Globals.resolve(LwjglApplicationConfiguration.class).foregroundFPS = foregroundFps;
		Globals.resolve(LwjglApplicationConfiguration.class).backgroundFPS = backgroundFps;
	}
	
	@Command
	public void connect(String ip, @Default(Constants.DEFAULT_PORT_STR) int port) {
		InfrpgGame.connect(ip, port);
	}

}
