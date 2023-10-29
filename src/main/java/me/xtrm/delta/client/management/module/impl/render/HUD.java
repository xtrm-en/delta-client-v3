package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.render.EventRender2D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.management.module.impl.render.hud.TabGui;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.Modes;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.render.ColorUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.loader.api.LoaderProvider;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class HUD extends Module {
	
	public HUD() {
		super("HUD", Category.RENDER, true);
		setDescription("L'interface");
		
		registerSettings(
				new Setting("Logo", this, true),
				new Setting("ArrayList", this, true),
				new Setting("TabGui", this, false),
				new Setting("Spacer", this),
				new Setting("Mode", this, "Delta", Modes.build("Delta", "Ancien", "Altas"))
		);
	}
	
	private TabGui tabGui;
	
	@Handler
	public void onRender2D(EventRender2D e) {	
		if(mc.gameSettings.showDebugInfo) return;
		
		String mode = getSetting("Mode").getComboValue();
		
		if(getSetting("Logo").getCheckValue()) {
			switch(mode) {
				case "Delta":
					drawNewWatermark();
					break;
				case "Ancien":
					drawWatermark();
					break;
				case "Altas":
					drawWatermarkAtlas();
					break;
			}
		}
		
		if(getSetting("ArrayList").getCheckValue()) {
			switch(mode) {
				case "Delta":
					drawNewArraylist();
					break;
				case "Ancien":
					drawArraylist();
					break;
				case "Altas":
					drawArraylistAtlas();
					break;
			}
		}
	}
	
	CachedResource logo = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_title.png");
	
	public void drawNewWatermark() {
		RenderUtils.bindCachedResource(logo);
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_BLEND);
			double val = 0.3;
			GL11.glScaled(val, val, val);
			Gui.func_146110_a(-20, -30, 0, 0, 333, 333, 333, 333);
		}
		GL11.glPopMatrix();
	}
	
	public void drawWatermark() {
		Fonts.fontWatermark.drawStringWithShadow(
				Consts.NAME.substring(0,1) + "" + EnumChatFormatting.WHITE + Consts.NAME.substring(1, Consts.NAME.length()), 
				4, 
				2, 
				0xA211A2
		);
	}
	
	public void drawWatermarkAtlas() {
		GL11.glPushMatrix();
		{
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			drawStringRBW(Consts.NAME, 3, 2, 0.9F);
		}
		GL11.glPopMatrix();
		Fonts.mcFont.drawStringWithShadow(EnumChatFormatting.GRAY + Consts.VER_STR, (int)(Fonts.mcFont.getStringWidth(Consts.NAME) * 1.5F) + 8, 2, -1);
	}
	
	private TimeHelper timer = new TimeHelper();
	
	private float hue = 0;
	public void drawNewArraylist() {
		List<IModule> mods = new ArrayList<>();
		DeltaAPI.getClient().getModuleManager().getModules().stream().filter(m -> m.isVisible() && m.getAnimation() != -1).forEach(mods::add);
		
		Collections.sort(mods, new Comparator<IModule>() { public int compare(IModule m1, IModule m2) { if (Fonts.fontHUD.getStringWidth(m1.getDisplayName()) > Fonts.fontHUD.getStringWidth(m2.getDisplayName())) { return -1; } if (Fonts.fontHUD.getStringWidth(m1.getDisplayName()) < Fonts.fontHUD.getStringWidth(m2.getDisplayName())) { return 1; } return 0; } });
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int yPos = 0;
		int increment = (int)Fonts.fontHUD.getFontHeight();
				
		float hue1 = this.hue;
		if(timer.hasReached(1000 / 60)) {
			this.hue += 0.005F;
			timer.reset();
		}
		if(this.hue >= 255.0F) {
			this.hue = 0.0F;
		}

		int index = 0;
		for(IModule m : mods) {
			String text = m.getDisplayName();
			
			int widthUnder = 0;
			boolean hasLowerAnimation = false;
			
			int nextModule = index + 1;
			if(nextModule < mods.size()) {
				IModule m2 = mods.get(nextModule);

				widthUnder = m2.getAnimation() + 2;
				
				hasLowerAnimation = m2.getAnimation() > m.getAnimation();
			}
			
			Gui.drawRect(sr.getScaledWidth() - m.getAnimation() - 2, yPos, sr.getScaledWidth(), yPos + increment, 0xAA000000);
			Fonts.fontHUD.drawStringWithShadow(text, sr.getScaledWidth() - m.getAnimation() - 1, yPos, Color.HSBtoRGB(hue1, 0.6F, 1F));
			Gui.drawRect(sr.getScaledWidth() - 2, yPos, sr.getScaledWidth(), yPos + increment, Color.HSBtoRGB(hue1, 0.6F, 1F));
			
			Gui.drawRect(sr.getScaledWidth() - m.getAnimation() - 2 - 1, yPos, sr.getScaledWidth() - m.getAnimation() - 2, yPos + increment, Color.HSBtoRGB(hue1, 0.6F, 1F));
			Gui.drawRect(sr.getScaledWidth() - m.getAnimation() - 2 - 1, yPos + increment - (hasLowerAnimation ? 1 : 0), sr.getScaledWidth() - widthUnder - (hasLowerAnimation ? 1 : 0), yPos + increment + 1 - (hasLowerAnimation ? 1 : 0), Color.HSBtoRGB(hue1, 0.6F, 1F));
			
			for(int i = 0; i < 3; i++) {
				if(m.isEnabled()) {
					if(m.getAnimation() < Fonts.fontHUD.getStringWidth(text) + 4) { m.setAnimation(m.getAnimation() + 1); }
					if(m.getAnimation() > Fonts.fontHUD.getStringWidth(text) + 5) { m.setAnimation((int)Fonts.font.getStringWidth(text)); }
				}else {
					if(m.getAnimation() > -1) { m.setAnimation(m.getAnimation() - 1); }
				}
			}
			
			hue1 += 0.05f;
      		if(hue1 >= 255.0F) {
      			hue1 = 0.0F;
      		}
			
			yPos += Math.min(increment, m.getAnimation());
			
			index++;
		}
	}
	
	public void drawArraylist() {
		List<IModule> mods = new ArrayList<>();
		DeltaAPI.getClient().getModuleManager().getModules().forEach(mods::add);
		
		Collections.sort(mods, new Comparator<IModule>() { public int compare(IModule m1, IModule m2) { if (Fonts.font.getStringWidth(m1.getDisplayName()) > Fonts.font.getStringWidth(m2.getDisplayName())) { return -1; } if (Fonts.font.getStringWidth(m1.getDisplayName()) < Fonts.font.getStringWidth(m2.getDisplayName())) { return 1; } return 0; } });
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int yPos = 0;
		int increment = (int)Fonts.font.getFontHeight();
				
		float hue1 = this.hue;
		if(timer.hasReached(1000 / 60)) {
			this.hue += 0.005F;
			timer.reset();
		}
		if(this.hue >= 255.0F) {
			this.hue = 0.0F;
		}

		for(IModule m : mods) {
			if(m.isVisible() && m.getAnimation() != -1) {
				String text = m.getDisplayName();
				
				Gui.drawRect(sr.getScaledWidth() - m.getAnimation() - 2, yPos, sr.getScaledWidth(), yPos + increment, 0xAA000000);
				Fonts.font.drawStringWithShadow(text, sr.getScaledWidth() - m.getAnimation() - 1, yPos, Color.HSBtoRGB(hue1, 0.6F, 1F));
				Gui.drawRect(sr.getScaledWidth() - 2, yPos, sr.getScaledWidth(), yPos + increment, Color.HSBtoRGB(hue1, 0.6F, 1F));
				
				for(int i = 0; i < 3; i++) {
					if(m.isEnabled()) {
						if(m.getAnimation() < Fonts.font.getStringWidth(text) + 4) { m.setAnimation(m.getAnimation() + 1); }
						if(m.getAnimation() > Fonts.font.getStringWidth(text) + 5) { m.setAnimation((int)Fonts.font.getStringWidth(text)); }
					}else {
						if(m.getAnimation() > -1) { m.setAnimation(m.getAnimation() - 1); }
					}
				}
				
				hue1 += 0.05f;
	      		if(hue1 >= 255.0F) {
	      			hue1 = 0.0F;
	      		}
				
				yPos += Math.min(increment, m.getAnimation());
			}
		}
	}
	
	public void drawArraylistAtlas() {
		List<IModule> mods = new ArrayList<>();
		DeltaAPI.getClient().getModuleManager().getModules().forEach(mods::add);
		
		Collections.sort(mods, new Comparator<IModule>() { public int compare(IModule m1, IModule m2) { if (Fonts.mcFont.getStringWidth(m1.getDisplayName()) > Fonts.mcFont.getStringWidth(m2.getDisplayName())) { return -1; } if (Fonts.mcFont.getStringWidth(m1.getDisplayName()) < Fonts.mcFont.getStringWidth(m2.getDisplayName())) { return 1; } return 0; } });
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		float hue1 = this.hue;
		if(timer.hasReached(1000 / 60)) {
			this.hue += 0.005F;
			timer.reset();
		}
		if(this.hue >= 255.0F) {
			this.hue = 0.0F;
		}
		
		int count = 1;
		int addiable = Fonts.mcFont.FONT_HEIGHT + 1;
		for(IModule m : mods) {
			if(m.getAnimation() != -1 && m.getCategory().isVisible() && m.isVisible()) {
				Fonts.mcFont.drawStringWithShadow(m.getDisplayName(), 
										sr.getScaledWidth() - m.getAnimation(), 
										count, 
										Color.HSBtoRGB(hue1, 0.5F, 1F)
				);
				for(int i = 0; i < 3; i++) {
					if(m.isEnabled()) {
						if(m.getAnimation() < Fonts.mcFont.getStringWidth(m.getDisplayName()) + 2) { m.setAnimation(m.getAnimation() + 1); }
						if(m.getAnimation() > Fonts.mcFont.getStringWidth(m.getDisplayName()) + 3) { m.setAnimation((int)Fonts.mcFont.getStringWidth(m.getDisplayName())); }
					}else {
						if(m.getAnimation() > -1) { m.setAnimation(m.getAnimation() - 1); }
					}
				}
				
				hue1 += 0.05f;
	      		if(hue1 >= 255.0F) {
	      			hue1 = 0.0F;
	      		}
				
				count += Math.min(addiable, m.getAnimation());
			}
		}
	}
	
	public void drawStringRBW(String s, int x, int y, float brightness) {
		int xx = x;
		for(int i = 0; i < s.length(); i++) {
			String sdd = s.charAt(i) + "";
			
			Fonts.mcFont.drawStringWithShadow(sdd, xx, y, ColorUtils.effect(i * 3500000L, brightness, 100).getRGB());
			xx += Fonts.mcFont.getStringWidth(sdd);
		}
	}
	
	@Override
	public void onEnable() {
		if(tabGui == null)
			tabGui = new TabGui();
		
		LoaderProvider.getLoader().getEventManager().subscribe(tabGui);
		
		if(mc != null && mc.theWorld != null) {
			Display.setTitle(Consts.NAME + " " + Consts.VER_STR);
		}
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		LoaderProvider.getLoader().getEventManager().unsubscribe(tabGui);
		
		Display.setTitle(System.getProperty("delta.windowTitle").replace("%PLAYER_USERNAME%", mc.getSession().getUsername()));
	}

}
