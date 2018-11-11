package experimental.mapgen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import lib.util.noise.OpenSimplexNoise;

public class BiomeTerrain
{
	static final int windowWidth = 900;
	static final int windowHeight = 900;
	static final double pixelScale = 1.0;
	
	static final int width = (int)(windowWidth / pixelScale);
	static final int height = (int)(windowHeight / pixelScale);
	
	static int iterations = 8;
	static double persistence = 0.5;
	static double scale = 0.015;
	static double waterLevel = 125;
	static double sandFactor = 1.07;
	static double grassFactor = 1.43;
	
	static int biomeIterations = 1;
	static double biomeScale = 0.008;
	static double biomePersistence = 0.0;
	
	static OpenSimplexNoise simplexNoise = new OpenSimplexNoise(Double.doubleToLongBits(Math.random()));
	static OpenSimplexNoise biomeNoise = new OpenSimplexNoise(Double.doubleToLongBits(Math.random()));
	static JFrame frame = new JFrame();
	
	public static void main(String[] args) throws Exception
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Box content = new Box(BoxLayout.Y_AXIS);
		frame.add(content);
		
		JPanel imgPanel = new JPanel()
		{
			@Override
			public void paint(Graphics g)
			{
				Graphics2D g2 = (Graphics2D)g;
				g2.scale(pixelScale, pixelScale);
				g2.drawImage(img, 0, 0, null);
			}
		};
		imgPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		content.add(imgPanel);
		
		JSlider persistenceSlider = new JSlider(0, 100);
		persistenceSlider.addChangeListener(l -> {
			double v = persistenceSlider.getValue() / 100.0;
			persistence = v;
			System.out.println("persistence " + persistence);
			generate(img);
		});
		content.add(persistenceSlider);
		
		JSlider scaleSlider = new JSlider(0, 100);
		scaleSlider.addChangeListener(l -> {
			double v = scaleSlider.getValue() * 0.1 / 100.0;
			scale = v;
			System.out.println("scale " + scale);
			generate(img);
		});
		content.add(scaleSlider);
		
		JSlider waterLevelSlider = new JSlider(0, 255);
		waterLevelSlider.addChangeListener(l -> {
			waterLevel = waterLevelSlider.getValue();
			System.out.println("water level " + waterLevel);
			generate(img);
		});
		content.add(waterLevelSlider);
		
		JSlider sandLevelSlider = new JSlider(100, 200);
		sandLevelSlider.addChangeListener(l -> {
			sandFactor = sandLevelSlider.getValue() / 100.0;
			System.out.println("sandFactor " + sandFactor);
			generate(img);
		});
		content.add(sandLevelSlider);
		
//		JSlider grassLevelSlider = new JSlider(100, 200);
//		grassLevelSlider.addChangeListener(l -> {
//			grassFactor = grassLevelSlider.getValue() / 100.0;
//			System.out.println("grassFactor " + grassFactor);
//			generate(img);
//		});
//		content.add(grassLevelSlider);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		generate(img);
	}
	
	static final Color BROWN = new Color(140, 68, 0);
	private static void generate(BufferedImage img)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				double biome = sumOctave(biomeNoise, biomeIterations, x, y, biomePersistence, biomeScale, 0, 1);
				double h = sumOctave(simplexNoise, iterations, x, y, persistence, scale, 0, 255) * biome;
				
				if (h < 0) h = 0;
				if (h > 255) h = 255;
				
//				if (h < waterLevel) img.setRGB(x, y, Color.BLUE.getRGB());
//				else if (h < waterLevel * sandFactor) img.setRGB(x, y, Color.ORANGE.getRGB());
//				else img.setRGB(x, y, Color.GREEN.darker().getRGB());
				
				img.setRGB(x, y, new Color((int)h, (int)h, (int)h).getRGB());
			}
		}
		
		frame.repaint();
	}

	private static double sumOctave(
		OpenSimplexNoise simplexNoise,
		int iterations,
		double x,
		double y,
		double persistence,
		double scale,
		double low,
		double high)
	{
		double ampSum = 0;
		double amp = 1;
		double freq = scale;
		double noise = 0;

		for (int i = 0; i < iterations; i++)
		{
			noise += simplexNoise.eval(x * freq, y * freq) * amp;
			ampSum += amp;
			amp *= persistence;
			freq *= 2;
		}

		noise /= ampSum;
		noise = noise * (high - low) * 0.5 + (high + low) * 0.5;

		return noise;
	}

}
