package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;

public class Jesus extends Module {
	
	public Jesus() {
		super("Jesus", Category.MOVEMENT);
		setDescription("Permet de marcher sur l'eau");
	}
	
	@Handler
	public void onUpdate(EventUpdate e) {
		if(mc.thePlayer.isInWater()) {
			mc.thePlayer.motionY = 0.05;
			mc.thePlayer.motionX *= 1.2;
			mc.thePlayer.motionZ *= 1.2;
			if(mc.thePlayer.isCollidedHorizontally) 
				mc.thePlayer.onGround = true;
		}
	}

}