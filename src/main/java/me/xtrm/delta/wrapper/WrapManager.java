package me.xtrm.delta.wrapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.management.module.impl.render.xray.XRayManager;
import me.xtrm.delta.factory.DeltaDisplayFactory;
import me.xtrm.delta.factory.DeltaEventFactory;
import me.xtrm.delta.factory.DeltaSplashFactory;

public enum WrapManager {
	INSTANCE;
	
	private final List<WrapData> wraps = new ArrayList<>();
	
	private WrapManager() {
		registerWrap(DeltaDisplayFactory.class);
		registerWrap(DeltaEventFactory.class);
		registerWrap(DeltaSplashFactory.class);
		registerWrap(XRayManager.class);
	}
	
	public void registerWrap(Class<?> clazz) {
		for(Method method : clazz.getMethods()) {
			Wrap wrap = null;
			if((wrap = method.getAnnotation(Wrap.class)) != null) {
				wraps.add(new WrapData(wrap.id(), clazz, method));
			}
		}
	}
	
	public WrapData get(String id) {
		return wraps.stream().filter(wd -> wd.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
	}
}
