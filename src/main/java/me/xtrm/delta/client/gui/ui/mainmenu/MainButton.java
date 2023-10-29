package me.xtrm.delta.client.gui.ui.mainmenu;

import java.awt.Color;

import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.PolygonUtils;
import me.xtrm.delta.client.utils.PolygonUtils.Point;
import me.xtrm.delta.client.utils.animate.Translate;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.Minecraft;

public class MainButton extends ShowableButton {

	private Color buttonColor;
	private int decalage;
	
	private int anim;
	
	private Translate appearAnim;
	
	public MainButton(int id, int x, int y, int width, int height, String text, Color color, long timer) {
		super(id, x, y, width, height, text, timer);
		this.decalage = 20;
		this.buttonColor = color;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(this.visible) {	// GuiNewChat
			if(!this.enabled) {
				GuiUtils.getInstance().drawParallelogram(xPosition + decalage, yPosition, xPosition + width, yPosition, xPosition + width - decalage, yPosition + height, xPosition, yPosition + height, 0xDE000000);
				Fonts.font.drawStringWithShadow(
						displayString, 
						xPosition + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						yPosition + (height / 2) - (Fonts.font.getFontHeight() / 2), 
						-1);
			}else {
				if(appearAnim == null) return;
				appearAnim.interpolate(0, 0, 3);
				
				int x = (int) (xPosition + appearAnim.getX());
				int y = (int) (yPosition + appearAnim.getY());
				
				field_146123_n = PolygonUtils.isInside(new Point[] {
						new Point(x + decalage, y),
						new Point(x + width, y), 
						new Point(x + width - decalage, y + height), 
						new Point(x, y + height)
				}, new Point(mouseX, mouseY));
				
				for(int i = 0; i < 5; i++)
					if(anim < buttonColor.getAlpha())
						anim++;
				
				Color c = new Color(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), Math.min(buttonColor.getAlpha(), Math.min(buttonColor.getAlpha(), anim)));
				int ptdr = (int) ((double)((double)anim / (double)buttonColor.getAlpha()) * 255);
				
				GuiUtils.getInstance().drawParallelogram(x + decalage, y, x + width, y, x + width - decalage, y + height, x, y + height, field_146123_n ? c.brighter().getRGB() : c.getRGB());
				Fonts.font.drawStringWithShadow(
						displayString, 
						x + (width / 2) - (Fonts.font.getStringWidth(displayString) / 2), 
						y + (height / 2) - (Fonts.font.getFontHeight() / 2), 
						0x00FFFFFF + (ptdr << 24)
				);
			}
		}
	}

	private boolean skip;
	
	@Override
	public void onSetupDisplay() {
		appearAnim = new Translate(0, skip ? 0 : -20);
	}
	
	@Override
	public void onSetupSkip() {
		this.anim = buttonColor.getAlpha();
		skip = true;
	}
}
