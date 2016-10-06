package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class Gamma extends Algorithm
{
	//ガンマ補正値のLUT
	private static int[] gamma_correction = {0,0,0,0,1,1,2,2,3,3,4,5,5,6,6,7,8,8,9,9,10,11,11,12,13,13,14,15,16,16,17,18,19,19,20,21,22,22,23,24,25,25,26,27,28,29,29,30,31,32,33,34,34,35,36,37,38,39,40,40,41,42,43,44,45,46,47,47,48,49,50,51,52,53,54,55,56,57,58,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,113,114,115,116,117,118,119,120,121,122,123,124,125,127,128,129,130,131,132,133,134,135,136,137,139,140,141,142,143,144,145,146,147,149,150,151,152,153,154,155,157,158,159,160,161,162,163,164,166,167,168,169,170,171,173,174,175,176,177,178,180,181,182,183,184,185,187,188,189,190,191,192,194,195,196,197,198,200,201,202,203,204,206,207,208,209,210,212,213,214,215,216,218,219,220,221,222,224,225,226,227,229,230,231,232,233,235,236,237,238,240,241,242,243,245,246,247,248,250,251,252,253,
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

	public static void changeGamma(double gamma)
	{
		if(gamma <= 0.0) return;// Division By Zero対策
		double a;
		double b;
		for (int i = 0; i < 256; i++) {
			a = i / 255d;
			b = Math.pow(a, (1/gamma));
			gamma_correction[i] = (int) (b * 255);
		}
	}
}