package game.infrpg.client.world;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractChunkProcessor implements IChunkProcessor
{
	private final ExecutorService executorService;

	public AbstractChunkProcessor()
	{
		this.executorService = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public Executor getAsyncExecutor()
	{
		return executorService;
	}

	@Override
	public void dispose()
	{
		executorService.shutdown();
	}
}
