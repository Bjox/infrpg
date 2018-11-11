package lib.util.noise;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lib.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class NoiseVisualizer extends JFrame
{
	private final Noise2D noise;
	private final int xStart, xEnd;
	private final int yStart, yEnd;
	private final int windowWidth, windowHeight;
	private final double pixelScale;
	private final int width, height;
	private final BufferedImage img;
	
	public NoiseVisualizer(Noise2D noise, int xStart, int xEnd, int yStart, int yEnd, double pixelScale)
	{
		this.noise = noise;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.pixelScale = pixelScale;
		
		this.width = xEnd - xStart;
		this.height = yEnd - yStart;
		
		this.windowWidth = (int)(width * pixelScale);
		this.windowHeight = (int)(height * pixelScale);
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		generate();
		
		JPanel imgPanel = new JPanel()
		{
			@Override
			public void paint(Graphics g)
			{
				super.paint(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.scale(pixelScale, pixelScale);
				g2.drawImage(img, 0, 0, null);
			}
		};
		imgPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		add(imgPanel);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("NoiseVisualizer");
	}
	
	private void generate()
	{
		for (int y = yStart; y < yEnd; y++)
		{
			for (int x = xStart; x < xEnd; x++)
			{
				double v = noise.eval(x, y);
				v = Util.mapToRange(v, -1, 1, 0, 255);
				
				img.setRGB(x, y, new Color((int)v, (int)v, (int)v).getRGB());
			}
		}
	}
	
	public static NoiseVisualizerBuilder build(Noise2D noise)
	{
		return new NoiseVisualizerBuilder(noise);
	}
	
}
