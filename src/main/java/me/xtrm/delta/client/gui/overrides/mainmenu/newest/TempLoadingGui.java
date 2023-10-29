package me.xtrm.delta.client.gui.overrides.mainmenu.newest;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class TempLoadingGui extends GuiNormalizedScreen {
	
	private static CachedResource logo = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_icon.png");
	private static boolean loaded;
	
	private TimeHelper timer;
	private GuiScreen nextScreen;
	
	public TempLoadingGui(GuiScreen nextScreen) {
		this.nextScreen = nextScreen;
		this.timer = new TimeHelper();
		this.timer.reset();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		if(loaded) {
			onGuiClosed();
			mc.displayGuiScreen(nextScreen);
		}
	}
	
	private double animScale = 5;
	private int boomAnimation = 0;
	private int blackFadeAnimation = 0;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = ScaledUtils.gen();
		Gui.drawRect(-1, -1, sr.getScaledWidth() + 1, sr.getScaledHeight() + 1, 0xFF0A0A0A);
		
		if(animScale < 0.1) {
			animScale = 0.1;
		}
		if(animScale == 0.1) {
			boomAnimation++;
		}
		
		int renderedWidth = 350 / 2;
		int renderedHeight = 350 / 2;
		
		renderedWidth *= animScale;
		renderedHeight *= animScale;
		
		RenderUtils.bindCachedResource(logo); // width/height = 350/350
		
		if(boomAnimation < 3) {
			GL11.glPushMatrix();
			{					
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4d(1, 1, 1, 1);
				GL11.glTranslated((sr.getScaledWidth() / 2) - 1, (sr.getScaledHeight() / 2) - 1, 0);
				GL11.glScaled(animScale, animScale, 1);
				GL11.glRotated(MathHelper.wrapAngleTo180_double(System.currentTimeMillis() / 2) + 180, 0, 0, 1); // spinnnnnnnnnnn
				Gui.func_152125_a(-renderedWidth / 2, -renderedHeight / 2, 0, 0, 350, 350, renderedWidth, renderedHeight, 350, 350);
			}
			GL11.glPopMatrix();
			if(animScale > 3)
				animScale -= 0.045;
			if(animScale > 1)
				animScale -= 0.075;
			else
				animScale -= 0.1;
		}
		
		if(boomAnimation != 0) {
			GL11.glPushMatrix();
			{
				GL11.glTranslated((sr.getScaledWidth() / 2) - 1, (sr.getScaledHeight() / 2) - 1, 0);
				GuiUtils.getInstance().drawFullCircle(0, 0, boomAnimation * 50, 0xFFEEEEEE);
			}
			GL11.glPopMatrix();
		}
		
		if(boomAnimation * 50 > sr.getScaledWidth() / 2) {
			blackFadeAnimation++;
			Color c = new Color(10,10,10,Math.min(255, blackFadeAnimation * 10));
			
			Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), c.getRGB());
			if(blackFadeAnimation * 10 > 320) {
				loaded = true;
				mc.displayGuiScreen(nextScreen);
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		
	}

}