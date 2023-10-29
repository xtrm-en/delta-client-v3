package me.xtrm.delta.client.gui.click.old.object;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;

public class Button {
	
	public double x, y, width, height;
	
	public List<Element> elements;
	
	public String name;
	public Panel parent;
	
	public boolean selected;
	
	public Button(String name, Panel parent) {
		this.name = name;
		this.parent = parent;
		
		this.elements = new ArrayList<>();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		updatePosition();
		
		// Draw stuff
		if(selected)
			Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, new Color(0xFFA211A2).getRGB());
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY))
			Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, 0x55000000);
		
		Fonts.fontHUD.drawStringWithShadow(name, x + 2 + ((isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) ? 3 : 0), y + height / 2 - Fonts.fontHUD.getFontHeight() / 2, -1);
		
		// Draw elements
		if(selected)
			for(Element se : elements)
				se.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void updatePosition() {
		// Update pos
		int count = 0;
		for(Button b : parent.elements) {
			if(b == this)
				break;
			count++;
		}
				
		int arrayWidth = (int) parent.width;
		for(IModule m : DeltaAPI.getClient().getModuleManager().getModules()) 
			if(m.getCategory().isVisible()) 
				if(Fonts.fontHUD.getStringWidth(m.getName()) + 12 > arrayWidth) 
					arrayWidth = Fonts.fontHUD.getStringWidth(m.getName()) + 12;
				
		width = arrayWidth;
		height = 13;
		x = parent.x + parent.width + 5;
		y = parent.parent.wy + 20 + (count * height);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
			if(state == 0) { // select
				for(Button b : parent.elements)
					b.selected = false;
				this.selected = true;
			}
		}
		
		if(selected)
			for(Element se : elements)
				se.mouseClicked(mouseX, mouseY, state);
	}

	public void mouseReleased(int mouseX, int mouseY) {
		if(selected)
			for(Element se : elements)
				se.mouseReleased(mouseX, mouseY);
	}
	
	public void keyTyped(char charr, int keycode) {
		if(selected)
			for(Element se : elements)
				se.keyTyped(charr, keycode);
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
