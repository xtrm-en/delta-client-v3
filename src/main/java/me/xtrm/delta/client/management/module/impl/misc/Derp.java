package me.xtrm.delta.client.management.module.impl.misc;

import java.util.Random;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;

public class Derp extends Module {

	public Derp() {
		super("Derp", Category.MISC);
		setDescription("\"jme sens pas bien\"");
		
		registerSettings(new Setting("ServerOnly", this, true));
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		Random r = new Random();
		e.setYaw(r.nextInt(360) - 180);
		e.setPitch(r.nextInt(180) - 90);
		
		e.setSilent(getSetting("ServerOnly").getCheckValue());
	}


}
