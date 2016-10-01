package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class Algorithm
{
	public static final int GAMMA = 0;
	private static final String[] strs =  {"ガンマ補正","フーリエ変換","白黒","色反転","回転"};
	public static BufferedImage start(BufferedImage img, int type)
	{
		if(type == GAMMA)
		{
			BufferedImage re = Gamma.algorithm(img);
			return re;
		}
		return null;
	}

	public static String[] getAllType()
	{
		return strs;
	}
}