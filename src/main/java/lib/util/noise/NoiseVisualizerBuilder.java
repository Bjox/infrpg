package lib.util.noise;


public class NoiseVisualizerBuilder
{
	private final Noise2D noise;
	private int xStart = 0;
	private int xEnd = 200;
	private int yStart = 0;
	private int yEnd = 200;
	private double pixelScale = 2;

	public NoiseVisualizerBuilder(Noise2D noise)
	{
		this.noise = noise;
	}
	
	public NoiseVisualizerBuilder setWidth(int width)
	{
		this.xEnd = xStart + width;
		return this;
	}
	
	public NoiseVisualizerBuilder setHeight(int height)
	{
		this.yEnd = yStart + height;
		return this;
	}

	public NoiseVisualizerBuilder setXStart(int xStart)
	{
		this.xStart = xStart;
		return this;
	}

	public NoiseVisualizerBuilder setXEnd(int xEnd)
	{
		this.xEnd = xEnd;
		return this;
	}

	public NoiseVisualizerBuilder setYStart(int yStart)
	{
		this.yStart = yStart;
		return this;
	}

	public NoiseVisualizerBuilder setYEnd(int yEnd)
	{
		this.yEnd = yEnd;
		return this;
	}

	public NoiseVisualizerBuilder setPixelScale(double pixelScale)
	{
		this.pixelScale = pixelScale;
		return this;
	}

	public NoiseVisualizer createNoiseVisualizer()
	{
		return new NoiseVisualizer(noise, xStart, xEnd, yStart, yEnd, pixelScale);
	}
	
}
