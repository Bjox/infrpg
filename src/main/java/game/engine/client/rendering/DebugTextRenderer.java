package game.engine.client.rendering;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DebugTextRenderer {
	
	private final List<StringBuffer> lines;
	private final BitmapFont font;
	
	public DebugTextRenderer(BitmapFont font) {
		this.lines = new ArrayList<>();
		this.font = font;
	}
	
	public void setLine(int line, String str) {
		while (lines.size() <= line) {
			lines.add(new StringBuffer(0));
		}
		
		lines.get(line).replace(0, Integer.MAX_VALUE, str);
	}
	
	public void render(Batch batch, float x, float y) {
		for (int i = 0; i < lines.size(); i++) {
			font.draw(batch, lines.get(i), x, y - i*font.getLineHeight());
		}
	}

}
