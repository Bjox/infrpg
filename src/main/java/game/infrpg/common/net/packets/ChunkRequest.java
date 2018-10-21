package game.infrpg.common.net.packets;

import game.infrpg.common.net.NetPacket;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ChunkRequest extends NetPacket {
	
	public final int x, y;

	public ChunkRequest(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("ChunkRequest [%d,%d]", x, y);
	}
	
}
