package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.loader.api.event.data.EventType;

public class AntiAFK extends Module {
	
	public AntiAFK() {
		super("AntiAFK", Category.MISC);
		setDescription("Permet de rester AFK et de ne pas se faire kick");
	}
	
	private int tick = 0;
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		tick++;
		if(tick > 100) {
			tick = 0;
			mc.thePlayer.rotationYaw -= 90;
			MovementUtils.setSpeed(0.5);
			if(mc.thePlayer.onGround)
				mc.thePlayer.jump();
		}
	}

}