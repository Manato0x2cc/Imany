package Imany.util.algorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BandW extends Algorithm
{
	public static BufferedImage algorithm(BufferedImage img)
	{
		BufferedImage re = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = img.getMinX(); x < img.getWidth(); x++) {
			for (int y = img.getMinY(); y < img.getHeight(); y++) {
				re.setRGB(x,y,toMono(img.getRGB(x,y)));
			}
		}
		return re;
	}

	private static int toMono(int i)
	{
		int b = i & 0xff;
		int g = (i & 0xff00) >> 8;
		int r = (i & 0xff0000) >> 16;
		int a = (i & 0xff000000) >>> 24;

		//NTSC係数による加重平均法
		int y = (int) (0.298912 * r + 0.586611 * g + 0.114478 * b);
		y = (y << 16) | (y << 8) | (y);
		
		return y;
	}
}