package me.xtrm.xeon.loader.classloading.transform;

import java.util.Map;

import me.xtrm.delta.client.utils.reflect.ReflectedClass;
import me.xtrm.xeon.loader.api.XeonProvider;
import me.xtrm.xeon.loader.classloading.XeonClassLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;

public class XeonHandler {

	public static void handle(Minecraft mc, CrashReport cr) {
		System.out.println("Dumping XCL cache:");
		((XeonClassLoader)XeonProvider.getXeonLoader().getXCL()).cachedClasses.values().forEach(System.out::println);
		
		System.out.println("Dumping LCL cache:");
		try {
			((Map<String, Class<?>>)ReflectedClass.of(XeonProvider.getXeonLoader().getLCL()).get0("cachedClasses")).values().forEach(System.out::println);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
}
