package Imany.util.algorithm;

import java.awt.image.BufferedImage;

public class Gaussian extends Algorithm
{
	private static final double NEPIER = 2.7182;
	private static final double SIGMA = 2.7182;
	//5*5のカーネル
	private static final double[][] GAUSSIAN_5 = 
			{
				{1/256.0,  4/256.0,  6/256.0,  4/256.0, 1/256.0},
				{4/256.0, 16/256.0, 24/256.0, 16/256.0, 4/256.0},
				{6/256.0, 24/256.0, 36/256.0, 24/256.0, 6/256.0},
				{4/256.0, 16/256.0, 24/256.0, 16/256.0, 4/256.0},
				{1/256.0,  4/256.0,  6/256.0,  4/256.0, 1/256.0}
			};
	//3*3のカーネル
	private static final double[][] GAUSSIAN_3 = 
			{
				{1/16, 2/16, 1/16},
				{2/16, 4/16, 2/16},
				{1/16, 2/16, 1/16}
			};
	public static BufferedImage algorithm(BufferedImage img, boolean grayscale)
	{
		if(grayscale) img = Grayscale.algorithm(img);
		BufferedImage re = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		re.setData(img.getData());
		for (int x = 1; x < img.getWidth() - 2; x++) {
			for (int y = 1; y < img.getHeight() - 2; y++) {
				if(x == 1 || x == img.getWidth() -2)
				{
					re.setRGB(x, y, toGaussian(img, x, y, GAUSSIAN_3, 1));
					continue;
				}
				if(y == 1 || y == img.getHeight())
				{
					re.setRGB(x, y, toGaussian(img, x, y, GAUSSIAN_3, 1));
					continue;
				}

				re.setRGB(x, y, toGaussian(img, x, y, GAUSSIAN_5, 2));
			}
		}
		return re;
	}

	private static int toGaussian(BufferedImage img, int x, int y, double[][] filter, int count)
	{
		double r = 0.0, g = 0.0, b = 0.0;
		int rgb = 0;
		for (int i = -count; i <= count; i++) {
			for (int j = -count; j <= count; j++) {
				int rgb1 = img.getRGB(x+i, y+j);
				b += (rgb1 & 0xff) * filter[i+count][j+count];
				g += ((rgb1 & 0xff00) >> 8) * filter[i+count][j+count];
				r += ((rgb1 & 0xff0000) >> 16) * filter[i+count][j+count];
			}
		}
		rgb = ((int) r << 16) | ((int) g << 8) | ((int)b);
		return rgb;
	}
}