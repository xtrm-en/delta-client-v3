package me.xtrm.delta.client.management.module;

import java.util.ArrayList;
import java.util.List;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.player.EventKeyboard;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.api.module.IModuleManager;
import me.xtrm.delta.client.management.module.impl.combat.Aimbot;
import me.xtrm.delta.client.management.module.impl.combat.AntiKnockback;
import me.xtrm.delta.client.management.module.impl.combat.Criticals;
import me.xtrm.delta.client.management.module.impl.combat.KillAura;
import me.xtrm.delta.client.management.module.impl.combat.TPAura;
import me.xtrm.delta.client.management.module.impl.hidden.ClickGUI;
import me.xtrm.delta.client.management.module.impl.misc.AntiAFK;
import me.xtrm.delta.client.management.module.impl.misc.AutoSpawn;
import me.xtrm.delta.client.management.module.impl.misc.DeathCoordinates;
import me.xtrm.delta.client.management.module.impl.misc.Derp;
import me.xtrm.delta.client.management.module.impl.misc.FastSpawn;
import me.xtrm.delta.client.management.module.impl.misc.MCF;
import me.xtrm.delta.client.management.module.impl.misc.NoClip;
import me.xtrm.delta.client.management.module.impl.misc.Spin;
import me.xtrm.delta.client.management.module.impl.misc.Twerk;
import me.xtrm.delta.client.management.module.impl.movement.AirJump;
import me.xtrm.delta.client.management.module.impl.movement.Blink;
import me.xtrm.delta.client.management.module.impl.movement.Fly;
import me.xtrm.delta.client.management.module.impl.movement.Freecam;
import me.xtrm.delta.client.management.module.impl.movement.Glide;
import me.xtrm.delta.client.management.module.impl.movement.HighJump;
import me.xtrm.delta.client.management.module.impl.movement.InventoryMove;
import me.xtrm.delta.client.management.module.impl.movement.Jesus;
import me.xtrm.delta.client.management.module.impl.movement.NoFall;
import me.xtrm.delta.client.management.module.impl.movement.Speed;
import me.xtrm.delta.client.management.module.impl.movement.Spider;
import me.xtrm.delta.client.management.module.impl.movement.Sprint;
import me.xtrm.delta.client.management.module.impl.movement.Step;
import me.xtrm.delta.client.management.module.impl.player.AutoBreak;
import me.xtrm.delta.client.management.module.impl.player.AutoRespawn;
import me.xtrm.delta.client.management.module.impl.player.AutoWalk;
import me.xtrm.delta.client.management.module.impl.player.FastEat;
import me.xtrm.delta.client.management.module.impl.player.Regen;
import me.xtrm.delta.client.management.module.impl.player.Spammer;
import me.xtrm.delta.client.management.module.impl.render.ChestESP;
import me.xtrm.delta.client.management.module.impl.render.ESP;
import me.xtrm.delta.client.management.module.impl.render.Fullbright;
import me.xtrm.delta.client.management.module.impl.render.HUD;
import me.xtrm.delta.client.management.module.impl.render.Nametags;
import me.xtrm.delta.client.management.module.impl.render.Tracers;
import me.xtrm.delta.client.management.module.impl.render.UnclaimFinder;
import me.xtrm.delta.client.management.module.impl.render.XRay;
import me.xtrm.delta.client.management.module.impl.world.FastBreak;
import me.xtrm.delta.client.management.module.impl.world.FastPlace;
import me.xtrm.delta.client.management.module.impl.world.Nuker;
import me.xtrm.delta.client.management.module.impl.world.Timer;
import me.xtrm.delta.client.utils.MinecraftEnvironment;
import me.xtrm.delta.loader.api.LoaderProvider;

/**
 * Manager listing & registering all Modules
 */
public class ModuleManager implements IModuleManager {

	private final List<IModule> modules;
	
	public ModuleManager() {
		modules = new ArrayList<>();
	}
	
	@Override
	public void init() {
		modules.add(new Aimbot());
		modules.add(new AirJump());
		modules.add(new AntiAFK());
		modules.add(new AntiKnockback());
		modules.add(new AutoBreak());
		modules.add(new AutoRespawn());
		if(MinecraftEnvironment.isPaladium()) modules.add(new AutoSpawn());
		modules.add(new AutoWalk());
		modules.add(new Blink());
		modules.add(new ChestESP());
		modules.add(new ClickGUI());
		modules.add(new Criticals());
		modules.add(new DeathCoordinates());
		modules.add(new Derp());
		modules.add(new ESP());
		modules.add(new FastBreak());
		modules.add(new FastEat());
		modules.add(new FastPlace());
		if(MinecraftEnvironment.isPaladium()) modules.add(new FastSpawn());
		modules.add(new Fly());													//TODO: ANTI-KICK // xTram
		modules.add(new Freecam());
		modules.add(new Fullbright());
		modules.add(new Glide());
		modules.add(new HighJump());
		modules.add(new HUD());
		modules.add(new InventoryMove());
		modules.add(new Jesus());
		modules.add(new KillAura());
		modules.add(new MCF());
		modules.add(new Nametags());
		modules.add(new NoClip());
		modules.add(new NoFall());
		modules.add(new Nuker());
		modules.add(new Regen());
		modules.add(new Spammer());
		modules.add(new Speed());
		modules.add(new Spider());
		modules.add(new Spin());
		modules.add(new Sprint());
		modules.add(new Step());
		modules.add(new Timer());
		modules.add(new TPAura());
		modules.add(new Tracers());
		modules.add(new Twerk()); // je regrette
		modules.add(new UnclaimFinder());
		modules.add(new XRay());
		
		LoaderProvider.getLoader().getEventManager().subscribe(this);
	}
	
	@Override
	public List<IModule> getModules() {
		return modules;
	}
	
	@Handler
	public void onKey(EventKeyboard e) {
		if(e.getKey() == 0 || e.getKey() == -1)
			return;
		
		getModules().stream().filter(m -> m.getKey() == e.getKey()).forEach(IModule::toggle);
	}

}
