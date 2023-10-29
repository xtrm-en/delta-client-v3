package me.xtrm.delta.client.management.module.impl.player;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", Category.PLAYER);
		setDescription("Permet de manger plus rapidement");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;		
		if(mc.thePlayer.onGround) 
			if(mc.thePlayer.isEating() && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() instanceof ItemFood && mc.thePlayer.fallDistance < 3F) 
				for(int i = 0; i < 10; i++)
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	}

}
