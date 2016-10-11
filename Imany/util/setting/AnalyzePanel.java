package Imany.util.setting;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import Imany.util.ImagePanel;

public class AnalyzePanel extends SettingPanel implements ChangeListener
{
	private static final double DEFAULT = 1.5;//緑
	private JSpinner spinner;
	private double now;
	private ImagePanel imagePanel;
	public AnalyzePanel(Setting s)
	{
		if(s.get("gamma") == null) now = DEFAULT;
		else now = (double) Double.valueOf(s.get("gamma"));
		JLabel gamma_analyze = new JLabel("ガンマ補正値");
		SpinnerNumberModel model = new SpinnerNumberModel(1.5d, 0.0d, 5.0d, 0.1d);
		spinner = new JSpinner(model);
		spinner.addChangeListener(this);
		gamma_analyze.setBounds(20,20,100,30);
		spinner.setBounds(140,20,50,30);
		add(gamma_analyze);
		add(spinner);
		
		saves.put("gamma", String.valueOf(now));
	}

	public void stateChanged(ChangeEvent e){
		saves.replace("gamma", String.valueOf(spinner.getValue()));
	}
}