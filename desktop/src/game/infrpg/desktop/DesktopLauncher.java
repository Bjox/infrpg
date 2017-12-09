package game.infrpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import game.infrpg.Infrpg;

public class DesktopLauncher {
	public static void main (String[] arg) {
//		TexturePacker.Settings texSettings = new TexturePacker.Settings();
//		TexturePacker.process(texSettings, "raw/", "packed/", "pack");
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = 800;
		config.title = "infRpg";
		config.vSyncEnabled = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = 30;
		new LwjglApplication(new Infrpg(config, arg), config);
	}
}
