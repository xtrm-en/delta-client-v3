package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class AutoSpawn extends Module {

	public AutoSpawn() {
		super("AutoSpawn", Category.MISC);
		setDescription("Retourne au spawn si la vie du joueur est basse");
		registerSettings(new Setting("Minimum Health", this, 4, 1, 20, true));
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(mc.thePlayer.getHealth() < getSetting("Minimum Health").getSliderValue()) {
			mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(1000000, 1000, 1000, 1000000, true));
			toggle();
		}
	}

}