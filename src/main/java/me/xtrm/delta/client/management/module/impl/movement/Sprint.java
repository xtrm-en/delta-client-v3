package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.loader.api.event.data.EventType;

public class Sprint extends Module {
	
	public Sprint() {
		super("Sprint", Category.MOVEMENT);
		setDescription("Sprint automatiquement");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		
		if(MovementUtils.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.setSprinting(true);
		}else {
			mc.thePlayer.setSprinting(false);
		}
	}

}
