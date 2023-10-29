package me.xtrm.delta.client.utils;

import me.xtrm.xeon.loader.api.XeonProvider;

public class MinecraftEnvironment {
	
	private static String envType;
	
	public static boolean isVanilla() {
		return !isPaladium() && !isForge();
	}
	
	public static boolean isForge() {
		try {
			Class.forName("cpw.mods.fml.common.launcher.FMLTweaker");
			return true;
		} catch(Exception e) { }
		return false;
	}
	
	public static boolean isPaladium() {
		boolean state = XeonProvider.getXeonLoader().isClassWrappingEnabled();
		XeonProvider.getXeonLoader().setClassWrapping(true);
		try {
			Class.forName("cpw.mods.fml.common.discovery.PalaDiscoverer", false, null);
			XeonProvider.getXeonLoader().setClassWrapping(state);
			return true;
		} catch(Exception e) { }
		XeonProvider.getXeonLoader().setClassWrapping(state);
		return false;
	}
	
	public static String getType() {
		if(isVanilla()) {
			envType = "V";
			System.out.println("env is V... wot?");
		}else {
			if(isForge()) {
				envType = "F";
				if(isPaladium()) {
					envType = envType + "-P";
				}
			}else {
				envType = "C-MCP";
				System.out.println("env is C-MCP... WHAT THE FUCK");
			}
		}
		return envType;
	}
}