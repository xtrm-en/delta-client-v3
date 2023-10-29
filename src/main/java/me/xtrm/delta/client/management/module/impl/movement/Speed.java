package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import org.lwjgl.input.Keyboard;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.loader.api.event.data.EventType;

public class Speed extends Module {
	
	public Speed() {
		super("Speed", Keyboard.KEY_M, Category.MOVEMENT);
		setDescription("Zooooooooommmmmmmmmmmmmm");
		
		registerSettings(
				new Setting("Speed", this, 0.45D, 0, 5, false),
				new Setting("BunnyHop", this, true)
		);
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		MovementUtils.setSpeed(0);
		if(MovementUtils.isMoving())
			MovementUtils.setSpeed(getSetting("Speed").getSliderValue());
		
		if(getSetting("BunnyHop").getCheckValue())
			if(mc.thePlayer.onGround && MovementUtils.isMoving())
				mc.thePlayer.jump();
	}
}