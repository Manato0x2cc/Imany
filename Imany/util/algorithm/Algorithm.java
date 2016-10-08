package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import Imany.util.ImagePanel;

public class Algorithm
{
	public static final int GAMMA = 0;
	public static final int AFFINE = 1;
	private static final String[] strs =  {"ガンマ補正","アフィン変換","白黒","色反転","回転"};
	public static BufferedImage start(BufferedImage img, int type)
	{
		if(type == GAMMA)
		{
			BufferedImage re = Gamma.algorithm(img);
			return re;
		}
		return null;
	}

	//アフィン変換用
	public static void start(BufferedImage img, ImagePanel ip, double theta)
	{
		ip.affine(img, Affine.deg2rad(theta));
	}

	public static String[] getAllType()
	{
		return strs;
	}
}