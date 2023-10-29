package me.xtrm.delta.client.gui.ui;

import java.awt.Color;

import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;

/**
 * Custom GuiButton.
 */
public class UIRoundedButton extends GuiButton {
		
	private int alpha;
	
	public UIRoundedButton(int id, int x, int y, int width, int height, String text) { super(id, x, y, width, height, text); alpha = 80; }
	public UIRoundedButton(int id, int x, int y, String text) { super(id, x, y, text); alpha = 80; }

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(this.visible) {
			if(!this.enabled) {
				GuiUtils.getInstance().drawRoundedRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xDE000000, 0xDE000000);
				Fonts.font.drawStringWithShadow(
						EnumChatFormatting.DARK_GRAY + displayString, 
						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2), 
						-1);
			}else {
				boolean hovering = mouseX > this.xPosition && mouseX < this.xPosition + this.width && mouseY > this.yPosition && mouseY < this.yPosition + this.height;
				
				alpha += hovering ? ((alpha < 130) ? 10 : 0) : ((alpha > 80) ? -10 : 0);
				Color color = new Color(0, 0, 0, alpha);
				GuiUtils.getInstance().drawRoundedRect(xPosition, yPosition, xPosition + width, yPosition + height, color.getRGB(), color.getRGB());
				Fonts.font.drawStringWithShadow(
						displayString, 
						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2), 
						-1);
			}
		}
	}

}
