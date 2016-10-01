package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class Gamma extends Algorithm
{
	//ガンマ補正値のLUT
	private static int[] gamma_correction = {0,6,10,13,15,18,20,23,25,27,29,31,33,35,36,38,40,41,43,45,46,48,49,51,52,54,55,57,58,59,61,62,63,65,66,67,69,70,71,72,74,75,76,77,79,80,81,82,83,84,86,87,88,89,90,91,92,93,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,132,133,134,135,136,137,138,139,140,141,142,142,143,144,145,146,147,148,149,149,150,151,152,153,154,155,155,156,157,158,159,160,161,161,162,163,164,165,166,166,167,168,169,170,170,171,172,173,174,175,175,176,177,178,179,179,180,181,182,182,183,184,185,186,186,187,188,189,189,190,191,192,193,193,194,195,196,196,197,198,199,199,200,201,202,202,203,204,205,205,206,207,208,208,209,210,211,211,212,213,213,214,215,216,216,217,218,219,219,220,221,221,222,223,224,224,225,226,226,227,228,228,229,230,231,231,232,233,233,234,235,235,236,237,238,238,239,240,240,241,242,242,243,244,244,245,246,246,247,248,248,249,250,250,251,252,252,253,254,
		255
			};

	public static BufferedImage algorithm(BufferedImage img)
	{
		BufferedImage re = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = img.getMinX(); x < img.getWidth(); x++) {
			for (int y = img.getMinY(); y < img.getHeight(); y++) {
				re.setRGB(x,y,toGamma(img.getRGB(x,y)));
			}
		}
		return re;
	}

	private static int toGamma(int i)
	{
		int b = gamma_correction[i & 0xff];
		int g = gamma_correction[(i & 0xff00) >> 8];
		int r = gamma_correction[(i & 0xff0000) >> 16];
		int a = (i & 0xff000000) >>> 24;

		int rgba = (a << 24) | (r << 16) | (g << 8) | (b);
		return rgba;
	}
}