package me.xtrm.delta.client.gui.click.old.object;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;

public class Element {
	
	public double x, y, width, height;
	
	public Button parent;
	
	public Element(Button parent) {
		this.parent = parent;
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int count = 0;
		for(Element se : parent.elements) {
			if(se == this)
				break;
			count++;
		}
		
		int arrayWidth = 80;
		for(IModule m : DeltaAPI.getClient().getModuleManager().getModules()) 
			if(m.getCategory().isVisible()) 
				if(Fonts.fontHUD.getStringWidth(m.getName()) + 12 > arrayWidth) 
					arrayWidth = Fonts.fontHUD.getStringWidth(m.getName()) + 12;
		
		width = (parent.parent.parent.wx + parent.parent.parent.wwidth - 5) - (parent.parent.parent.wx + 5 + 80 + 5 + arrayWidth + 5);
		height = parent.height;
		x = (parent.parent.parent.wx + 5 + 80 + 5 + arrayWidth + 5);
		y = parent.parent.parent.wy + 20 + (count * height);
		
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY))
			Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, 0x55000000);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int state) {
		
	}

	public void mouseReleased(int mouseX, int mouseY) {
		
	}
	
	public void keyTyped(char charr, int keycode) {
	
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean isHoveredUp(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y - height && mouseY <= y;
	}
	
	public boolean isHovering(int x, int y, int x2, int y2, int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
	}

}