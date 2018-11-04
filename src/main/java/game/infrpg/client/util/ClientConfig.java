package game.infrpg.client.util;

import java.io.IOException;
import lib.cmd.TypeParser;
import lib.config.AbstractConfig;
import lib.config.ConfigClass;
import lib.config.ConfigField;
import lib.config.IConfigStore;
import lib.di.Inject;
import lib.storage.IStorage;
import org.lwjgl.util.Dimension;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@ConfigClass
public class ClientConfig extends AbstractConfig {
	
	@ConfigField
	public ResolutionConfig screenResolution = new ResolutionConfig(1000, 800);
	@ConfigField
	public boolean verticalSync = false;
	@ConfigField
	public boolean fullscreen = false;
	
	@Inject
	public ClientConfig(IConfigStore configStore, IStorage storage, TypeParser typeParser) throws IOException {
		super(configStore, storage, typeParser);
		typeParser.addParser(ResolutionConfig.class, ResolutionConfig::parse);
	}
	
	public static class ResolutionConfig
	{
		public int width, height;

		public ResolutionConfig(int width, int height)
		{
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString()
		{
			return String.format("%dx%d", width, height);
		}
		
		public Dimension toDimension()
		{
			return new Dimension(width, height);
		}
		
		public static ResolutionConfig parse(String value)
		{
			try
			{
				String[] parts = value.split("x");
				return new ResolutionConfig(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			}
			catch (Exception e)
			{
				throw new RuntimeException("Cannot parse \"" + value + "\" to ResolutionConfig.", e);
			}
		}
	}
	
}
