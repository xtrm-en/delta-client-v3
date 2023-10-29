package me.xtrm.delta.client.gui.click.old.element;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.gui.click.old.object.Element;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.PlayerUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

public class KeybindElement extends Element {

	private IModule mod;
	private boolean binding;
	private TimeHelper timer, timer2;
	
	public KeybindElement(MButton parent) {
		super(parent);
		this.mod = parent.mod;
		this.binding = false;
		this.timer = new TimeHelper();
		this.timer2 = new TimeHelper();
	}	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(binding) {
			timer.reset();
			if(timer2.hasReached(7000)) {
				binding = false;
			}
		}
		
		Gui.drawRect((int)x + 10, (int)y + 1, (int)x + (int)width - 10, (int)y + (int)height - 1, isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY) ? 0xFF3F3F3F : 0xFF2C2C2C);
		Gui.drawRect((int)x + 11, (int)y + 2, (int)x + (int)width - 11, (int)y + (int)height - 2, 0xFF0F0F0F);
		
		String s = "Keybind: " + (Keyboard.getKeyName(mod.getKey()).equalsIgnoreCase("NONE") ? "None" : Keyboard.getKeyName(mod.getKey()));
		
		if(binding)
			s = "Press a key...";
		else {
			if(!timer.hasReached(3000)) 
				s = "Bound to " + (Keyboard.getKeyName(mod.getKey()).equalsIgnoreCase("NONE") ? "None" : Keyboard.getKeyName(mod.getKey()));
		}
		
		Fonts.fontHUD.drawStringWithShadow(s, x + 10 + (((x + width - 10) - (x + 10)) / 2) - Fonts.fontHUD.getStringWidth(s) / 2, y + 1, -1);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
			if(state == 0) {
				if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					PlayerUtils.sendMessage("Module " + EnumChatFormatting.DARK_PURPLE + mod.getName() + EnumChatFormatting.GRAY + " unbind");
					mod.setKey(0);
					binding = false;
				} else {
					binding = true;
					timer2.reset();
				}
			}else {
				binding = false;
			}
		}else {
			binding = false;
		}
	}
	
	@Override
	public void keyTyped(char charr, int keycode) {
		if(binding) {
			binding = false;
			
			if(keycode <= 0)
				return;
			
			PlayerUtils.sendMessage("Module " + EnumChatFormatting.DARK_PURPLE + mod.getName() + EnumChatFormatting.GRAY + " bind sur " + EnumChatFormatting.DARK_PURPLE + Keyboard.getKeyName(keycode));
			mod.setKey(keycode);
		}
	}

}
