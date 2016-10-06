package Imany.util.setting;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
public class SettingPanel extends JPanel
{
	HashMap<String, String> saves;
	public SettingPanel(){
		setBounds(0,50,400,250);
		setLayout(null);
		saves = new HashMap<String, String>();
	}

	public void allSave(Setting s)
	{
		if(saves.isEmpty()) {
			return;
		}
		for(Map.Entry<String, String> e : saves.entrySet()) {
            s.save(e.getKey(), e.getValue());
        }
	}
}