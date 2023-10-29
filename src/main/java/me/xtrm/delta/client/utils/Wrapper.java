package me.xtrm.delta.client.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;

public class Wrapper {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean atlasMode = false;
	
	public static void exitGame() {
		FMLCommonHandler.instance().exitJava(0, true);
	}
}