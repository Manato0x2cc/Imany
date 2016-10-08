package Imany.util.algorithm;

import java.awt.image.BufferedImage;
import Imany.util.ImagePanel;
import java.awt.Graphics;
import java.awt.Color;

public class Affine extends Algorithm
{
	public static final int CENTERX = ImagePanel.WIDTH/2;
	public static final int CENTERY = ImagePanel.HEIGHT/2;
	//画像 回転するパネル 角度
	public static void algorithm(BufferedImage img, ImagePanel ip, double theta)
	{
		BufferedImage re = img;
		double rad = deg2rad(theta);
		int centerX = re.getWidth() / 2;
		int centerY = re.getHeight() / 2;
		if(ip.getGraphics() == null) return;
		Graphics g = ip.getGraphics();

		for (double x = 0d; x < re.getWidth(); x++) {
			for (double y = re.getHeight()-1; y >= 0; y--) {
				g.setColor(new Color(re.getRGB((int)x,(int)y)));
				g.fillRect(
					(int) (getX_inc(x-centerX,y-centerY,rad)+CENTERX),
				 	(int) (getY_inc(x-centerX,y-centerY,rad)+CENTERY),
				 	1,
				 	1);
			}
		}
		ip.affine(img, rad);
	}

	public static void drawAffineImage(Graphics g, BufferedImage img, double rad)
	{
		int centerX = img.getWidth() / 2;
		int centerY = img.getHeight() / 2;
		for (double x = 0d; x < img.getWidth(); x++) {
			for (double y = img.getHeight()-1; y >= 0; y--) {
				g.setColor(new Color(img.getRGB((int)x,(int)y)));
				g.fillRect(
					(int) (getX_inc(x-centerX,y-centerY,rad)+CENTERX),
				 	(int) (getY_inc(x-centerX,y-centerY,rad)+CENTERY),
				 	1,
				 	1);
			}
		}
	}

	//各座標のx増加量
	private static double getX_inc(double x, double y, double rad)
	{
		return (Math.cos(rad) * x) - (Math.sin(rad) * y);
	}

	private static double getY_inc(double x, double y, double rad)
	{
		return (Math.sin(rad)*x) + (Math.cos(rad)*y);
	}

	public static double deg2rad(double degrees)
	{
		return degrees * (Math.PI / 180f);
	}
}