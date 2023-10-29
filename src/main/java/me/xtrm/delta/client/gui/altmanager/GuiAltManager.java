package me.xtrm.delta.client.gui.altmanager;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.thealtening.auth.TheAlteningAuthentication;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.altlogin.GuiOldAltLogin;
import me.xtrm.delta.client.gui.delta.GuiNewDelta;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class GuiAltManager extends GuiNormalizedScreen {
	
	private static final CachedResource alteningLogo = new CachedResource("http://nkosmos.github.io/assets/altening.png");  
	public static final CachedResource pala_logo = new CachedResource("https://pbs.twimg.com/profile_images/1249367268162764800/nT0fW4I-_400x400.jpg"); 
    private static TheAlteningAuthentication authenticator = TheAlteningAuthentication.mojang();
    private GuiScreen prev;
    private static final double factor = 1319D / 1165D; // altening logo factor
	
	public GuiAltManager(GuiNewDelta guiNewDelta) {

	}

	@Override
	public void initGui() {
		super.initGui();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		this.buttonList.add(new UIRoundedButton(0, sr.getScaledWidth() - 196 - 25, 25, 192, 40, "+ Add Account"));
		this.buttonList.add(new UIRoundedButton(0, sr.getScaledWidth() - 196 - 25, 25 + 40 + 7 + 1 + 7, 192, 40, ""));
		this.buttonList.add(new UIRoundedButton(5, width / 2 - 250 / 2, sr.getScaledHeight() / 2 + (25 * 3), 250, 20, "Back"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = ScaledUtils.gen();
		Gui.drawRect(0, 0, this.width, this.height, 0xFF222222);
		Gui.drawRect(28, 216, 192, 256, 0xFF191919);
		
		Gui.drawRect(this.width - 196 - 25, 25 + 40 + 7, this.width, 73, 0xFFFFFFFF);
		Fonts.font.drawStringWithShadow("Serveur(s):", 28, 192, -1);

		RenderUtils.bindCachedResource(pala_logo);
    	func_152125_a(28 + 4, 216 + 4, 32, 32, 32, 32, 32, 32, 32, 32);
    	
    	Fonts.font.drawStringWithShadow("mc.paladium-pvp.fr", 28 + 32 + 4 + 1, 216 + 4, -1);
    	Fonts.font.drawStringWithShadow("Ban: False", 28 + 32 + 4 + 1, 216 + 4 + 16, -1);
		String username = mc.getSession().getUsername();
		
		mc.renderEngine.bindTexture(RenderUtils.loadHead());
		
		GL11.glPushMatrix();
		{
			GL11.glColor4d(1, 1, 1, 1);
			Gui.func_152125_a(25, 25, 0, 0, 64, 64, 64, 64, 64, 64);
		}
		GL11.glPopMatrix();
		
		
		boolean cracked = false;
		String str = "OfflinePlayer:" + mc.getSession().getUsername();
        UUID crackedUUID = UUID.nameUUIDFromBytes(str.getBytes());
        if(crackedUUID.equals(mc.getSession().func_148256_e().getId())) {
        	cracked = true;
        }
		
        Fonts.font.drawStringWithShadow(username, 94, 25, -1);
		Fonts.font.drawStringWithShadow("Type: " + EnumChatFormatting.GRAY + (cracked ? "Cracked" : "Premium"), 94, 50, -1);
//		Gui.drawRect(p_73734_0_, p_73734_1_, p_73734_2_, p_73734_3_, p_73734_4_);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawRect(this.width - 196 - 25, 25 + 40 + 7 + 1 + 7, this.width - 196 - 24, 25 + 40 + 7 + 1 + 7 + 40, 0xFF00CE18);
		Fonts.font.drawStringWithShadow(username,this.width - 196 - 25 + 32 + 8, 25 + 40 + 7 + 1 + 7 + 3, -1);
		Fonts.font.drawStringWithShadow("Type: " + EnumChatFormatting.GRAY + (cracked ? "Cracked" : "Premium") ,this.width - 196 - 25 + 32 + 8, 25 + 40 + 7 + 1 + 7 + 3 + 15, -1);
		
		mc.renderEngine.bindTexture(RenderUtils.loadHead());
		GL11.glPushMatrix();
		{
			GL11.glColor4d(1, 1, 1, 1);
			Gui.func_152125_a(this.width - 196 - 25 + 4, 25 + 40 + 7 + 1 + 7 + 4, 0, 0, 32, 32, 32, 32, 32, 32);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			mc.displayGuiScreen(new GuiOldAltLogin(this));
		}
		else if (button.id == 5) {
            mc.displayGuiScreen(prev);
        }
	}

}
