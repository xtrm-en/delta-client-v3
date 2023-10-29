package me.xtrm.delta.client.gui.delta;

import me.xtrm.delta.client.gui.overrides.mainmenu.old.CMainMenu;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class GuiOldCredits extends GuiScreen {
	
	private GuiScreen prev;
	private TimeHelper timer;
	
	public GuiOldCredits(GuiScreen prev) {
		this.prev = prev;
		this.timer = new TimeHelper();
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new UIRoundedButton(69, width / 2 - 100, height / 2 + 85, "Retour"));
		super.initGui();
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		this.drawDefaultBackground();
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF0f0f0f);
		
		if(timer.hasReached(50)) {
			CMainMenu.particleSystem.tick(50);
			timer.reset();
		}
		CMainMenu.particleSystem.render(1, p_73863_1_, p_73863_2_);
		
		GuiUtils.getInstance().drawRoundedRect(sr.getScaledWidth() / 2 - 208, sr.getScaledHeight() / 2 - 115, sr.getScaledWidth() / 2 + 208, sr.getScaledHeight() / 2 + 115, 0x3F000000, 0x3F000000);
		
		String name = Consts.NAME.substring(0, 1) + EnumChatFormatting.WHITE + Consts.NAME.substring(1, Consts.NAME.length());
		Fonts.fontBig.drawStringWithShadow(
				name, 
				sr.getScaledWidth() / 2 - Fonts.fontBig.getStringWidth(Consts.NAME) / 2, 
				sr.getScaledHeight() / 2 - 115, 
				0xA211A2
		);
		
		Fonts.font.drawCenteredStringWithShadow("Delta Client rel-(build:" + Consts.VER + " tag:" + Consts.VER_TYPE + ")", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 50 - 10, -1);
		Fonts.font.drawCenteredStringWithShadow("(c) Copyright nKosmos - 2020", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 40 - 10, -1);
		Fonts.font.drawCenteredStringWithShadow("Delta Client est la propriété de nKosmos", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 30 - 10, -1);
		
		Fonts.font.drawCenteredStringWithShadow("Le développeur et le créateur", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - 10 - 10, -1);
		Fonts.font.drawCenteredStringWithShadow("de Delta Client est " + EnumChatFormatting.RED + "xTrM_", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 0 - 10, -1);
		
		Fonts.font.drawCenteredStringWithShadow("Libraries utilisées: Baritone (leijurv), java-discord-rpc (minnced)", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 20 - 10, -1);
		Fonts.font.drawCenteredStringWithShadow("Les droits de ces libraries vont vers leurs auteurs originels", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 30 - 10, -1);
		
		Fonts.font.drawCenteredStringWithShadow("Site de nKosmos: " + EnumChatFormatting.RED + WebUtils.getWebsite(), sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 60 - 10, -1);
		Fonts.font.drawCenteredStringWithShadow("Le serveur Discord de xTrM_: " + EnumChatFormatting.BLUE + WebUtils.getDiscordUrl(), sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 70 - 10, -1);
		
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if(p_146284_1_.id == 69) {
			mc.displayGuiScreen(prev);
		}
		super.actionPerformed(p_146284_1_);
	}

}
