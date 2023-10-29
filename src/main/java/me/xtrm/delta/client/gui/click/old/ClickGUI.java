package me.xtrm.delta.client.gui.click.old;

import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.gui.click.old.object.Button;
import me.xtrm.delta.client.gui.click.old.object.Panel;
import me.xtrm.delta.client.gui.click.old.object.impl.console.ConsolePanel;
import me.xtrm.delta.client.gui.click.old.object.impl.module.CPanel;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.ColorUtils;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGUI extends GuiScreen implements GuiYesNoCallback {

	public int wx, wy, wwidth, wheight;
	private int dragX, dragY;
	
	private boolean dragging;
	
	public List<Panel> elements;
	
	public ClickGUI() {
		elements = new ArrayList<>();
		
		for(Category c : Category.values())  
			if(c.isVisible()) 
				elements.add(new CPanel(c, this));
		
//		elements.add(new RadioPanel(this));
		elements.add(new ConsolePanel(this));
		
		ScaledResolution sr = ScaledUtils.gen();
		wwidth = 400;
		wheight = 230;
		wx = sr.getScaledWidth() / 2 - wwidth / 2;
		wy = sr.getScaledHeight() / 2 - wheight / 2; 
	}
	
	@Override
	public void initGui() {		
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.dragging) {
			wx = dragX + mouseX;
			wy = dragY + mouseY;
		}
		
		Gui.drawRect(wx, wy, wx + wwidth, wy + wheight, 0xDD000000);
		
		int arrayWidth = 80;
		for(IModule m : DeltaAPI.getClient().getModuleManager().getModules()) 
			if(m.getCategory().isVisible()) 
				if(Fonts.fontHUD.getStringWidth(m.getName()) + 12 > arrayWidth) 
					arrayWidth = Fonts.fontHUD.getStringWidth(m.getName()) + 12;
		
		Gui.drawRect(wx + 5, wy + 5 + 15, wx + 5 + 80, wy + wheight - 5, 0xCC111111);
		
		boolean panelSelected = false, buttonSelected = false;
		for(Panel p : elements) {
			if(p.selected) {
				panelSelected = true;
				for(Button b : p.elements) {
					if(b.selected) {
						buttonSelected = true;
					}
				}
			}
		}
		
		if(panelSelected)
			Gui.drawRect(wx + 5 + 80 + 5, wy + 5 + 15, wx + 5 + 80 + 5 + arrayWidth, wy + wheight - 5, 0xCC111111);
		if(buttonSelected)
			Gui.drawRect(wx + 5 + 80 + 5 + arrayWidth + 5, wy + 5 + 15, wx + wwidth - 5, wy + wheight - 5, 0xCC111111);
		
		Gui.drawRect(wx + 5, wy + 18, wx + wwidth - 5, wy + 18 + 1, ColorUtils.effect(0, 0.65F, 1).getRGB());
		
		Fonts.font.drawStringWithShadow(Consts.NAME + " " + Consts.VER_STR + " by xTrM_", wx + wwidth / 2 - Fonts.font.getStringWidth(Consts.NAME + " " + Consts.VER_STR + " by xTrM_") / 2, wy + 3, -1);
		
		if(isHovering(wx + wwidth - 23, wy + 3, wx + wwidth - 8, wy + 17, mouseX, mouseY))
			Gui.drawRect(wx + wwidth - 23, wy + 3, wx + wwidth - 8, wy + 17, 0xAA000000);
		Fonts.font.drawStringWithShadow("x", wx + wwidth - 20, wy + 2, -1);
		
		for(Panel cp : elements) {
			cp.drawScreen(mouseX, mouseY, partialTicks);
		}
		
		for(Panel p : elements) {
			if(!p.selected)
				continue;
			for(Button b : p.elements) {
				if(b instanceof MButton) {
					MButton mb = (MButton)b;
					if(mb.isHovered(mouseX, mouseY) && !mb.isHoveredUp(mouseX, mouseY)) {
						if(mb.timer.hasReached(250)) {
							String text = mb.mod.getDescription();
							GuiUtils.getInstance().drawRect(mb.x + mb.width + 5, mb.y, mb.x + mb.width + 7 + Fonts.fontHUD.getStringWidth(text) + 5, mb.y + mb.height, 0xEE0C0C0C);
							Fonts.fontHUD.drawStringWithShadow(text, mb.x + mb.width + 7, mb.y + mb.height / 2 - Fonts.fontHUD.getFontHeight() / 2, -1);
						}
					}else {
						mb.timer.reset();
					}
				}
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int state) {
		if(isHovering(wx + wwidth - 23, wy + 3, wx + wwidth - 8, wy + 17, mouseX, mouseY) && state == 0) {
			this.dragging = false;
			mc.displayGuiScreen(null);
		}else if(isHovering(wx, wy, wx + wwidth, wy + 20, mouseX, mouseY) && state == 0) {
			dragX = wx - mouseX;
			dragY = wy - mouseY;
			this.dragging = true;
		}
		
		for(Panel cp : elements) {
			cp.mouseClicked(mouseX, mouseY, state);
		}
		super.mouseClicked(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		if(state != -1)
			mouseReleased(mouseX, mouseY);
		super.mouseMovedOrUp(mouseX, mouseY, state);
	}
	
	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;
		for(Panel cp : elements) {
			cp.mouseReleased(mouseX, mouseY);
		}
	}
	
	@Override
	protected void keyTyped(char charr, int keycode) {			
		for(Panel cp : elements) {
			cp.keyTyped(charr, keycode);
		}
		super.keyTyped(charr, keycode);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public boolean isHovering(int x, int y, int x2, int y2, int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
	}
	
}