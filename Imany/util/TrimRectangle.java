package Imany.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.Point;

public class TrimRectangle
{
	private Point firstPoint;
	private Point secondPoint;
	public static final int NONE = 0;
	public static final int LEFT_TOP = 1;
	public static final int LEFT_BOTTOM = 2;
	public static final int RIGHT_TOP = 3;
	public static final int RIGHT_BOTTOM = 4;

	public TrimRectangle(Point p)
	{
		firstPoint = p;
		secondPoint = p;
	}

	public void add(int x, int y)
	{
		secondPoint = new Point(firstPoint.x+x, firstPoint.y+y);
	}

	public void move(int addX, int addY, ImagePanel ip)
	{
		if(addX + getX() <= 0) addX = -getX();
		if(addX + getWidth() + getX() >= ip.getWidth()) addX = ip.getWidth() - (getWidth() + getX());
		if(addY + getY() <= 0) addY = -getY();
		if(addY + getHeight() + getY() >= ip.getHeight()) addY = ip.getHeight() - (getHeight() + getY());
		
		firstPoint = new Point(getX()+addX, getY()+addY);
		secondPoint = new Point(getX()+getWidth()+addX, getY()+getHeight()+addY);
	}

	public int getArea()
	{
		return getWidth() * getHeight();
	}

	public boolean isOnCorner(Point p)
	{
		if(isIn(p, getX()-8, getY()-7, 14,14))return true;
		if(isIn(p, getX()+getWidth()-7, getY()-7, 14,14))return true;
		if(isIn(p, getX()-7, getY()+getHeight()-7, 14,14))return true;
		if(isIn(p, getX()+getWidth()-7, getY()+getHeight()-7, 14,14))return true;

		return false;
	}

	public int whatCorner(Point p)
	{
		if(isIn(p, getX()-8, getY()-7, 14,14))return LEFT_TOP;
		if(isIn(p, getX()+getWidth()-7, getY()-7, 14,14))return RIGHT_TOP;
		if(isIn(p, getX()-7, getY()+getHeight()-7, 14,14))return LEFT_BOTTOM;
		if(isIn(p, getX()+getWidth()-7, getY()+getHeight()-7, 14,14))return RIGHT_BOTTOM;

		return NONE;
	}

	public void setFirstPoint(Point p, ImagePanel ip)
	{
		int x = p.x;
		int y = p.y;
		if(x <= 0) x = 0;
		if(x >= ip.getWidth()) x = ip.getWidth();
		if(y <= 0) y = 0;
		if(y >= ip.getHeight()) y = ip.getHeight();
		firstPoint = new Point(x, y);
	}

	public void setSecondPoint(Point p, ImagePanel ip)
	{
		int x = p.x;
		int y = p.y;
		if(x <= 0) x = 0;
		if(x >= ip.getWidth()) x = ip.getWidth();
		if(y <= 0) y = 0;
		if(y >= ip.getHeight()) y = ip.getHeight();
		secondPoint = new Point(x, y);
	}

	public int getX()
	{
		return Math.min(firstPoint.x, secondPoint.x);
	}

	public int getY()
	{
		return Math.min(firstPoint.y, secondPoint.y);
	}

	//左上
	public Point getFirstPoint()
	{
		return new Point(getX(), getY());
	}

	//右下
	public Point getSecondPoint()
	{
		return new Point(getX()+getWidth(), getY()+getHeight());
	}

	public int getWidth()
	{
		return Math.abs(firstPoint.x - secondPoint.x);
	}

	public int getHeight()
	{
		return Math.abs(firstPoint.y - secondPoint.y);
	}

	public boolean isIn(Point p)
	{
		if( getX() <= p.x && (getWidth()+getX()) >= p.x && 
			getY() <= p.y && (getHeight()+getY()) >= p.y)
		{
			return true;
		}else
		{
			return false;
		}
	}

	private boolean isIn(Point p, int sx, int sy, int width, int height)
	{
		if( sx <= p.x && (sx+width) >= p.x && 
			sy <= p.y && (sy+height) >= p.y)
		{
			return true;
		}else
		{
			return false;
		}
	}

	public void draw(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		float[] dash = {8.0f, 3.0f};
		BasicStroke dsahStroke = 
			new BasicStroke(
						2.0f, 
                        BasicStroke.CAP_BUTT, 
                        BasicStroke.JOIN_MITER, 
                        3.0f,
                        dash,
                        0.0f);
		g2.setStroke(dsahStroke);
		g2.setColor(new Color(255,255,255,0));
		g2.fillRect(getX(), getY(), getWidth(), getHeight());
		g2.setColor(Color.RED);
		g2.fillRect(getX()-6, getY()-6, 12,12);
		g2.setColor(Color.GREEN);
		g2.fillRect(getX()+getWidth()-6, getY()-6, 12,12);
		g2.setColor(Color.PINK);
		g2.fillRect(getX()-6, getY()+getHeight()-6, 12,12);
		g2.setColor(Color.YELLOW);
		g2.fillRect(getX()+getWidth()-6, getY()+getHeight()-6, 12,12);
	}
}