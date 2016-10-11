package Imany.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.net.URL;
import Imany.util.algorithm.Affine;
import Imany.util.algorithm.Gamma;

public class ImagePanel extends JPanel implements MouseMotionListener
{
	Image image;
	BufferedImage returnImg;
	Logger lgr;
	boolean isAffine = false;//アフィン変換された画像か
	double rad;
	TrimRectangle tr;
	boolean trimming = false;
	Point lastPoint = null;
	public static final int WIDTH = 439;
	public static final int HEIGHT = 390;
	public static final int MIN = 200;
	public ImagePanel(Logger l, boolean canTrim)
	{
		lgr = l;
		if(canTrim)
		{
			addMouseMotionListener(this);
			addMouseListener(new MyListener());
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(isAffine)
		{
			resizePanel(returnImg);
			Image tmp = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			setBounds(0,0,WIDTH,HEIGHT);//回転するから
			BufferedImage bimage = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    		// Draw the image on to the buffered image
    		Graphics2D bGr = bimage.createGraphics();
    		bGr.drawImage(tmp, 0, 0, null);
    		bGr.dispose();
			Affine.drawAffineImage(g,bimage,rad);
			return;
		}
		g.drawImage(image,0,0,getWidth(),getHeight(),this);

		if(tr instanceof TrimRectangle)
		{
			/*Gamma.changeGamma(0.3, false);
			ImageIcon icn = new ImageIcon(Gamma.algorithm(returnImg));
			image = icn.getImage();
			Gamma.changeGamma(Gamma.gamma);*/
			g.setColor(new Color(0,0,0,180));
			//g.fillRect(0,0,getWidth(), getHeight());
			g.fillRect(0,0,getWidth(),tr.getY());
			g.fillRect(0,tr.getY()+tr.getHeight(),getWidth(),getHeight()-(tr.getY()+tr.getHeight()));
			g.fillRect(0,tr.getY(),tr.getX(),tr.getHeight());

			g.fillRect(tr.getX()+tr.getWidth(),tr.getY(),getWidth()-(tr.getX()+tr.getWidth()),tr.getHeight());
			
			tr.draw(g);
		}
	}
	public void setImage(URL url)
	{
		try{
			trimming = false;
  			tr = null;
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
			trimming = false;
  			tr = null;
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

	public BufferedImage getImage(boolean f)
	{
		if(tr instanceof TrimRectangle  && f)
		{
			if(tr.getArea() != 0)
			{
				resizePanel(returnImg);
				Image tmp = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
				BufferedImage s = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D bGr =s.createGraphics();
    			bGr.drawImage(tmp, 0, 0, null);
    			bGr.dispose();

				BufferedImage r = new BufferedImage(tr.getWidth(), tr.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = r.createGraphics();
				for (int x = 0; x < tr.getWidth(); x++) {
					for (int y = 0; y < tr.getHeight(); y++) {
						g2.setColor(new Color(s.getRGB(x+tr.getX(), y+tr.getY())));
						g2.fillRect(x,y,1,1);
					}
				}
				return r;
			}
		}
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

	public void mouseDragged(MouseEvent e){
		if(getImage(false) == null) return;
		if(tr instanceof TrimRectangle)
		{
			if(tr.isIn(e.getPoint()) && !tr.isOnCorner(e.getPoint())){
				int distX = e.getPoint().x - lastPoint.x;
				int distY = e.getPoint().y - lastPoint.y;
				tr.move(distX, distY, this);
				lastPoint = e.getPoint();
				repaint();
				return;
			}
			if(!trimming)
			{
				switch(tr.whatCorner(e.getPoint()))
				{
					case TrimRectangle.LEFT_TOP://左上
						tr.setFirstPoint(e.getPoint(), this);
						break;
					case TrimRectangle.LEFT_BOTTOM://左下
						tr.setFirstPoint(new Point(e.getPoint().x, tr.getY()), this);
						tr.setSecondPoint(new Point(tr.getSecondPoint().x, e.getPoint().y), this);
						break;
					case TrimRectangle.RIGHT_TOP://右上
						tr.setFirstPoint(new Point(tr.getX(), e.getPoint().y), this);
						tr.setSecondPoint(new Point(e.getPoint().x, tr.getSecondPoint().y), this);
						break;
					case TrimRectangle.RIGHT_BOTTOM://右下
						tr.setSecondPoint(new Point(e.getPoint().x, e.getPoint().y), this);
						break;

					default:
						break;
				}
				repaint();
				return;
			}
			tr.setSecondPoint(new Point(e.getPoint().x, e.getPoint().y), this);
			repaint();
		}
  	}	

  	public void mouseMoved(MouseEvent e){
  	}	

  	private class MyListener extends MouseAdapter
  	{
  		public void mouseClicked(MouseEvent e)
  		{
  			lastPoint = e.getPoint();
  			if(tr instanceof TrimRectangle)
  			{
  				if(tr.isIn(e.getPoint()) && e.getClickCount() < 2) return;
  			}
  			tr = null;
  			ImageIcon icn = new ImageIcon(returnImg);
			image = icn.getImage();
  			repaint();
  		}

  		public void mouseReleased(MouseEvent e)
  		{
  			if(trimming) trimming = false;
  		}

  		public void mousePressed(MouseEvent e)
  		{
  			lastPoint = e.getPoint();
  			if(tr instanceof TrimRectangle)
  			{
  				if(tr.isIn(e.getPoint())) return;
  			}
  			trimming = true;
  			tr = new TrimRectangle(e.getPoint());
  		}
  	}
}