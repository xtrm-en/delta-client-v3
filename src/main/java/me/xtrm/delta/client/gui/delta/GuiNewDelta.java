package me.xtrm.delta.client.gui.delta;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URL;

import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.altlogin.GuiOldAltLogin;
import me.xtrm.delta.client.gui.alt2.GuiAltManager;
import me.xtrm.delta.client.gui.overrides.mainmenu.newest.NewMainMenu;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.animate.Translate;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

public class GuiNewDelta extends GuiNormalizedScreen {

	private NewMainMenu nmm;
	
	private boolean back;
	private Translate anim, animBack;
	
	public GuiNewDelta(NewMainMenu nmm) {
		this.nmm = nmm;
		this.anim = new Translate(0, 0);
	}
	
	private static final CachedResource logofull = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_full.png");
	
	@Override
	public void initGui() {
		super.initGui();
		
		ScaledResolution sr = ScaledUtils.gen();
		
		this.buttonList.add(new UIRoundedButton(0, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 4, 200, 20, "Retour"));
		this.buttonList.add(new UIRoundedButton(42, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 4 - 50 - 55, 98, 20, "Alt Manager")); // iciiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
		this.buttonList.add(new UIRoundedButton(1, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 4 - 50, 98, 20, "Alt Login"));
		this.buttonList.add(new UIRoundedButton(2, sr.getScaledWidth() / 2 + 2, sr.getScaledHeight() - sr.getScaledHeight() / 4 - 50, 98, 20, "Credits"));
		this.buttonList.add(new UIRoundedButton(3, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() - sr.getScaledHeight() / 4 - 25, 98, 20, "Discord"));
		this.buttonList.add(new UIRoundedButton(4, sr.getScaledWidth() / 2 + 2, sr.getScaledHeight() - sr.getScaledHeight() / 4 - 25, 98, 20, "Site Web"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = ScaledUtils.gen();
		
		this.drawDefaultBackground();
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF090909);
		
		RenderUtils.bindCachedResource(logofull);
		
		int renderWidth = 233;
		int renderHeight = 111;
		
		GL11.glPushMatrix();
		{
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glTranslated(sr.getScaledWidth() / 2, sr.getScaledHeight() / 4, 0);
			Gui.func_152125_a(-(renderWidth / 2), -(renderHeight / 2), 0, 0, 700, 333, renderWidth, renderHeight, 700, 333);
		}
		GL11.glPopMatrix();		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		int uhhhhAddVariableThing = 50; // the "slope"
		
		if(anim != null) {
			if(!anim.interpolate(sr.getScaledWidth() + uhhhhAddVariableThing, 0, 5)) {
				GuiUtils.getInstance().drawParallelogram(0, 0, sr.getScaledWidth() - anim.getX() + uhhhhAddVariableThing, 0, sr.getScaledWidth() - anim.getX(), sr.getScaledHeight(), 0, sr.getScaledHeight(), 0xFF0A0A0A);
			}else {
				anim = null;
			}
		}
		
		if(back) {
			if(animBack == null) {
				animBack = new Translate(0, 0);
			}
						
			if(animBack.interpolate(sr.getScaledWidth() + uhhhhAddVariableThing, 0, 5)) {
				back = false;
				NewMainMenu.cascadeAnim = new Translate(0, 0);
				NewMainMenu.introCascade = -1;
				mc.displayGuiScreen(nmm);
			}
			
			GuiUtils.getInstance().drawParallelogram(0, 0, animBack.getX() - uhhhhAddVariableThing, 0, animBack.getX(), sr.getScaledHeight(), 0, sr.getScaledHeight(), 0xFF0A0A0A);
		}
	}
	
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if(back) return;
		if(anim != null) return;
		
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}
	
	@Override protected void keyTyped(char p_73869_1_, int p_73869_2_) {}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			back = true;
		}
		if(button.id == 1) {
			mc.displayGuiScreen(new GuiOldAltLogin(this));
		}
		if(button.id == 42) {
			mc.displayGuiScreen(new GuiAltManager(this));
		}
		if(button.id == 3) {
			try {
				if(Desktop.getDesktop().isSupported(Action.BROWSE)) {
					Desktop.getDesktop().browse(new URL(WebUtils.getDiscordUrl()).toURI());
				} else {
					Runtime.getRuntime().exec(WebUtils.getDiscordUrl());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(button.id == 2) {
			mc.displayGuiScreen(new GuiDCredits(this));
		}
		if(button.id == 4) {
			try {
				if(Desktop.getDesktop().isSupported(Action.BROWSE)) {
					Desktop.getDesktop().browse(new URL(WebUtils.getWebsite()).toURI());
				} else {
					Runtime.getRuntime().exec(WebUtils.getWebsite());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}