package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {

	public Fullbright() {
		super("Fullbright", Category.RENDER);
		setDescription("Permet de voir dans le noir");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.id, 860, 10));
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.removePotionEffectClient(Potion.nightVision.id);
		super.onDisable();
	}

}
