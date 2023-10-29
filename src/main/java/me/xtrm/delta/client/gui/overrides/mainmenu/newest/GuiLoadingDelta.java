package me.xtrm.delta.client.gui.overrides.mainmenu.newest;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import me.xtrm.elf.pixies.killswitch.Killswitch;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiLoadingDelta extends GuiNormalizedScreen {
	
	private boolean displayKillswitched;
	private GuiScreen next;
	
	public GuiLoadingDelta(GuiScreen next) {
		this.next = next;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		displayKillswitched = Killswitch.isKillswitched();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {		
		ScaledResolution sr = ScaledUtils.gen();
		
		this.drawDefaultBackground();
		Gui.drawRect(-1, -1, sr.getScaledWidth() + 1, sr.getScaledHeight() + 1, 0xFF0A0A0A);

		if(displayKillswitched) {
			int i = 0;
			Fonts.fontLoadingGui.drawCenteredStringWithShadow("Version désactivée", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8, -1);
			Fonts.font.drawCenteredStringWithShadow("La version " + Consts.VER_STR + " de " + Consts.NAME + " Client n'est plus disponible.", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1); i++; i++;
			
			Fonts.font.drawCenteredStringWithShadow("Votre version actuelle de " + Consts.NAME + " a été desactivée", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1); i++;
			Fonts.font.drawCenteredStringWithShadow("pour des raisons de stabilité ou de sécurité.", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1); i++; i++;
			
			Fonts.font.drawCenteredStringWithShadow("Pour plus d'informations, rendez vous sur notre", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1); i++;
			Fonts.font.drawCenteredStringWithShadow("serveur discord publique:", sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1); i++;i++;
			
			Fonts.font.drawCenteredStringWithShadow(WebUtils.getDiscordUrl(), sr.getScaledWidth() / 2, sr.getScaledHeight() / 8 + Fonts.fontLoadingGui.getFontHeight() + (i * (Fonts.font.getFontHeight() + 2)), -1);
			
			return;
		} else {
			mc.displayGuiScreen(next);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

}
