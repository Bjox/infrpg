package game.infrpg;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.util.jar.Attributes;
import lib.ArgumentParser;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommonModule extends AbstractModule {
	
	private final String[] args;
	
	public CommonModule(String[] args) {
		this.args = args;
	}
	
	@Override
	protected void configure() {
		bind(String[].class)
				.annotatedWith(Names.named("command line args"))
				.toInstance(args);
		
		bind(ArgumentParser.class)
				.in(Singleton.class);
	}
	
}
