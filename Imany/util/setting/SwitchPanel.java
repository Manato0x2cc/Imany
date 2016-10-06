package Imany.util.setting;

import java.util.ArrayList;

public class SwitchPanel
{
	private ArrayList<SettingPanel> ps = new ArrayList<SettingPanel>();
	private int now = 0;//現在のページ

	public SwitchPanel()
	{
		
	}

	public void add(SettingPanel panel)
	{
		ps.add(panel);
	}

	public SettingPanel get(int id)
	{
		if (id < 0 || id >= ps.size()) {
			return next();
		}
		now = id;
		return ps.get(id);
	}

	public SettingPanel next()
	{
		now++;
		if(now >= ps.size())
		{
			now = 0;
		}
		return ps.get(now);
	}

	public SettingPanel prev()
	{
		now--;
		if(now < 0)
		{
			now = ps.size()-1;
		}
		return ps.get(now);
	}
}