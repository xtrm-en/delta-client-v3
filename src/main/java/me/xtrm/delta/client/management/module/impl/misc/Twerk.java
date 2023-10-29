package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import cpw.mods.fml.relauncher.ReflectionHelper;
import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.settings.KeyBinding;

public class Twerk extends Module {

	public Twerk() {
		super("Twerk", Category.MISC);
		setDescription("... me demandez pas pourquoi c'est là");
	}
	
	@Handler
	public void onMotion(EventMotion e) {
		if(e.getType() != EventType.POST) return;
		
		ReflectionHelper.setPrivateValue(KeyBinding.class, mc.gameSettings.keyBindSneak, !mc.gameSettings.keyBindSneak.getIsKeyPressed(), "pressed", "field_74513_e");
	}

}
