package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.ReflectionHelper;
import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.render.EventRender2D;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.gui.click.old.ClickGUI;
import me.xtrm.delta.client.gui.click.old.object.Panel;
import me.xtrm.delta.client.gui.click.old.object.impl.console.ConsolePanel;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Module {

	public InventoryMove() {
		super("InventoryMove", Category.MOVEMENT);
		setDescription("Permet de bouger en étant dans les menus");
		
		registerSettings(new Setting("CameraMove", this, true));
	}
	
	private boolean screen;
	
	@Handler
	public void onUpdate(EventUpdate e) {
		if(mc.theWorld != null && mc.thePlayer != null && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiEditSign) && !(mc.currentScreen instanceof GuiCommandBlock)) {
			screen = true;
			if((mc.currentScreen instanceof ClickGUI)) {
				ClickGUI cgui = (ClickGUI)mc.currentScreen;
				for(Panel p : cgui.elements)
					if(p.selected) 
						if(p instanceof ConsolePanel)
							return;
			}
			update();
		}
		if(mc.currentScreen == null && screen) {
			screen = false;
			update();
		}
	}
	
	@Handler
	public void onRender2D(EventRender2D e) {
		if(mc.theWorld != null && mc.thePlayer != null && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiEditSign) && !(mc.currentScreen instanceof GuiCommandBlock)) {
			if(getSetting("CameraMove").getCheckValue()) {
				mc.thePlayer.rotationPitch += Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 3 : Keyboard.isKeyDown(Keyboard.KEY_UP) ? -3 : 0;
				mc.thePlayer.rotationYaw += Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 3 : Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? -3 : 0;
			}
		}
	}
	
	@Override
	public void onDisable() {
		update();
		super.onDisable();
	}
	
	private KeyBinding[] binds = new KeyBinding[] {
			mc.gameSettings.keyBindForward,
			mc.gameSettings.keyBindBack,
			mc.gameSettings.keyBindLeft,
			mc.gameSettings.keyBindRight,
			mc.gameSettings.keyBindJump,
			mc.gameSettings.keyBindSprint
	};
	
	public void update() {
		for(KeyBinding k : binds) {
			ReflectionHelper.setPrivateValue(KeyBinding.class, k, Keyboard.isKeyDown(k.getKeyCode()), "pressed", "field_74513_e");
		}
	}

}
