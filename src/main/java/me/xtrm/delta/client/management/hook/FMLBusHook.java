package me.xtrm.delta.client.management.hook;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.reflect.TypeToken;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.IEventListener;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.xtrm.delta.client.utils.EncryptionHelper;
import me.xtrm.delta.client.utils.MinecraftEnvironment;
import me.xtrm.delta.client.utils.reflect.ReflectedClass;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

public enum FMLBusHook {
	INSTANCE;
	
	private List<Object> registerCache;
	
	private FMLBusHook() {
		this.registerCache = new ArrayList<>();
	}
	
	public void silentRegister(Object o) {
		registerCache.add(o);
		try {
			registerOnBus(FMLCommonHandler.instance().bus(), o);
			registerOnBus(MinecraftForge.EVENT_BUS, o);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void registerOnBus(EventBus bus, Object o) throws ReflectiveOperationException {
		ReflectedClass rc_bus = ReflectedClass.of(bus);
		ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>)rc_bus.get0("listeners");
		Map<Object,ModContainer> listenerOwners = (Map<Object,ModContainer>)rc_bus.get0("listenerOwners");
		
		if(listeners.containsKey(o)) return;
		
		ModContainer activeContainer = Loader.instance().getMinecraftModContainer();
		listenerOwners.put(o, activeContainer);
		
		rc_bus.set0("listenerOwners", listenerOwners);
		
		Set<? extends Class<?>> supers = TypeToken.of(o.getClass()).getTypes().rawTypes();
        for (Method method : o.getClass().getMethods()) {
            for (Class<?> cls : supers) {
                try {
                    Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                    if (real.isAnnotationPresent(SubscribeEvent.class)) {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        if (parameterTypes.length != 1) 
                            throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");

                        Class<?> eventType = parameterTypes[0];

                        if (!Event.class.isAssignableFrom(eventType)) 
                            throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + eventType);

                        Method m = rc_bus.getHandle().getClass().getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
                        m.setAccessible(true);
                        m.invoke(rc_bus.getHandle(), eventType, o, method, activeContainer);
                        
                        break;
                    }
                } catch (NoSuchMethodException e) { ; }
            }
        }
	}
	
	public void unhookPalalol(EventBus bus) throws ReflectiveOperationException {
		if(!MinecraftEnvironment.isPaladium()) return;
		
		ReflectedClass rc_bus = ReflectedClass.of(bus);
		ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>)rc_bus.get0("listeners");

		for(Object object : listeners.keySet()) {
			String name = object.getClass().getName().toLowerCase();
			if(name.contains(EncryptionHelper.getPName())) {
				for(Method m : object.getClass().getDeclaredMethods()) {				
					if(m.getParameterCount() == 1) { 
						if(m.getParameterTypes()[0].equals(GuiOpenEvent.class)) {
							bus.unregister(object);
						}
					}
				}
			}
		}
	}
	
	public void unhookAll() {
		for(Object o : this.registerCache) {
			FMLCommonHandler.instance().bus().unregister(o);
			MinecraftForge.EVENT_BUS.unregister(o);
		}
	}

}
