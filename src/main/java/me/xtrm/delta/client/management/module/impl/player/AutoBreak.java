package me.xtrm.delta.client.management.module.impl.player;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import cpw.mods.fml.relauncher.ReflectionHelper;
import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.settings.KeyBinding;

public class AutoBreak extends Module {
	
	public AutoBreak() {
		super("AutoBreak", Category.PLAYER);
		setDescription("Presse toujours clique gauche pour casser automatiquement");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		ReflectionHelper.setPrivateValue(KeyBinding.class, mc.gameSettings.keyBindAttack, true, "pressed", "field_74513_e");
	}
	
	@Override
	public void onDisable() {
		ReflectionHelper.setPrivateValue(KeyBinding.class, mc.gameSettings.keyBindAttack, false, "pressed", "field_74513_e");
		super.onDisable();
	}

}
