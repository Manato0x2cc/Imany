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
	private ImagePanel ip;//左側のイメージパネル
	private ImagePanel after;//右側のイメージパネル
	private JComboBox<String> type;//画像処理の種類一覧
	private HashMap<String,Integer> hm = new HashMap<String, Integer>();
	//文字列をAlgorithm.javaで定義されてる定数に変換
	public Logger lgr;
	//Loggerクラス
	private Setting s;
	//設定画面
	private Framework[] fws = new Framework[6];
	//周りの枠
	private int affine = 0;
	//角度(一回ごとに+10)

	//mainメソッド
	public static void main(String[] arg)
	{
		if (getOS() == "MAC"){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			//上のツールバーにメニューを表示
		}
		Imany im = new Imany();
		im.setLocationRelativeTo(null);//中央
		im.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//×で終了
		im.setVisible(true);//可視化
	}

	public Imany()
	{
		initHashMap();//ハッシュマップを初期化
		setTitle("Imany");//タイトルを設定
		setResizable(false);//リサイズ不可
		setSize(1000,650);//サイズ
		setLayout(null);
		addMouseListener(this);//マウスリスナーを追加

		//メニュー
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

		//ロゴ
		JLabel logo = new JLabel(new ImageIcon(getURL("img/logo.png")));
		logo.setBounds(0,0,170,60);

		//周りの枠
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

		//ログ
		JScrollPane spane= new JScrollPane();
		spane.setBounds(50,470,900,130);

		lgr = new Logger();
		lgr.setBounds(0,0,900,130);
		
		spane.setViewportView(lgr);
		add(spane);

		new File(System.getProperty("user.dir")+"/resources").mkdir();//Make Folder

		//イメージパネルの後ろ(グレーだと見栄えが....)
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

		//イメージパネル
		ip = new ImagePanel(lgr, true);
		ip.setBounds(45,20,350,350);
		panel.add(ip);

		//変換後の画像を表示
		after = new ImagePanel(lgr, false);
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
		hm.put("白黒", Algorithm.BLACK_AND_WHITE);
		hm.put("ガウシアンフィルタ", Algorithm.GAUSSIAN);
		hm.put("濃度ヒストグラム", Algorithm.HIST);
		hm.put("2値化", Algorithm.BINARIZE);
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
    		if(ip.getImage(false) == null) return;
    		String selected = (String) type.getSelectedItem();

    		if(new Integer(hm.get(selected)) == Algorithm.AFFINE){//アフィンだと処理が少し違う
    			Algorithm.start(ip.getImage(true), after, affine+=10);
    			after.repaint();
    			lgr.log("Algorithm was success!");
    			return;
    		}

    		after.setImage(Algorithm.start(ip.getImage(true), new Integer(hm.get(selected))));
    		lgr.log("Algorithm was success!");
    	}

    	if(e.getActionCommand().equals("Save") || e.getActionCommand().equals("SaveAs"))
    	{
    		if(after.getImage(false) == null) return;
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
    	//新しい画面
    	if(e.getActionCommand().equals("NewWindow"))
    	{
    		Imany i = new Imany();
    		i.setVisible(true);
    	}
    	//色
    	if(e.getActionCommand().equals("color"))
    	{
    		s.setPage(Setting.TYPE_COLOR);
    		s.setVisible(true);
    	}
    	//ガンマ値の設定など
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
			ImageIO.write(after.getImage(false), getSuffix(file), file);
		}catch(Exception e)
		{
			lgr.warn(e.getMessage());
		}
	}

	public static String getSuffix(File path) {
    	if (path.isDirectory()) {
        	return null;
    	}
 
    	String fileName = path.getName();
 
    	int lastDotPosition = fileName.lastIndexOf(".");
    	if (lastDotPosition != -1) {
        	return fileName.substring(lastDotPosition + 1);
    	}
    	return null;
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