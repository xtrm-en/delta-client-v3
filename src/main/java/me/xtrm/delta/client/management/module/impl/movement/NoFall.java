package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;

public class NoFall extends Module {
	
	public NoFall() {
		super("NoFall", Category.MOVEMENT);
		setDescription("Permet de ne pas prendre de dégats de chute");
	}

	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		if(mc.thePlayer.fallDistance >= 2F)
			e.setOnGround(true);
	}
	
}
