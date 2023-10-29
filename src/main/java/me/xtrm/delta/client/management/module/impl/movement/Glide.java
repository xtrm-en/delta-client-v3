package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.loader.api.event.data.EventType;

public class Glide extends Module {

	public Glide() {
		super("Glide", Category.MOVEMENT);
		setDescription("Tombe lentement dans les airs");
		
		registerSettings(new Setting("Motion", this, 0.75, 0, 1, false));
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		if(mc.thePlayer.motionY < 0 && !mc.thePlayer.onGround)
			mc.thePlayer.motionY *= getSetting("Motion").getSliderValue();
	}

}
