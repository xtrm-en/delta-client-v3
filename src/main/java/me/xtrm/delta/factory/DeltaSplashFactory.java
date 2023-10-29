package me.xtrm.delta.factory;

import java.util.HashMap;
import java.util.Map;

import me.xtrm.delta.wrapper.Wrap;

public class DeltaSplashFactory {
	
	private static Map<String, String> values;
	
	static {
		values = new HashMap<>();
		values.put("enabled", "true");
		values.put("rotate", "true");
		values.put("logoOffset", "10");
		values.put("background", "0xEEEEEE");
		values.put("barBackground", "0xEEEEEE");
		values.put("forgeTexture", "textures/gui/deltalogo2_icon.png");
	}
	
	@Wrap(id = "getString")
	public static String getString(String originalValue, String name) {
		if(values.containsKey(name)) {
			return values.get(name);
		}
		return originalValue;
	}

}
