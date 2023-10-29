package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;

public class Step extends Module {

	public Step() {
		super("Step", Category.MOVEMENT);
		setDescription("Monte automatiquement les blocks");
		
		registerSettings(new Setting("Height", this, 5, 1, 10, true));
	}

	@Handler
	public void onUpdate(EventMotion e) {
		mc.thePlayer.stepHeight = (float)getSetting("Height").getSliderValue();
	}
	
	@Handler
	public void onUpdate(EventUpdate e) {
		mc.thePlayer.stepHeight = (float)getSetting("Height").getSliderValue();
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.stepHeight = 0.6f;
	}
	
}
