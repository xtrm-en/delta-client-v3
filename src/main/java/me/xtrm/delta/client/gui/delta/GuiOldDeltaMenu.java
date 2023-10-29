package me.xtrm.delta.client.gui.delta;

import me.xtrm.delta.client.gui.altmanager.GuiAltLogin;
import me.xtrm.delta.client.gui.overrides.mainmenu.newest.NewMainMenu;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.TimeHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class GuiOldDeltaMenu extends GuiScreen {
	
	private NewMainMenu prev;
	private TimeHelper timer;
	
	public GuiOldDeltaMenu(NewMainMenu prev) {
		this.prev = prev;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new UIRoundedButton(8, width / 2 - 100, height / 2 - 20 - 5 - 10, "Alt Login"));
		this.buttonList.add(new UIRoundedButton(9, width / 2 - 100, height / 2 - 10, "Credits"));
		this.buttonList.add(new UIRoundedButton(10, width / 2 - 100, height / 2 + 15, "Retour"));
//		this.buttonList.add(new UIButton(69, width / 2 - 100, height / 2 + 40, "FileManager saveFiles"));
//		this.buttonList.add(new UIButton(42, width / 2 - 100, height / 2 + 65, "FileManager loadFiles"));
		this.timer = new TimeHelper();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		this.drawDefaultBackground();
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF0a0a0a);
		
		String name = Consts.NAME.substring(0, 1) + EnumChatFormatting.WHITE + Consts.NAME.substring(1, Consts.NAME.length());
		Fonts.fontBig.drawStringWithShadow(
				name, 
				sr.getScaledWidth() / 2 - Fonts.fontBig.getStringWidth(Consts.NAME) / 2, 
				sr.getScaledHeight() / 2 - 110, 
				0xA211A2
		);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		switch(p_146284_1_.id) {
			case 8:
				mc.displayGuiScreen(new GuiAltLogin(this));
				break;
			case 9:
				mc.displayGuiScreen(new GuiOldCredits(this));
				break;
			case 10:
				mc.displayGuiScreen(prev);
				break;
		}
		super.actionPerformed(p_146284_1_);
	}

}
