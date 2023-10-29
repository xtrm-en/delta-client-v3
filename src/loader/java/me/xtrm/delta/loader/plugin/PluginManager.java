package me.xtrm.delta.loader.plugin;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.loader.api.event.data.IEvent;
import me.xtrm.delta.loader.api.event.events.plugin.EventPluginLoad;
import me.xtrm.delta.loader.api.plugin.IPluginManager;
import me.xtrm.delta.loader.api.plugin.exception.PluginLoadingException;
import me.xtrm.delta.loader.api.plugin.types.IPlugin;
import me.xtrm.delta.loader.api.plugin.types.PluginInfo;
import me.xtrm.xeon.loader.api.XeonProvider;

public class PluginManager implements IPluginManager {

	private final Logger logger;
	private final Map<IPlugin, PluginInfo> plugins;
	
	public PluginManager() {
		this.logger = LogManager.getLogger("PluginManager");
		this.plugins = new HashMap<IPlugin, PluginInfo>();
	}
	
	@Override
	public void init(File pluginsDir) {
		this.logger.info("Initializing PluginManager...");
		
		Stream.of(DeltaAPI.getClientData().getPlugins()).forEach(p -> {
			try {
				this.logger.info("Loading internal plugin {}", p);
				
				Class<? extends IPlugin> clazz = (Class<? extends IPlugin>) ((URLClassLoader)XeonProvider.getXeonLoader().getXCL()).loadClass(p);
				PluginInfo info = clazz.getDeclaredAnnotation(PluginInfo.class);
				if(info == null) {
					throw new PluginLoadingException("Plugin class " + clazz + " doesn't implement @PluginInfo");
				}
				
				new EventPluginLoad(info, true).call();

				IPlugin plugin = clazz.getConstructor().newInstance();
				
				this.plugins.put(plugin, info);
				this.logger.info("Loaded internal plugin " + info.name() + " version " + info.version() + " by " + info.author());
			} catch(ReflectiveOperationException e) {
				throw new PluginLoadingException("Error while loading internal plugin class... wtf", e);
			}
		});
		
		this.logger.info("Searching for plugins in {}", pluginsDir.getAbsolutePath());
		
		for(File file : pluginsDir.listFiles()) {
			boolean isPlugin = isPlugin(file);
			
			this.logger.info("Loading " + (isPlugin ? "non-" : "") + "plugin file {}", file.getName());
			
			try {
				XeonProvider.getXeonLoader().getXCL().addURL(file.toURI().toURL());
			} catch (MalformedURLException e) {
				throw new RuntimeException("Cannot transform file into URL... wierd", e);
			}
			
			if(isPlugin) {
				loadPlugin(file);	
			}
		}
		
		this.logger.info("Loaded {} plugins! (Internal: {}, External: {})", this.plugins.size(), DeltaAPI.getClientData().getPlugins().length, (this.plugins.size() - DeltaAPI.getClientData().getPlugins().length));
	}

	@Override
	public void loadPlugin(File pluginFile) {
		String mainClass = getPluginMainClass(pluginFile);
		
		try {
			Class<? extends IPlugin> clazz = (Class<? extends IPlugin>) ((URLClassLoader)XeonProvider.getXeonLoader().getXCL()).loadClass(mainClass);
			PluginInfo info = clazz.getDeclaredAnnotation(PluginInfo.class);
			if(info == null) {
				throw new PluginLoadingException("Plugin class " + clazz + " doesn't implement @PluginInfo");
			}
			
			IEvent e = new EventPluginLoad(info, false);
			e.call();
			if(e.isCancelled()) return;

			IPlugin plugin = clazz.getConstructor().newInstance();
			
			this.plugins.put(plugin, info);
			this.logger.info("Loaded plugin " + info.name() + " version " + info.version() + " by " + info.author());
		} catch(ReflectiveOperationException e) {
			throw new PluginLoadingException("Error while loading plugin class", e);
		}
	}

	@Override
	public boolean isPlugin(File jarFile) {
		return getPluginMainClass(jarFile) != null;
	}

	@Override
	public String getPluginMainClass(File pluginFile) {
		try {
			JarFile jarFile = new JarFile(pluginFile);
			ZipEntry entry = jarFile.getEntry("/plugin_data.yml");
			InputStream is = jarFile.getInputStream(entry);
			
			Yaml yaml = new Yaml();
			Map<String, Object> data = yaml.load(is);
			
			jarFile.close();
			
			return (String)data.get("pluginMain");
		} catch(Exception e) {}
		
		return null;
	}

	@Override
	public Map<IPlugin, PluginInfo> getLoadedPlugins() {
		return plugins;
	}

}
