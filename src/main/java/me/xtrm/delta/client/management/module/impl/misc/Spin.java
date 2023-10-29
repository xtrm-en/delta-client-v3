package me.xtrm.delta.client.management.module.impl.misc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import net.minecraft.util.MathHelper;

public class Spin extends Module {

	public Spin() {
		super("Spin", Category.MISC);
		setDescription("Beyblade beyblade hyper vitesse!");
		
		registerSettings(new Setting("ServerOnly", this, true));
	}
	
	private int spin;
	
	@Handler
	public void onUpdate(EventMotion e) {		
		spin += 10;
		if(spin > 360)
			spin = 0;
		
		e.setYaw(MathHelper.wrapAngleTo180_float(spin));
		e.setSilent(getSetting("ServerOnly").getCheckValue());
	}

}
