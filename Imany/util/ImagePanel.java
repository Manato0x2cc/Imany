package Imany.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.net.URL;
import Imany.util.algorithm.Affine;

public class ImagePanel extends JPanel
{
	Image image;
	BufferedImage returnImg;
	Logger lgr;
	boolean isAffine = false;//アフィン変換された画像か
	double rad;
	public static final int WIDTH = 439;
	public static final int HEIGHT = 390;
	public static final int MIN = 200;
	public ImagePanel(Logger l)
	{
		lgr = l;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(isAffine)
		{
			resizePanel(returnImg);
			Image tmp = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			setBounds(0,0,WIDTH,HEIGHT);
			BufferedImage bimage = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    		// Draw the image on to the buffered image
    		Graphics2D bGr = bimage.createGraphics();
    		bGr.drawImage(tmp, 0, 0, null);
    		bGr.dispose();
			Affine.drawAffineImage(g,bimage,rad);
			return;
		}
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
	}
	public void setImage(URL url)
	{
		try{
			BufferedImage im = ImageIO.read(url);
			returnImg = im;
			isAffine=false;
			resizePanel(im);
			
			ImageIcon icn = new ImageIcon(im);
			image = icn.getImage();
			locateTo(getWidth(), getHeight());
			repaint();
		}catch(Exception e){
			lgr.warn(e.getMessage());
		}
	}

	public void setImage(BufferedImage im)
	{
		try{
			returnImg = im;
			isAffine=false;
			resizePanel(im);
			
			ImageIcon icn = new ImageIcon(im);
			image = icn.getImage();
			locateTo(getWidth(), getHeight());
			repaint();
		}catch(Exception e){
			lgr.warn(e.getMessage());
		}
	}

	private void resizePanel(BufferedImage im)
	{
		int im_h = im.getHeight();
		int im_w = im.getWidth();
		if(im_h >= im_w && im_h > HEIGHT)
		{
			setSize(equalRate(im_w, im_h, HEIGHT), HEIGHT);
		}else if(im_w >= im_h && im_w > WIDTH)
		{
			setSize(WIDTH, equalRate(im_h, im_w, WIDTH));
		}else if(max(im_w, im_h) <= MIN)
		{
			if(im_h >= im_w){
				setSize(equalRate_MIN(im_w, im_h, MIN), MIN);
			}else{
				setSize(MIN, equalRate_MIN(im_h, im_w, MIN));
			}
		}else setSize(im_w, im_h);
	}

	public void affine(BufferedImage img, double rad)
	{
		isAffine = true;
		this.rad = rad;
		returnImg = img;
		image = (Image) returnImg;
		setBounds(0,0,WIDTH,HEIGHT);
		repaint();
	}

	public void locateTo(int width, int height)
	{
		setBounds((WIDTH-width)/2,(HEIGHT-height)/2, getWidth(), getHeight());
	}

	public BufferedImage getImage()
	{
		return returnImg;
	}

	public int equalRate_MIN(int a, int b, int c)
	{
		return a*c/b;
	}

	public int equalRate(int a, int b, int c)
	{
		return a*c/b;
	}


	public int max(int a, int b)
	{
		if(a >= b)
		{
			return a;
		}else {
			return b;
		}
	}
}