package Imany.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.net.URL;

public class ImagePanel extends JPanel
{
	Image image;
	BufferedImage returnImg;
	Logger lgr;
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
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
	public void setImage(URL url)
	{
		try{
			BufferedImage im = ImageIO.read(url);
			returnImg = im;
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
			
			ImageIcon icn = new ImageIcon(im);
			image = icn.getImage();
			locateTo(getWidth(), getHeight());
			repaint();
		}catch(Exception e){
			lgr.warn(e.getMessage());
		}
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