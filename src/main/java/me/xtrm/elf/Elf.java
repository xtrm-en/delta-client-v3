package me.xtrm.elf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.loader.api.LoaderProvider;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPostLoading;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPreLoading;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCShutdown;
import me.xtrm.delta.loader.api.plugin.types.IPlugin;
import me.xtrm.delta.loader.api.plugin.types.PluginInfo;
import me.xtrm.elf.pixies.killswitch.Killswitch;

@PluginInfo(author = "xTrM_", version = "2.20", name = "Elf")
public class Elf implements IPlugin {
	
	private Logger logger;
	
	public static boolean apiOnline, isPaarthurnaxOnline;
	
	public Elf() {		
		logger = LogManager.getLogger("ELF");
	}
	
	@Handler
	public void onInit(EventMCPreLoading e) {
		logger.info("Pre-Initializing ELF.");

		WebUtils.checkInternet();

		if(System.getProperty("delta.devenv", "false") != "true")
			new Killswitch();
	}

	@Handler
	public void onPostInit(EventMCPostLoading event) {
		logger.info("Post-Initializing ELF.");
		
		LoaderProvider.getLoader().getEventManager().subscribe(this);
		
		WebUtils.checkInternet();
	}
	
	@Handler
	public void onPlayerUpdate(EventMotion e) {
		if(Killswitch.isKillswitched()) {
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			logger.info("Skidada Skidoodle, your game is outdated you fucking noodle");
			
			onShutdown(null);
			Wrapper.exitGame();
		}
	}

	@Handler
	public void onShutdown(EventMCShutdown event) {
		
	}
}
