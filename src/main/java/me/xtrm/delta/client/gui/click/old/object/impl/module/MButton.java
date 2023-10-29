package me.xtrm.delta.client.gui.click.old.object.impl.module;

import java.awt.Color;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.element.GuiButtonElement;
import me.xtrm.delta.client.gui.click.old.element.KeybindElement;
import me.xtrm.delta.client.gui.click.old.element.setting.CheckboxElement;
import me.xtrm.delta.client.gui.click.old.element.setting.ComboElement;
import me.xtrm.delta.client.gui.click.old.element.setting.LabelElement;
import me.xtrm.delta.client.gui.click.old.element.setting.SliderElement;
import me.xtrm.delta.client.gui.click.old.element.setting.SpacerElement;
import me.xtrm.delta.client.gui.click.old.object.Button;
import me.xtrm.delta.client.gui.click.old.object.Element;
import me.xtrm.delta.client.gui.click.old.object.Panel;
import me.xtrm.delta.client.gui.xray.GuiBlacklist;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.TimeHelper;
import net.minecraft.client.gui.Gui;

public class MButton extends Button {
	
	public TimeHelper timer;
	public IModule mod;
	
	public MButton(IModule mod, Panel parent) {
		super(mod.getName(), parent);
		this.mod = mod;
		this.timer = new TimeHelper();
		
		if(DeltaAPI.getClient().getSettingManager().getSettingsForModule(mod) != null) {
			for(ISetting set : DeltaAPI.getClient().getSettingManager().getSettingsForModule(mod)) {
				if(set.isCheck()) {
					elements.add(new CheckboxElement(set, this));
				}else if(set.isSlider()) {
					elements.add(new SliderElement(set, this));
				}else if(set.isCombo()) {
					elements.add(new ComboElement(set, this));
				}else if(set.isLabel()) {
					elements.add(new LabelElement(set, this));
				}else if(set.isSpacer()) {
					elements.add(new SpacerElement(set, this));
				}
			}
		}
		if(mod.getName().equalsIgnoreCase("XRay")) {
			elements.add(new GuiButtonElement("Config", new GuiBlacklist(parent.parent), this));
		}
		if(mod.getName().equalsIgnoreCase("Spammer")) {
//			elements.add(new GuiDisplayElement("Set message", new GuiSpammerMsg(), this));
		}
		elements.add(new KeybindElement(this));
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		updatePosition();
		
		// Draw stuff
		if(selected)
			Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, new Color(0xFFA211A2).getRGB());
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY))
			Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, 0x55000000);
		
		Fonts.fontHUD.drawStringWithShadow(name, x + 2 + ((isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) ? 3 : 0), y + height / 2 - Fonts.fontHUD.getFontHeight() / 2, -1);
		Gui.drawRect((int)x + (int)width - 4, (int)y, (int)x + (int)width, (int)y + (int)height, mod.isEnabled() ? -12264636 : -2410183);
		
		// Draw elements
		if(selected)
			for(Element se : elements)
				se.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
			if(state == 1) { // toggle
				mod.toggle();
			}else if(state == 0) { // select
				for(Button mb : parent.elements)
					mb.selected = false;
				this.selected = true;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, state);
	}

}
