package Imany;

import javax.swing.*;
import Imany.util.*;
import Imany.util.algorithm.*;
import Imany.util.setting.Setting;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Robot;
import java.io.File;
import javax.imageio.ImageIO;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import java.util.HashMap;

public class Imany extends JFrame implements ActionListener, MouseListener{
	private ImagePanel ip;
	private ImagePanel after;
	private JComboBox<String> type;
	private HashMap<String,Integer> hm = new HashMap<String, Integer>();
	public Logger lgr;
	private Setting s;
	private Framework[] fws = new Framework[6];
	private boolean gamma_flag;
	private int[] gamma_correction;
	private int affine = 0;
	public static void main(String[] arg)
	{
		if (getOS() == "MAC"){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		Imany im = new Imany();
		im.setLocationRelativeTo(null);//中央
		im.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×で終了
		im.setVisible(true);//可視化
	}

	public Imany()
	{
		initHashMap();
		setTitle("Imany");
		setResizable(false);
		setSize(1000,650);
		setLayout(null);
		addMouseListener(this);
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenuItem mnu1_1 = new JMenuItem("Open");
					mnu1_1.addActionListener(this);
					mnu1_1.setActionCommand("open");
		JMenuItem mnu1_2 = new JMenuItem("Save");
					mnu1_2.addActionListener(this);
					mnu1_2.setActionCommand("Save");
		JMenuItem mnu1_3 = new JMenuItem("Save As");
					mnu1_3.addActionListener(this);
					mnu1_3.setActionCommand("SaveAs");
		JMenuItem mnu1_4 = new JMenuItem("New Window");
					mnu1_4.addActionListener(this);
					mnu1_4.setActionCommand("NewWindow");
		menu1.add(mnu1_1);
		menu1.add(mnu1_2);
		menu1.add(mnu1_3);
		menu1.addSeparator();
		menu1.add(mnu1_4);

		JMenu menu2 = new JMenu("View");
		JMenuItem mnu2_1 = new JMenuItem("Theme");
					mnu2_1.addActionListener(this);
					mnu2_1.setActionCommand("theme");
		JMenuItem mnu2_2 = new JMenuItem("Color");
					mnu2_2.addActionListener(this);
					mnu2_2.setActionCommand("color");
		menu2.add(mnu2_1);
		menu2.addSeparator();
		menu2.add(mnu2_2);
		JMenu menu3 = new JMenu("Preference");
		JMenuItem mnu3_1 = new JMenuItem("Analyze");
					mnu3_1.addActionListener(this);
					mnu3_1.setActionCommand("analyze");
		menu3.add(mnu3_1);
		JMenu menu4 = new JMenu("Help");
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		menubar.add(menu4);
		setJMenuBar(menubar);

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

		fws[0] = fw1;
		fws[1] = fw2;
		fws[2] = fw3;
		fws[3] = fw4;
		fws[4] = fw5;
		fws[5] = fw6;

		add(fw1);
		add(fw2);
		add(fw3);
		add(fw4);
		add(fw5);
		add(fw6);

		JScrollPane spane= new JScrollPane();
		spane.setBounds(50,470,900,130);

		lgr = new Logger();
		lgr.setBounds(0,0,900,130);
		
		spane.setViewportView(lgr);
		add(spane);

		new File(System.getProperty("user.dir")+"/resources").mkdir();//Make Folder

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

		JButton algo = new JButton("変換");
		algo.addActionListener(this);
		algo.setActionCommand("algorithm");
		algo.setBounds(800,0,100,20);

		type = new JComboBox<String>(Algorithm.getAllType());
		type.setBounds(500,0,170,20);
		fw5.setLayout(null);
		fw5.add(algo);
		fw5.add(type);

		s = new Setting(this);
		stateChange();//初期化
	}

//  値の変更が記録されたとき。SettingのWindowClosingで呼ばれる
	public void stateChange()
	{
		for (int i = 0; i < fws.length; i++) {
			fws[i].setColor(new Color((int) Integer.valueOf(s.get("color"))));
		}

		Gamma.changeGamma((double) Double.valueOf(s.get("gamma")));
	}

	private void initHashMap()
	{
		hm.put("ガンマ補正", Algorithm.GAMMA);
		hm.put("アフィン変換", Algorithm.AFFINE);
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
      				lgr.log("An Image Loaded!");
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
    		if(new Integer(hm.get(selected)) == Algorithm.AFFINE){
    			Algorithm.start(ip.getImage(), after, affine+=10);
    			after.repaint();
    			lgr.log("Algorithm was success!");
    			return;
    		}
    		after.setImage(Algorithm.start(ip.getImage(), new Integer(hm.get(selected))));
    		lgr.log("Algorithm was success!");
    	}

    	if(e.getActionCommand().equals("Save") || e.getActionCommand().equals("SaveAs"))
    	{
    		if(after.getImage() == null) return;
    		JFileChooser fc = new JFileChooser();//ファイル開く奴〜wwwwww
    		FileFilter filter = new FileNameExtensionFilter("画像ファイル", ImageIO.getReaderFileSuffixes());
			fc.setFileFilter(filter);
    		if(e.getActionCommand().equals("SaveAs")) fc.setSelectedFile(new File("Unknown.png"));
			int selected = fc.showSaveDialog(this);
    		if (selected == JFileChooser.APPROVE_OPTION){//開いたとき
      			try{
      				File file = fc.getSelectedFile();
      				if(file.isDirectory())
      				{
      					file = new File(file.getAbsolutePath().toString()+file.getName());
      				}
      				save(file);
      				lgr.log("Saved file as "+file.getName());
      			}catch(Exception ex)
      			{
      				lgr.warn(ex.getMessage());
      			}
			}
    	}

    	if(e.getActionCommand().equals("NewWindow"))
    	{
    		Imany i = new Imany();
    		i.setVisible(true);
    	}

    	if(e.getActionCommand().equals("color"))
    	{
    		s.setPage(Setting.TYPE_COLOR);
    		s.setVisible(true);
    	}

    	if(e.getActionCommand().equals("analyze"))
    	{
    		s.setPage(Setting.TYPE_ANALYZE);
    		s.setVisible(true);
    	}
	}

	public void mouseEntered(MouseEvent e){
  	}

  	public void mouseExited(MouseEvent e){
  	}

  	public void mousePressed(MouseEvent e){
    }

  	public void mouseReleased(MouseEvent e){
  	}

  	public void mouseClicked(MouseEvent e){
  		
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

	private static String getOS() {
    	String os = System.getProperty("os.name").toLowerCase();

    	if(os.indexOf("mac") >= 0){
       		return "MAC";
    	}
    	else if(os.indexOf("win") >= 0){
       		return "WIN";
    	}
    	else if(os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0){
       		return "LINUX/UNIX";
    	}
    	else if(os.indexOf("sunos") >= 0){
       		return "SOLARIS";
    	}else
    	{
    		return "UNKNOWN";
    	}
    }
}