package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.Location;
import me.xtrm.delta.client.utils.PlayerUtils;
import net.minecraft.util.EnumChatFormatting;

public class DeathCoordinates extends Module {

	public DeathCoordinates() {
		super("DeathCoords", Category.MISC);
		setDescription("Affiche vos coordonnées de mort dans le chat");
	}
	
	private boolean lastDeathState;
	
	@Handler
	public void onUpdate(EventMotion e) {
		boolean last = lastDeathState;
		lastDeathState = mc.thePlayer.isDead;
		
		if(last == lastDeathState) return;
		
		if(lastDeathState) {
			PlayerUtils.sendMessage("Vous êtes mort en: " + EnumChatFormatting.LIGHT_PURPLE + new Location(mc.thePlayer).getXYZ());
		}
	}

}
