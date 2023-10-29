package me.xtrm.delta.client.management.setting;

import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.api.setting.ISettingManager;

public class SettingManager implements ISettingManager {

	private List<ISetting> settings;
	
	public SettingManager() {
		this.settings = new ArrayList<ISetting>();
	}

	@Override
	public List<ISetting> getSettings() {
		return settings;
	}	

}