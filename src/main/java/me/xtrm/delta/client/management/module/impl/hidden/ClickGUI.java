package me.xtrm.delta.client.management.module.impl.hidden;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.HIDDEN);
		this.setVisible(false);
	}
	
	private GuiScreen clickGui;
	
	@Override
	public void onToggle() {	
		if(mc.theWorld == null)
			return;
		
		clickGui = new me.xtrm.delta.client.gui.click.old.ClickGUI();
		mc.displayGuiScreen(clickGui);
		
		super.onToggle();
	}

}