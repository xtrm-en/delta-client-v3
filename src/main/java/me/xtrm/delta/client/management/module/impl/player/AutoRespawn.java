package me.xtrm.delta.client.management.module.impl.player;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;

public class AutoRespawn extends Module {

	public AutoRespawn() {
		super("AutoRespawn", Category.PLAYER);
		setDescription("Respawn automatiquement");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		if(mc.thePlayer.isDead)
			mc.thePlayer.respawnPlayer();
	}

}