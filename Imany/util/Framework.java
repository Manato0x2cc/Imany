package Imany.util;

import javax.swing.JPanel;
import java.awt.Color;

public class Framework extends JPanel
{
	public Framework()
	{
		setBackground(new Color(70,178,109));
	}

	public Framework(Color c)
	{
		setBackground(c);
	}

	public void setColor(Color c)
	{
		setBackground(c);
		repaint();
	}
}