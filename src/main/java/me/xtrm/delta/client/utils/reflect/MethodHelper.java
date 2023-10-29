package me.xtrm.delta.client.utils.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodHelper {
		
	private static Map<String, Method> cache = new HashMap<>();
	
	public static Method getMethod(Class<?> cla$$, String cacheName, Class<?> returnType, Class... args) {
		if(cache.containsKey(cacheName)) return cache.get(cacheName);
		
		for(Method met : cla$$.getDeclaredMethods()) {
			if(met.getReturnType() == returnType) {
				if(args != null && args.length != 0) {
					if(met.getParameterTypes().length == args.length) {
						boolean good = true;
						for(int i = 0; i < args.length; i++) {
							if(met.getParameterTypes()[i] != args[i]) {
								good = false;
							}
						}
						if(good) {
							cache.putIfAbsent(cacheName, met);
							try {
								Java9Fix.setAccessible(met);
							} catch (ReflectiveOperationException e) {
								e.printStackTrace();
							}
							return met;
						}
					}
				}else {
					cache.putIfAbsent(cacheName, met);
					try {
						Java9Fix.setAccessible(met);
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
					}
					return met;
				}
			}
		}
		return null;
	}

}
