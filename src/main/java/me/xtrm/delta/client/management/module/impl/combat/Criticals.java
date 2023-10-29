package me.xtrm.delta.client.management.module.impl.combat;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.network.EventPacket;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.PlayerUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {

	private final double[] vals = new double[] { 0.05, 0, 0.012511, 0 };
	private TimeHelper timer = new TimeHelper();
	
	public Criticals() {
		super("Criticals", Category.COMBAT);
	}
	
	@Handler
	public void onPacket(EventPacket event) {
		if(event.getType() == EventType.RECIEVE) return;
		
		Packet p = event.getPacket();
		if(p instanceof C02PacketUseEntity) {
			C02PacketUseEntity pc02 = (C02PacketUseEntity)p;
			if(pc02.func_149565_c() == Action.ATTACK) {
				PlayerUtils.sendMessage("CRIT");
				doCrit();
			}
		}
	}
	
	void doCrit() {
		if(!canCrit()) return;
		if(!timer.hasReached(200)) return;
		timer.reset();
		
		for(double offset : vals) {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY + offset, mc.thePlayer.posY + offset, mc.thePlayer.posZ, true));
		}
	}

	private boolean canCrit() {
        return mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !DeltaAPI.getClient().getModuleManager().getModule("Speed").isEnabled();
    }
}
