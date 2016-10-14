package Imany.util.algorithm;

import java.awt.image.BufferedImage;

public class Binarize extends Algorithm
{
	private static final int BLACK = 0;
	private static final int WHITE = 16777215;
	public static BufferedImage algorithm(BufferedImage img, boolean gaussian)
	{
		if(gaussian) img = Gaussian.algorithm(img, true);//Gaussian.algorithm
		BufferedImage re = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		re.setData(img.getData());
		int[] histogram = Histogram.histogram(img);
		int res = Integer.MIN_VALUE;
		int s = 0;
		for (int t = 2; t < 255; t++) {
			int res_b = 0;
			int count_b = 0;
			for (int i = 0; i < t; i++) {
				res_b += histogram[i];
			}
			int res_w = 0;
			int count_w = 0;
			for (int i = t; i < 255; i++) {
				res_w += histogram[i];
			}
			int m_b = 0;//平均
			int m_w = 0;//平均

			m_b = res_b / (t - 1);
			m_w = res_w / (255 - t);
			int res1 = res_b * res_w * (int) Math.pow(m_b - m_w, 2);
			if(res1 > res){
				s=t;
				res = res1;
			}
		}
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				if((img.getRGB(x,y) & 0xff) > s)
				{
					re.setRGB(x,y,WHITE);
				}else
				{
					re.setRGB(x,y,BLACK);
				}
			}
		}
		return re;
	}

	/*int m;//ヒストグラムの平均値
	int res = 0;//合計
			for (int i = 0; i < histogram.length; i++) {
				res += histogram[i];
			}
			int r = res / histogram.length;
			int dif = Integer.MAX_VALUE;
			for (int i = 0; i < histogram.length; i++) {	
				if(Math.abs(r - histogram[i]) < dif){
					m = i;
					dif = Math.abs(r - histogram[i]);
				}
			}
		int res = 0;//合計
		for (int i = 0; i < histogram.length; i++) {
			res += histogram[i];
		}
		int r = res / histogram.length;
		int dif = Integer.MAX_VALUE;
		for (int i = 0; i < histogram.length; i++) {
			if(Math.abs(r - histogram[i]) < dif){
				m = i;
				dif = Math.abs(r - histogram[i]);
			}
		}
		boolean check = true;
		int omega_b = 0;//黒側の画素数
		int m_b = 0;//平均
		int bunsan_b = 0;//分散
		int omega_w = 0;//白側の画素数
		int m_w = 0;//平均
		int bunsan_w = 0;//分散

		for (int i = 0; i < histogram.length; i++) {
			if(check)
				omega_b += histogram[i];
			else
				omega_w += histogram[i];

			if(i + 1 == m) check = false;
		}

		m_b = omega_b / (m - 1);
		m_w = omega_w / (255-m);
		int bunsan;
		for (int i = 0; i < m; i++) {
			bunsan += Math.pow(histogram[i] - m_b, 2);
		}
		bunsan_b = bunsan / (m - 1);
		bunsan = 0;
		for (int i = m; i <= 255; i++) {
			bunsan += Math.pow(histogram[i] - m_w, 2);
		}
		bunsan_w = bunsan / (255 - m);


		//クラス内分散
		double wc = */
}