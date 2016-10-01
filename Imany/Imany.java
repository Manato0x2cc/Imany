package Imany;

import javax.swing.*;
import Imany.util.*;
import Imany.util.algorithm.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import java.awt.Color;
import java.util.HashMap;

public class Imany extends JFrame implements ActionListener{
	private ImagePanel ip;
	private ImagePanel after;
	private JComboBox<String> type;
	private HashMap<String,Integer> hm = new HashMap<String, Integer>();
	private Logger lgr;
	public static void main(String[] arg)
	{
		Imany im = new Imany();
		im.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×で終了
		im.setVisible(true);//可視化
	}

	public Imany()
	{
		initHashMap();
		setTitle("Imany");
		setResizable(false);
		setSize(1000,650);
		setLocationRelativeTo(null);//中央
		setLayout(null);

		JLabel logo = new JLabel(new ImageIcon(getURL("img/logo.png")));
		logo.setBounds(0,0,170,60);

		Framework fw1 = new Framework();//左
		Framework fw2 = new Framework();//上
		fw2.setLayout(null);
		fw2.add(logo);
		fw2.setColor(new Color(70,180,109));
		Framework fw3 = new Framework();
		Framework fw4 = new Framework();//下
		Framework fw5 = new Framework();//logと区切り
		Framework fw6 = new Framework();//画像を分ける

		fw1.setBounds(0,60,50,560);
		fw3.setBounds(950,60,50,560);
		fw2.setBounds(0,0,1000,60);
		fw4.setBounds(0,600,1000,50);
		fw5.setBounds(50,450,900,20);
		fw6.setBounds(489,50,21,400);

		add(fw1);
		add(fw2);
		add(fw3);
		add(fw4);
		add(fw5);
		add(fw6);

		lgr = new Logger();
		lgr.setBounds(50,470,900,130);
		add(lgr);

		JPanel panel = new JPanel();
		panel.setBounds(50,60,439,390);
		panel.setBackground(new Color(255,255,255));
		panel.setLayout(null);
		add(panel);

		JPanel panel2 = new JPanel();
		panel2.setBounds(510,60,439,390);
		panel2.setBackground(new Color(255,255,255));
		panel2.setLayout(null);
		add(panel2);

		ip = new ImagePanel(lgr);
		ip.setBounds(45,20,350,350);
		panel.add(ip);

		after = new ImagePanel(lgr);
		after.setBounds(45,20,350,350);
		panel2.add(after);

		JButton open = new JButton("参照");
		open.addActionListener(this);
		open.setActionCommand("open");
		open.setBounds(340,0,100,20);

		JButton algo = new JButton("変換");
		algo.addActionListener(this);
		algo.setActionCommand("algorithm");
		algo.setBounds(700,0,100,20);

		JButton save = new JButton("Save");
		save.addActionListener(this);
		save.setActionCommand("save");
		save.setBounds(800,0,100,20);

		type = new JComboBox<String>(Algorithm.getAllType());
		type.setBounds(500,0,170,20);
		fw5.setLayout(null);
		fw5.add(open);
		fw5.add(algo);
		fw5.add(save);
		fw5.add(type);
	}

	private void initHashMap()
	{
		hm.put("ガンマ補正", Algorithm.GAMMA);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("open"))
		{
			JFileChooser fc = new JFileChooser();//ファイル開く奴〜wwwwww
			FileFilter filter = new FileNameExtensionFilter("画像ファイル", ImageIO.getReaderFileSuffixes());
			fc.setFileFilter(filter);

			int selected = fc.showOpenDialog(this);

    		if (selected == JFileChooser.APPROVE_OPTION){//開いたとき
      			try{
      				File file = fc.getSelectedFile();
      				ip.setImage(file.toURI().toURL());
      			}catch(Exception ex)
      			{
      				lgr.warn(ex.getMessage());
      			}
			}
    	}

    	if(e.getActionCommand().equals("algorithm"))
    	{
    		if(type.getSelectedIndex() == -1) return;
    		if(ip.getImage() == null) return;
    		String selected = (String) type.getSelectedItem();
    		after.setImage(Algorithm.start(ip.getImage(), new Integer(hm.get(selected))));
    		lgr.log("Algorithm was success!");
    	}

    	if(e.getActionCommand().equals("save"))
    	{
    		if(after.getImage() == null) return;
    		JFileChooser fc = new JFileChooser();//ファイル開く奴〜wwwwww
    		fc.setSelectedFile(new File("imany.png"));
    		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int selected = fc.showSaveDialog(this);
    		if (selected == JFileChooser.APPROVE_OPTION){//開いたとき
      			try{
      				File file = fc.getSelectedFile();
      				if(file.isDirectory())
      				{
      					file = new File(file.getAbsolutePath().toString()+file.getName());
      				}
      				save(file);
      			}catch(Exception ex)
      			{
      				lgr.warn(ex.getMessage());
      			}
			}
    	}
	}

	public URL getURL(String path)
	{
		return getClass().getResource(path);
	}

	public void save(File file)
	{
		try{
			ImageIO.write(after.getImage(), "png", file);
		}catch(Exception e)
		{
			lgr.warn(e.getMessage());
		}
	}
}