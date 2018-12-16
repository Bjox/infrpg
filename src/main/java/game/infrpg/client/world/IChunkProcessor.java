package game.infrpg.client.world;

import com.badlogic.gdx.utils.Disposable;
import game.infrpg.server.map.Chunk;
import java.io.Closeable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IChunkProcessor extends Disposable
{
	IMapChunk process(Chunk chunk);
	
	Executor getAsyncExecutor();
	
	default CompletableFuture<IMapChunk> processAsync(Chunk chunk)
	{
		return CompletableFuture.supplyAsync(() -> process(chunk), getAsyncExecutor());
	}
}
