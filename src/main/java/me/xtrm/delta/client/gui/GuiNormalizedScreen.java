package me.xtrm.delta.client.gui;

import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class GuiNormalizedScreen extends GuiScreen implements GuiYesNoCallback {

	private int lastGuiScale = -69;
	
	@Override
	public void initGui() {
		lastGuiScale = mc.gameSettings.guiScale;
		if(mc.gameSettings.guiScale != 2) {
			mc.gameSettings.guiScale = 2;
			
			ScaledResolution sr = ScaledUtils.gen();
			this.width = sr.getScaledWidth();
	        this.height = sr.getScaledHeight();
		}
		
		super.initGui();
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		
		if(lastGuiScale != -69) {
			mc.gameSettings.guiScale = lastGuiScale;
		}
	}
	
}
