package Imany.util.setting;

import java.util.Properties;
import javax.swing.*;
import Imany.util.Logger;
import Imany.util.Framework;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.File;
import java.net.URI;
import Imany.Imany;

public class Setting extends JFrame implements WindowListener
{
	Logger lgr;
	SwitchPanel sp;
	String[] classes;
	SettingPanel[] panels;
	JButton backTop = new JButton("Top");
	ColorPanel color_panel;
	AnalyzePanel analyze_panel;
	int now;
	Properties prop;
	File file;
	Imany imany;

	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;

	public static final int TYPE_NONE = 0;//Top
	public static final int TYPE_ANALYZE = 1;
	public static final int TYPE_COLOR = 2;
	public static final int TYPE_THEME = 3;
	public static final int TYPE_GUI = 4;
	public static final int TYPE_USER = 5;

	public static final int ALL_TYPE = 6;

	public Setting(Imany i)
	{
		imany = i;
		lgr = i.lgr;
		setTitle("Preference");
		setLayout(null);
		setResizable(false);
		setSize(WIDTH, HEIGHT);
		addWindowListener(this);

		Framework fw = new Framework();
		fw.setBounds(0,0,WIDTH,30);

		backTop.setBounds(WIDTH-60,0,60,30);
		add(backTop);
		add(fw);
        prop = new Properties();

        now = TYPE_NONE;

        try{
			String url = "file:"+System.getProperty("user.dir")+"/resources/setting.properties";
			URI uri = new URI(url);
			file = new File(uri);
			if(!file.exists()) file.createNewFile();
			InputStream inputStream = new FileInputStream(file);
        	prop.load(inputStream);
        	inputStream.close();
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		initClasses();
	}

	public void save(String key, String value){
		prop.setProperty(key, value);
	}

	private void initClasses()
	{
		classes = new String[3];
		classes[TYPE_NONE] = "Preference";
		//classes[TYPE_THEME] = "Theme";
		classes[TYPE_COLOR] = "Color";
		//classes[TYPE_GUI] = "GUI";
		//classes[TYPE_USER] = "User";
		classes[TYPE_ANALYZE] = "Analyze";

		panels = new SettingPanel[3];
		panels[TYPE_NONE] = new SettingPanel();
		//panels[TYPE_THEME] = null;
		panels[TYPE_COLOR] = (color_panel = new ColorPanel(this));
		//panels[TYPE_GUI] = null;
		//panels[TYPE_USER] = null;
		panels[TYPE_ANALYZE] = (analyze_panel = new AnalyzePanel(this));

		for (int i = 0; i < 3; i++) {
			add(panels[i]);
			panels[i].setVisible(false);
		}
	}

	public String get(String key)
	{
		return prop.getProperty(key);
	}

	public void setPage(int type)
	{
		if(type >= ALL_TYPE || type < 0) return;
		panels[now].setVisible(false);
		setTitle(classes[type]);
		panels[type].setVisible(true);
		now = type;
	}
	public void windowOpened(WindowEvent e){
    	//lgr.log("Window open");
  	}

  	public void windowClosing(WindowEvent e){
  		lgr.log("Window closing");
    	for (int i = 0; i < 3; i++) {
    		panels[i].allSave(this);
    	}
    	try{
    		prop.store(new FileOutputStream(file), "Do not change properties. It cannot read correctly.");
    	}catch(Exception ex){}
    	imany.stateChange();

  	}

  	public void windowClosed(WindowEvent e){
    	//lgr.log("Window closed");
  	}

  	public void windowIconified(WindowEvent e){
    	//lgr.log("Window Icon fied");
  	}

  	public void windowDeiconified(WindowEvent e){
    	//lgr.log("Window DeIcon fied");
  	}

  	public void windowActivated(WindowEvent e){
    	//lgr.log("Window Activated");
  	}

  	public void windowDeactivated(WindowEvent e){
    	//lgr.log("Window Dectivated");
  	}
}