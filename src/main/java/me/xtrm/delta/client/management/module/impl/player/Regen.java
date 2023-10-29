package me.xtrm.delta.client.management.module.impl.player;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {

	public Regen() {
		super("Regen", Category.PLAYER);
		setDescription("Permet de régénerer sa vie plus rapidement");
		
		registerSettings(
				new Setting("WhileMoving", this, true),
				new Setting("Frequence", this, 50, 1, 100, true)
		);
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		if(mc.thePlayer.onGround) {
			if(!getSetting("WhileMoving").getCheckValue() && MovementUtils.isMoving())
				return;
			
			if(mc.thePlayer.getHealth() < 20)
				for(int i = 0; i < ((int)getSetting("Frequence").getSliderValue()); i++) 
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}
	}

}
