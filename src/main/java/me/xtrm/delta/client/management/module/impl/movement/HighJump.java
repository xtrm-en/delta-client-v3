package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.player.EventJump;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;

public class HighJump extends Module {

	public HighJump() {
		super("HighJump", Category.MOVEMENT);
		setDescription("Fait sauter plus haut");
		
		registerSettings(new Setting("Motion", this, 2D, 0D, 5D, false));
	}
	
	@Handler
	public void onUpdate(EventJump e) {
		double motion = getSetting("Motion").getSliderValue();
		
		if(mc.thePlayer.onGround) {
			mc.thePlayer.motionY = motion;
		}
	}

}
