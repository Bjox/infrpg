package game.infrpg.client.entity;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public class EntityType<T extends Entity> {
	
	public final String name;
	private final Class<T> instanceClass;

	public EntityType(String name, Class<T> instanceClass) {
		this.name = name;
		this.instanceClass = instanceClass;
	}
	
}
