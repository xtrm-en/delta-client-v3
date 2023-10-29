package me.xtrm.delta.client.utils.render;

import me.xtrm.delta.client.utils.Wrapper;
import net.minecraft.client.gui.ScaledResolution;

public class ScaledUtils {

	public static ScaledResolution gen() {
		return new ScaledResolution(Wrapper.mc, Wrapper.mc.displayWidth, Wrapper.mc.displayHeight);
	}
	
}
