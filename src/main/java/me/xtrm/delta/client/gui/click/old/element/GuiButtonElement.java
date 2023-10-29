package me.xtrm.delta.client.gui.click.old.element;

import me.xtrm.delta.client.gui.click.old.object.Button;
import me.xtrm.delta.client.gui.click.old.object.Element;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.Wrapper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class GuiButtonElement extends Element {

	private String text;
	private GuiScreen gui;
	
	public GuiButtonElement(String text, GuiScreen gui, Button parent) {
		super(parent);
		
		this.text = text;
		this.gui = gui;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawRect(((int)x + ((int)width / 2) - (Fonts.fontHUD.getStringWidth(text) / 2 + 7)), (int)y + 1, ((int)x + ((int)width / 2) + (Fonts.fontHUD.getStringWidth(text) / 2 + 8)), (int)y + (int)height - 1, isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY) ? 0xFF3F3F3F : 0xFF2C2C2C);
		Gui.drawRect(((int)x + ((int)width / 2) - (Fonts.fontHUD.getStringWidth(text) / 2 + 7)) + 1, (int)y + 2, ((int)x + ((int)width / 2) + (Fonts.fontHUD.getStringWidth(text) / 2 + 8)) - 1, (int)y + (int)height - 2, 0xFF0F0F0F);

		Fonts.fontHUD.drawStringWithShadow(text, x + 10 + (((x + width - 10) - (x + 10)) / 2) - Fonts.fontHUD.getStringWidth(text) / 2, y + 2, -1);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		super.mouseClicked(mouseX, mouseY, state);
		
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) 
			if(state == 0) 
				Wrapper.mc.displayGuiScreen(gui);
	}

}
