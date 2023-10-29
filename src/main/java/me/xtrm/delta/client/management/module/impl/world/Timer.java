package me.xtrm.delta.client.management.module.impl.world;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.PlayerUtils;
import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.loader.api.event.data.EventType;

public class Timer extends Module {

	public Timer() {
		super("Timer", Category.WORLD);
		setDescription("Accélère ou ralentit le jeu");
		
		registerSettings(new Setting("Timer", this, 1.25, 0.1F, 2, false));
	}

	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		float val = ((float)getSetting("Timer").getSliderValue());
		if(val < 0.1F) {
			val = 0.1F;
			getSetting("Timer").setSliderValue(0.1F);
			PlayerUtils.sendMessage("Timer débuggé.");
		}
		Wrapper.timer.timerSpeed = val;
	}
	
	@Override
	public void onDisable() {
		Wrapper.timer.timerSpeed = 1F;
		super.onDisable();
	}
	
}
