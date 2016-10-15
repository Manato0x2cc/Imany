package Imany.util.algorithm;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URI;
public class Histogram extends Algorithm
{
	private static final int SIZE = 524;
	public static BufferedImage algorithm(BufferedImage img)
	{
		try{
			String url = "file:"+System.getProperty("user.dir")+"/hist";
			URI uri = new URI(url);
			File file = new File(uri);
			file.mkdir();
			BufferedImage re = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
			int gMax = 256;//量子化ビット数
			int[] histogram_b = new int[gMax];//青
			int[] histogram_g = new int[gMax];//緑
			int[] histogram_r = new int[gMax];//赤
			int[] histogram_gray = new int[gMax];//グレースケール化
			BufferedImage s = Grayscale.algorithm(img);
			int rgb;
			for(int y = 0; y < img.getHeight(); y++){
    			for(int x = 0; x < img.getWidth(); x++){
    				rgb = img.getRGB(x, y);
        			histogram_b[rgb & 0xff]++;
        			histogram_g[(rgb & 0xff00) >> 8]++;
        			histogram_r[(rgb & 0xff0000) >> 16]++;
       		 		histogram_gray[s.getRGB(x,y) & 0xff]++;
        		}
        	}

        	double[] normHist_b = new double[gMax];
        	double[] normHist_g = new double[gMax];
        	double[] normHist_r = new double[gMax];
        	double[] normHist_gray = new double[gMax];

			int min_b = Integer.MAX_VALUE;
			int min_g = Integer.MAX_VALUE;
			int min_r = Integer.MAX_VALUE;
			int min_gray = Integer.MAX_VALUE;
			int max_b = Integer.MIN_VALUE;
			int max_g = Integer.MIN_VALUE;
			int max_r = Integer.MIN_VALUE;
			int max_gray = Integer.MIN_VALUE;

			for(int i = 0; i < gMax; i++){//縦軸の正規化
    			min_b = Math.min(min_b, histogram_b[i]);
    			max_b = Math.max(max_b, histogram_b[i]);
    			min_g = Math.min(min_g, histogram_g[i]);
    			max_g = Math.max(max_g, histogram_g[i]);
    			min_r = Math.min(min_r, histogram_r[i]);
    			max_r = Math.max(max_r, histogram_r[i]);
    			min_gray = Math.min(min_gray, histogram_gray[i]);
    			max_gray = Math.max(max_gray, histogram_gray[i]);
			}
			for(int i = 0; i < gMax; i++){
	    		normHist_b[i] = (double) (histogram_b[i] - min_b) / (max_b - min_b);
	    		normHist_g[i] = (double) (histogram_g[i] - min_g) / (max_g - min_g);
	    		normHist_r[i] = (double) (histogram_r[i] - min_r) / (max_r - min_r);
	    		normHist_gray[i] = (double) (histogram_gray[i] - min_gray) / (max_gray - min_gray);	
			}

			Graphics2D g2 = re.createGraphics();
			g2.setColor(Color.WHITE);
			g2.fillRect(0,0,SIZE,SIZE);
			int bin = 1;	

			for (int i = 0; i < gMax; i++) {
				int x = i;
				int y_b = (int) (normHist_b[i] * (SIZE/2) + 0.5);
				int y_g = (int) (normHist_g[i] * (SIZE/2) + 0.5);
				int y_r = (int) (normHist_r[i] * (SIZE/2) + 0.5);
				int y_gray = (int) (normHist_gray[i] * (SIZE/2) + 0.5);
				g2.setColor(Color.BLUE);
				g2.fillRect(x,SIZE/2 - 1 - y_b,bin,y_b);
				g2.setColor(Color.GREEN);
				g2.fillRect(x+SIZE/2,SIZE/2 - 1 - y_g,bin,y_g);
				g2.setColor(Color.RED);
				g2.fillRect(x,SIZE - 1 - y_r,bin,y_r);
				g2.setColor(Color.BLACK);
				g2.fillRect(x+SIZE/2,SIZE - 1 - y_gray,bin,y_gray);
			}
			/*url = "file:"+System.getProperty("user.dir")+"/hist/hist.png";
			uri = new URI(url);
			file = new File(uri);
			ImageIO.write(re, "png", file);*/
			return re;
		}catch(Exception e)
		{

		}
		return null;
	}

	public static BufferedImage only_black(BufferedImage img)
	{
		int gMax = 256;//量子化ビット数
		int[] histogram = new int[gMax];
		BufferedImage re = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		BufferedImage s = Grayscale.algorithm(img);
		for(int y = 0; y < img.getHeight(); y++){
    		for(int x = 0; x < img.getWidth(); x++){
       			histogram[s.getRGB(x,y) & 0xff]++;
        	}
        }
        double[] normHist = new double[gMax];
        int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < gMax; i++){//縦軸の正規化
    		min = Math.min(min, histogram[i]);
    		max = Math.max(max, histogram[i]);
		}
		for(int i = 0; i < gMax; i++){
	    	normHist[i] = (double) (histogram[i] - min) / (max - min);
		}
		Graphics2D g2 = re.createGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0,SIZE,SIZE);
		int bin = 2;
		for (int i = 0; i < gMax; i++) {
			int x = i * bin;
			int y = (int) (normHist[i] * SIZE + 0.5);
			g2.setColor(Color.BLACK);
			g2.fillRect(x, SIZE - 1 - y,bin,y);
		}
		return re;
	}

	//グレースケール化しておく
	public static int[] histogram(BufferedImage img)
	{
		int gMax = 256;//量子化ビット数
		int[] histogram = new int[gMax];
		BufferedImage re = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		for(int y = 0; y < img.getHeight(); y++){
    		for(int x = 0; x < img.getWidth(); x++){
       			histogram[img.getRGB(x,y) & 0xff]++;
        	}
        }
        return histogram;
	}
}