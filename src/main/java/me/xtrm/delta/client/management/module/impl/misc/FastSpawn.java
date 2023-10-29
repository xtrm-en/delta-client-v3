package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class FastSpawn extends Module {
	
	public FastSpawn() {
		super("FastSpawn", Category.MISC);
		setDescription("Retourne au spawn instentanément");
	}
	
	@Override
	public void onEnable() {
		mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(1000000, 1000, 1000, 1000000, true));
		
		toggle();
		super.onEnable();
	}

}
