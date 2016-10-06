package Imany.util.setting;

import Imany.util.Framework;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import javax.swing.plaf.basic.BasicSliderUI;

public class ColorPanel extends SettingPanel implements ChangeListener
{
	private static final int DEFAULT = 4633197;//緑
	private JSlider red;
	private JSlider green;
	private JSlider blue;
	private Framework fw;//色表示
	private int now;

	public ColorPanel(Setting s)
	{
		if(s.get("color") == null) now = DEFAULT;
		else now = (int) Integer.valueOf(s.get("color"));
		int[] rgb = toRGB(now);
		red = new JSlider(JSlider.HORIZONTAL, 0, 255, rgb[0]);
			red.setUI(new MySlider(red, Color.RED));
			red.addChangeListener(this);
		green = new JSlider(JSlider.HORIZONTAL, 0, 255, rgb[1]);
			green.setUI(new MySlider(green, Color.GREEN));
			green.addChangeListener(this);
		blue = new JSlider(JSlider.HORIZONTAL, 0, 255, rgb[2]);
			blue.setUI(new MySlider(blue, Color.BLUE));
			blue.addChangeListener(this);

		fw = new Framework(new Color(rgb[0], rgb[1], rgb[2]));
		fw.setBounds(20,50,100,100);

		red.setBounds(140, 40, 200, 30);
		blue.setBounds(140, 80, 200, 30);
		green.setBounds(140, 120, 200, 30);

		add(fw);
		add(red);
		add(blue);
		add(green);

		saves.put("color", String.valueOf(now));
	}

	public int toBit(int r, int g, int b)
	{
		return (r << 16) | (g << 8) | (b);
	}

	public int toBit()
	{
		return (red.getValue() << 16) | (green.getValue() << 8) | (blue.getValue());
	}

	public int[] toRGB(int rgb)
	{
		int b = rgb & 0xff;
		int g = (rgb & 0xff00) >> 8;
		int r = (rgb & 0xff0000) >> 16;

		int[] r_g_b = new int[3];
		r_g_b[0] = r;
		r_g_b[1] = g;
		r_g_b[2] = b;

		return r_g_b;
	}

	public void stateChanged(ChangeEvent e){
		fw.setColor(new Color(now = toBit()));
		saves.replace("color", String.valueOf(now));
	}
	private class MySlider extends BasicSliderUI
	{
		private Color color;
		public MySlider(JSlider sl, Color c)
		{
			super(sl);
			color = c;
		}

		@Override
    	public void paintTrack(Graphics g) {
        	Graphics2D g2d = (Graphics2D) g;
        	Rectangle t = trackRect;//グラフ
        	g2d.setColor(color);
        	g2d.fillRect(t.x, t.y, t.width, t.height);//描画
    	}
}
}