package me.xtrm.delta.client.gui.click.old.object;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.gui.click.old.ClickGUI;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;

public class Panel {
	
	public double x, y, width, height;
	
	public List<Button> elements;
	
	public String name;
	public ClickGUI parent;
	
	public boolean selected;
	
	public Panel(String name, ClickGUI parent) {
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
			for(Button b : elements) 
				b.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void updatePosition() {
		// Update pos
		int count = 0;
		for(Panel p : parent.elements) {
			if(p == this)
				break;
			count++;
		}
				
		this.width = 80;
		this.height = 13;
		this.x = parent.wx + 5;
		this.y = parent.wy + 20 + (count) * this.height;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(state == 0) {
			if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
				for(Panel p : parent.elements)
					p.selected = false;
				this.selected = true;
			}
		}
		
		if(selected)
			for(Button b : elements) 
				b.mouseClicked(mouseX, mouseY, state);
	}

	public void mouseReleased(int mouseX, int mouseY) {
		if(selected)
			for(Button b : elements) 
				b.mouseReleased(mouseX, mouseY);
	}
	
	public void keyTyped(char charr, int keycode) {
		if(selected)
			for(Button b : elements) 
				b.keyTyped(charr, keycode);
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
