package me.xtrm.delta.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.IDeltaClient;
import me.xtrm.delta.client.api.command.ICommandManager;
import me.xtrm.delta.client.api.file.IFileManager;
import me.xtrm.delta.client.api.friend.IFriendManager;
import me.xtrm.delta.client.api.module.IModuleManager;
import me.xtrm.delta.client.api.setting.ISettingManager;
import me.xtrm.delta.client.management.command.CommandManager;
import me.xtrm.delta.client.management.file.FileManager;
import me.xtrm.delta.client.management.friend.FriendManager;
import me.xtrm.delta.client.management.hook.FMLBusHook;
import me.xtrm.delta.client.management.hook.FMLEventHook;
import me.xtrm.delta.client.management.module.ModuleManager;
import me.xtrm.delta.client.management.setting.SettingManager;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.MinecraftEnvironment;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.loader.api.event.data.EventType;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLInitialization;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventFMLLoad;
import me.xtrm.delta.loader.api.event.events.lifecycle.EventMCPostLoading;
import net.minecraftforge.common.MinecraftForge;

public class Delta implements IDeltaClient {

	private final Logger logger;
	
	private final IFileManager fileManager;
	private final IFriendManager friendManager;
	private final IModuleManager moduleManager;
	private final ISettingManager settingManager;
	private final ICommandManager commandManager;
	
	public Delta() {
		this.logger = LogManager.getLogger("Delta");
		
		this.logger.info("Initializing FileManager...");
		this.fileManager = new FileManager();
		this.logger.info("Initializing FriendManager...");
		this.friendManager = new FriendManager();
		this.logger.info("Initializing ModuleManager...");
		this.moduleManager = new ModuleManager();
		this.logger.info("Initializing SettingManager...");
		this.settingManager = new SettingManager();
		this.logger.info("Initializing CommandManager...");
		this.commandManager = new CommandManager();
		
		this.logger.info("Loaded ClientCore successfully!");
	}
	
	@Handler
	public void onFMLLoad(EventFMLLoad event) {
		this.logger.info(Consts.NAME + " Client rel(" + Consts.VER_STR + "-" + Consts.VER_TYPE + ") - Starting on MinecraftEnvironment{type=" + MinecraftEnvironment.getType() + "}");
	}
	
	@Handler
	public void onFMLInit(EventFMLInitialization event) {
		if(event.getType() != EventType.POST) return;
		
		this.logger.info("Registering base modules...");
		this.moduleManager.init();
		this.logger.info("Registering base commands...");
		this.commandManager.init();
		this.logger.info("Finished Pre-Initialization.");
	}
	
	@Handler
	public void onMCPostLoad(EventMCPostLoading event) {
		System.setProperty("delta.windowTitle", Display.getTitle());
		Display.setTitle(Consts.NAME + " " + Consts.VER_STR);
		
		this.logger.info("Loading saved files...");
		this.fileManager.init();
		
		this.logger.info("Loading HookSystemManager...");
		FMLBusHook.INSTANCE.silentRegister(new FMLEventHook());
		
		try {
			FMLBusHook.INSTANCE.unhookPalalol(MinecraftForge.EVENT_BUS);
		} catch(ReflectiveOperationException e) {
			this.logger.error("Error executing /UNHOOK/", e);
			e.printStackTrace();
		}
		
		RenderUtils.disableFastRender();
		
		logger.info("Delta was loaded successfully!");
	}
	
	@Override
	public IFileManager getFileManager() {
		return fileManager;
	}
	
	@Override
	public IFriendManager getFriendManager() {
		return friendManager;
	}

	@Override
	public IModuleManager getModuleManager() {
		return moduleManager;
	}

	@Override
	public ISettingManager getSettingManager() {
		return settingManager;
	}

	@Override
	public ICommandManager getCommandManager() {
		return commandManager;
	}
	
}
