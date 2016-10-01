package Imany.util;

import javax.swing.JTextArea;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger extends JTextArea
{
	public Logger()
	{
		setLineWrap(true);
		setEditable(false);
		log("Imany is loaded");
		log("This Application is built By Manato0x2cc(manato0x2cc@gmail.com)");
	}

	public void log(String str)
	{
		setForeground(Color.BLACK);
		append("["+getTime()+"/INFO] "+str+"\n");
	}

	public void warn(String str)
	{
		setForeground(Color.RED);
		append("["+getTime()+"/WARN] "+str+"\n");
	}

	public String getTime()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		return sdf.format(date);
	}
}