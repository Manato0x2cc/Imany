package Imany.util.algorithm;

import java.awt.image.BufferedImage;

public class Grayscale extends Algorithm
{
	public static BufferedImage algorithm(BufferedImage img)
	{
		BufferedImage re = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		re.setData(img.getData());
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				int re_rgb = (
					((rgb & 0xff0000) >> 16) + 
					((rgb & 0xff00) >> 8) +
					(rgb & 0xff)
					) / 3;
				re_rgb = (re_rgb << 16) | (re_rgb << 8) | (re_rgb);
				re.setRGB(x, y, re_rgb);
			}
		}
		return re;
	}
}