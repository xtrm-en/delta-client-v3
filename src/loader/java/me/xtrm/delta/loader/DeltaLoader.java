package me.xtrm.delta.loader;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.hippo.api.lwjeb.annotation.Handler;
import me.hippo.api.lwjeb.annotation.Wrapped;
import me.hippo.api.lwjeb.bus.PubSub;
import me.hippo.api.lwjeb.configuration.BusConfigurations;
import me.hippo.api.lwjeb.configuration.config.impl.BusPubSubConfiguration;
import me.hippo.api.lwjeb.message.scan.impl.MethodAndFieldBasedMessageScanner;
import me.hippo.api.lwjeb.wrapped.WrappedType;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.loader.api.IDeltaLoader;
import me.xtrm.delta.loader.api.event.data.IEvent;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLLoad;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPostLoading;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPreLoading;
import me.xtrm.delta.loader.api.library.ILibraryManager;
import me.xtrm.delta.loader.api.plugin.IPluginManager;
import me.xtrm.delta.loader.event.bus.message.publish.impl.DeltaMessagePublisher;
import me.xtrm.delta.loader.library.LibraryManager;
import me.xtrm.delta.loader.plugin.PluginManager;
import me.xtrm.delta.loader.transform.impl.GuiIngameForgeHook;
import me.xtrm.delta.loader.transform.impl.LoadControllerHook;
import me.xtrm.delta.loader.transform.impl.LoaderHook;
import me.xtrm.delta.loader.transform.impl.MinecraftHook;
import me.xtrm.delta.loader.utils.IOUtils;
import me.xtrm.xeon.loader.api.transform.ITransformer;

public class DeltaLoader implements IDeltaLoader {

	private Logger logger;
	
	private PubSub<IEvent> pubSub;
	private ILibraryManager libraryManager;
	private IPluginManager pluginManager;
	
	@Override
	public void initialize() {
		this.logger = LogManager.getLogger("DeltaLoader");
		
		this.pubSub = new PubSub<>(new BusConfigurations.Builder().setConfiguration(
                BusPubSubConfiguration.class, () -> {
                    BusPubSubConfiguration busPubSubConfiguration = BusPubSubConfiguration.getDefault();
                    busPubSubConfiguration.setScanner(new MethodAndFieldBasedMessageScanner<>());
                    busPubSubConfiguration.setPublisher(new DeltaMessagePublisher<>());
                    return busPubSubConfiguration;
                }
        ).build());
		
		this.libraryManager = new LibraryManager();
		this.libraryManager.init(); // load the core libraries
		
		this.pluginManager = new PluginManager();
		this.pluginManager.init(IOUtils.getPluginsDir()); // load the plugins
		
		this.pubSub.subscribe(this);
		this.pubSub.subscribe(DeltaAPI.getClient());
		this.pluginManager.getLoadedPlugins().keySet().forEach(this.pubSub::subscribe);
	}
	
	@Wrapped({EventFMLLoad.class, EventMCPreLoading.class, EventMCPostLoading.class})
	@Handler
	public void onEvent(WrappedType wrapped) {
		System.out.println(wrapped.raw());
	}
	
	@Override
	public PubSub<IEvent> getEventManager() {
		return pubSub;
	}

	@Override
	public ILibraryManager getLibraryManager() {
		return libraryManager;
	}

	@Override
	public IPluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public List<ITransformer> getTransformers() {
		return Arrays.asList(
				new GuiIngameForgeHook(),
				new LoadControllerHook(),
				new LoaderHook(),
				new MinecraftHook()
		);
	}

}
