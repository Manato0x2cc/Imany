package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import Imany.util.ImagePanel;

public class Algorithm
{
	public static final int GAMMA = 0;
	public static final int AFFINE = 1;
	public static final int BLACK_AND_WHITE = 2;
	public static final int GAUSSIAN = 3;
	public static final int HIST = 4;
	private static final String[] strs =  {"ガンマ補正","アフィン変換","白黒","ガウシアンフィルタ","濃度ヒストグラム","色反転"};
	public static BufferedImage start(BufferedImage img, int type)
	{
		if(type == GAMMA)
		{
			BufferedImage re = Gamma.algorithm(img);
			return re;
		}else if(type == BLACK_AND_WHITE)
		{
			BufferedImage re = BandW.algorithm(img);
			return re;
		}else if(type == GAUSSIAN)
		{
			BufferedImage re = Gaussian.algorithm(img);
			return re;
		}else if(type == HIST)
		{
			BufferedImage re = Histogram.algorithm(img);
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