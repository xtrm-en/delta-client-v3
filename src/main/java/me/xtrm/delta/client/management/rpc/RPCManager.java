package me.xtrm.delta.client.management.rpc;

import java.io.IOException;
import java.net.URL;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordUser;
import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.loader.api.LoaderProvider;
import net.minecraft.client.gui.GuiScreen;

public enum RPCManager {
	INSTANCE;
	
	private club.minnced.discord.rpc.DiscordRPC lib;
	private DiscordEventHandlers handlers;
	private DiscordRichPresence presence;
	
	private DiscordUser info;
	
	private long startTimestamp;
	
	private void load() {
		LoaderProvider.getLoader().getEventManager().subscribe(this);
		
		lib = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
        
		String applicationId = null;
		try {
			applicationId = WebUtils.readUrl(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/d_id")).replace("\n", "");
		} catch (IOException e1) {
			e1.printStackTrace();
			applicationId = "688377297015013486";
		}
		
        String steamId = "";
        
        handlers = new DiscordEventHandlers();
        handlers.ready = ((user) -> {
			RPCManager.this.info = user;
		});
        
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        
        presence = new DiscordRichPresence();
        updateRPC("In menus", "Loading Delta...");
		
		Thread rpcThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                	lib.Discord_Shutdown();
                }
            }
		}, "Delta RPC");
		rpcThread.setDaemon(true);
		rpcThread.start();
	}
	
	private int last;
	private GuiScreen lastGui;
	
	@Handler
	public void onUpdate(EventUpdate e) {
		if(Wrapper.mc.currentScreen != lastGui) {
			lastGui = Wrapper.mc.currentScreen;
			if(lastGui == null) {
				last = -999;
			}
		}
		
		int total = 0;
		int i = 0;
		for(IModule m : DeltaAPI.getClient().getModuleManager().getModules()) {
			total++;
			if(m.isEnabled()) {
				i++;
			}
		}

		if(last != i) {
			last = i;
			
			boolean b = Wrapper.mc.isSingleplayer();			
			updateRPC("En jeu - " + (b ? "Solo" : Wrapper.mc.func_147104_D().serverIP), i + "/" + total + " modules activés.");
		}
	}
	
	public void updateRPC(String details, String status) {
		if(presence == null) {
			load();
			return;
		}
		
		presence.largeImageKey = "delta-title2";
		presence.smallImageKey = "delta-logo2";
        presence.largeImageText = Consts.NAME + " " + Consts.VER_STR;
        presence.smallImageText = "http://idelta.fr/";
        presence.startTimestamp = startTimestamp;
        
		presence.details = details;
        presence.state = status;
        
        lib.Discord_UpdatePresence(presence);
	}
	
	public DiscordUser getUser() {
		return info;
	}

}
