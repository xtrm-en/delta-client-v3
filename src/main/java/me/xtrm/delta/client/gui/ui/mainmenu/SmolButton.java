package me.xtrm.delta.client.gui.ui.mainmenu;

import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.animate.Translate;
import me.xtrm.delta.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class SmolButton extends ShowableButton {

	private CachedResource icon;
	private int iconWidth, iconHeight;
	private int anim; 
	
	private Translate appearAnim;
	private Translate anim2;
	
	public SmolButton(int id, int x, int y, int width, int height, String text, CachedResource icon, int iconWidth, int iconHeight, long delay) {
		super(id, x, y, width, height, text, delay);
		this.icon = icon;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(visible) {
			if(!enabled) {
				Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xDE000000);
//				Fonts.font.drawStringWithShadow(
//						displayString, 
//						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
//						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2), 
//						-1);
			}else {
				if(appearAnim == null || anim2 == null) return;
				appearAnim.interpolate(100, 0, 3);				
				
				field_146123_n = mouseX >= this.xPosition && mouseX <= xPosition + width && mouseY >= yPosition && mouseY <= yPosition + height - 1;
				
				if(field_146123_n) {
					anim2.interpolate(200, 0, 4);
				} else { 
					anim2.interpolate(0, 0, 4);
				}
				
				anim = (int) (anim2.getX() / 10);
				
				double scaleValue = 100 - anim;
				scaleValue /= 100;
				
				int renderWidth = (int) (width * scaleValue);
				int renderHeight = (int) (height * scaleValue);
				int diffW = width - renderWidth;
				int diffH = height - renderHeight;
				
				double alpha = appearAnim.getX();
				alpha /= 100;
				
				RenderUtils.bindCachedResource(icon);
				GL11.glPushMatrix();
				{
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glColor4d(1, 1, 1, alpha);
					Gui.func_152125_a(this.xPosition + diffW / 2, this.yPosition + diffH / 2, 0, 0, iconWidth, iconHeight, renderWidth, renderHeight, iconWidth, iconHeight);
				}
				GL11.glPopMatrix();
			}
		}
	}

	private boolean skip;
	
	@Override
	public void onSetupDisplay() {
		appearAnim = new Translate(skip ? 100 : 0, 0);
		anim2 = new Translate(skip ? 1000 : 0, 0);
	}
	
	@Override
	public void onSetupSkip() {
		skip = true;
	}

}
