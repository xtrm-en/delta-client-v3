package me.xtrm.delta.client.gui.ui;

import java.awt.Color;

import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

/**
 * Custom GuiButton.
 */
public class AnimatedButton extends GuiButton {
		
	private int alpha, yOffset;
	
	public AnimatedButton(int id, int x, int y, int width, int height, String text) { super(id, x, y, width, height, text); alpha = 100; }
	public AnimatedButton(int id, int x, int y, String text) { super(id, x, y, text); alpha = 100; }

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(this.visible) {
			if(!this.enabled) {
				Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xDE000000);
				Fonts.font.drawStringWithShadow(
						displayString, 
						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2), 
						-1);
			}else {
				boolean hovering = mouseX > this.xPosition && mouseX < this.xPosition + this.width && mouseY > this.yPosition - yOffset && mouseY < this.yPosition + this.height;
				
				alpha += hovering ? ((alpha < 130) ? 10 : 0) : ((alpha > 0) ? -10 : 0);
				yOffset += hovering ? ((yOffset < 4) ? 1 : 0) : ((yOffset > 0) ? -1 : 0);
				
				Color color = new Color(0, 0, 0, alpha);
				GuiUtils.getInstance().drawRoundedRect(xPosition, yPosition - yOffset, xPosition + width, yPosition + height - yOffset, color.getRGB(), color.getRGB());
				Fonts.font.drawStringWithShadow(
						displayString, 
						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2) - yOffset, 
						-1);
			}
		}
	}

}
