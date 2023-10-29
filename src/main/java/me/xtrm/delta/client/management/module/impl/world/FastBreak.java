package me.xtrm.delta.client.management.module.impl.world;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FastBreak extends Module {

	public FastBreak() {
		super("FastBreak", Category.WORLD);
		setDescription("Permet de casser les blocks plus vite (Peut rollback)");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 840, 6));
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.removePotionEffectClient(Potion.digSpeed.id);
		super.onDisable();
	}

}