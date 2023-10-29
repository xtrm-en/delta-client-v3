package me.xtrm.delta.client.gui.click.old.object.impl.console;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.gui.click.old.ClickGUI;
import me.xtrm.delta.client.gui.click.old.object.Panel;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;

public class ConsolePanel extends Panel implements ICommandListener {

	private GuiTextField textField;
	private List<String> msg;
	
	public ConsolePanel(ClickGUI parent) {
		super("Console", parent);
		textField = new GuiTextField(Fonts.mcFont, 0, 0, 200, 10);
		msg = new ArrayList<>();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(selected) {
			Gui.drawRect(parent.wx + 188, parent.wy + 20, parent.wx + parent.wwidth - 5, parent.wy + parent.wheight - 5, 0xCC111111);

			Gui.drawRect(parent.wx + 90, parent.wy + parent.wheight - 5 - 17, parent.wx + parent.wwidth - 5, parent.wy + parent.wheight - 5, Integer.MIN_VALUE);
			Gui.drawRect(parent.wx + 90, parent.wy + parent.wheight - 5 - 18, parent.wx + parent.wwidth - 5, parent.wy + parent.wheight - 5 - 17, 0xFFA211A2);
			Fonts.mcFont.drawStringWithShadow(">", (int)x + (int)width + 10, (int)y + (int)(height / 2) - Fonts.mcFont.FONT_HEIGHT / 2 - 1, -1);
			
			for(int i = msg.size(); i > 0; i--) {
				String s = msg.get(i - 1);
				Fonts.mcFont.drawStringWithShadow(s, parent.wx + 95, (int) (this.y - 14 - (msg.size() * Fonts.mcFont.FONT_HEIGHT) + (i * Fonts.mcFont.FONT_HEIGHT)), -1);
				if(i < msg.size() - 18)
					break;
			}
			
			textField.setFocused(true);
			textField.setEnableBackgroundDrawing(false);
			textField.setMaxStringLength(200);
			textField.drawTextBox();
		}
	}
	
	@Override
	public void updatePosition() {
		super.updatePosition();
		
		this.y = parent.wy + parent.wheight - 5 - this.height;
		
		this.textField.xPosition = (int)x + (int)width + 5 + 14;
		this.textField.yPosition = (int)y + (int)(height / 2) - 10 / 2;
		this.textField.height = (int)this.height;
		this.textField.width = ((parent.wx + parent.wwidth - 5) - (parent.wx + 105));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		super.mouseClicked(mouseX, mouseY, state);
		
		if(selected)
			textField.mouseClicked(mouseX, mouseY, state);
	}
	
	@Override
	public void keyTyped(char charr, int keycode) {
		if(selected) {
			if(keycode == Keyboard.KEY_RETURN) {
				String command = textField.getText();
				textField.setText("");
				DeltaAPI.getClient().getCommandManager().runCommand(this, command);
				return;
			}
			textField.textboxKeyTyped(charr, keycode);
		}
	}

	@Override
	public void print(String text) {
		if(text.contains("\n")) {
			String[] txt = text.split(Pattern.quote("\n"));
			for(String str : txt) {
				msg.add(str);
			}
		}else {
			msg.add(text);
		}
	}
	
	@Override
	public void clear() {
		msg.clear();
	}

}
