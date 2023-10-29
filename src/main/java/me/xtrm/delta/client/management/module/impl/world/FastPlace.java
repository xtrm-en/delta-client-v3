package me.xtrm.delta.client.management.module.impl.world;

import java.lang.reflect.Field;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.Minecraft;

public class FastPlace extends Module {

	public FastPlace() {
		super("FastPlace", Category.WORLD);
		setDescription("Permet de poser les blocks plus vite");
	}
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		try {
			Field f = Minecraft.getMinecraft().getClass().getDeclaredField("rightClickDelayTimer");
			f.setAccessible(true);
			f.set(Minecraft.getMinecraft(), 0);
		} catch(Exception ex) {
			try {
				Field f = Minecraft.getMinecraft().getClass().getDeclaredField("field_71467_ac");
				f.setAccessible(true);
				f.set(Minecraft.getMinecraft(), 0);
			} catch(Exception exx) {
				ex.printStackTrace();
				exx.printStackTrace();
			}
		}
	}

}