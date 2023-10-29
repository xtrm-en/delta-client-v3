package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import org.lwjgl.input.Keyboard;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.management.module.impl.render.xray.XData;
import me.xtrm.delta.client.management.module.impl.render.xray.XRayManager;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

public class XRay extends Module {
	
	public static XData blacklist;
	
	public XRay() {
		super("XRay", Keyboard.KEY_X, Category.RENDER);
		setDescription("Permet de voir les minerais à travers les blocks");
		
		blacklist = new XData();
		
		// 1,2,3,4,7,12,13,17,18,24,31,32,78,87,88,106,121
		blacklist.add(1, 0);
		blacklist.add(2, 0);
		blacklist.add(3, 0);
		blacklist.add(4, 0);
		blacklist.add(7, 0);
		blacklist.add(12, 0);
		blacklist.add(13, 0);
		blacklist.add(17, 0);
		blacklist.add(18, 0);
		blacklist.add(24, 0);
		blacklist.add(31, 0);
		blacklist.add(32, 0);
		blacklist.add(78, 0);
		blacklist.add(87, 0);
		blacklist.add(88, 0);
		blacklist.add(106, 0);
		blacklist.add(121, 0);
		
		// Modded shit
		blacklist.add(484, -1);
		blacklist.add(485, -1);
		blacklist.add(486, -1);
		blacklist.add(487, -1);
		blacklist.add(488, -1);
		
		XRayManager.xray = this;
	}	
	
	@Handler
	public void onUpdate(EventUpdate e) {
		mc.gameSettings.ambientOcclusion = 0;
		mc.gameSettings.gammaSetting = 100F;
	}
	
	@Override
	public void onEnable() {
		oldGamma = mc.gameSettings.gammaSetting;
		super.onEnable();
	}
	
	float oldGamma;
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = oldGamma;
		super.onDisable();
	}
	
	@Override
	public void onToggle() {
		super.onToggle();
		mc.renderGlobal.loadRenderers();
	}
	
	public static boolean shouldRender(boolean orig, Object self, Object blockAccess, int x, int y, int z, int side) {
		int id = Block.getIdFromBlock((Block)self);
		int meta = ((IBlockAccess)blockAccess).getBlockMetadata(x, y, z);
		
		return !blacklist.contains(id, meta);
	}

}
