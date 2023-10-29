package me.xtrm.delta.client.management.module.impl.movement;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.Wrapper;

public class AirJump extends Module {

	public AirJump() {
		super("AirJump", Category.MOVEMENT);
		setDescription("Permet de sauter en l'air");
	}

	@Handler
	public void onUpdate(EventUpdate e) {		
		Wrapper.mc.thePlayer.onGround = true;
	}
	
}