package me.xtrm.delta.client.management.module.impl.world;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.Location;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class Nuker extends Module {

	public Nuker() {
		super("Nuker", Category.WORLD);
		setDescription("Casse tout les blocks aux alentours (Requiert l'outil approprié)");
		
		registerSettings(new Setting("Radius", this, 3D, 1D, 5D, true));
	}

	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		int radius = (int)getSetting("Radius").getSliderValue();
		
		for(int x = -radius; x<radius; x++) {
			for(int y = -radius; y<radius; y++) {
				for(int z = -radius; z<radius; z++) {
					Location loc = new Location(mc.thePlayer).offset(x, y, z);
					Block b = mc.theWorld.getBlock((int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
					if(b != null && !b.getMaterial().isLiquid() && Block.getIdFromBlock(b) != 0 && b != Blocks.bedrock) {
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(0, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
						if(!mc.thePlayer.capabilities.isCreativeMode) {
							mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(2, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
						}
					}
				}
			}
		}
	}
	
}
