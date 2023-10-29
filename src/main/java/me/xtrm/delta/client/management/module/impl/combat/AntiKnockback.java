package me.xtrm.delta.client.management.module.impl.combat;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.network.EventPacket;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class AntiKnockback extends Module {

	public AntiKnockback() {
		super("AntiKnockback", Category.COMBAT);
		setDescription("Annule le Knockback");
	}
	
	@Handler
	public void onPacket(EventPacket e) {
		if(e.getType() != EventType.RECIEVE) return;
		
		if(e.getPacket() instanceof S12PacketEntityVelocity) {
			S12PacketEntityVelocity p = (S12PacketEntityVelocity)e.getPacket();
			
			if(mc.theWorld == null) return;
			if(mc.thePlayer == null) return;
			if(p.func_149412_c() == -1) return;
			
			if(mc.theWorld.getEntityByID(p.func_149412_c()) == mc.thePlayer) {
				e.setCancelled(true);
			}
		}
	}

}
