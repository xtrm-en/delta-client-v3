package me.xtrm.delta.client.gui.altlogin;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.ui.CErrorScreen;
import me.xtrm.delta.client.gui.ui.PasswordField;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.reflect.ReflectedClass;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;

public class GuiOldAltLogin extends GuiNormalizedScreen {
	
    private GuiTextField usernameField, usernamePasswordField;
    private PasswordField passwordField;
    
    private GuiScreen prev;

    public GuiOldAltLogin(GuiScreen prev) {
    	this.prev = prev;
    }
    
    private static final CachedResource alteningLogo = new CachedResource("https://nkosmos.github.io/assets/altening.png");    
    private static final CachedResource mojangLogo = new CachedResource("https://nkosmos.github.io/assets/mojang.png");
    private static TheAlteningAuthentication authenticator = TheAlteningAuthentication.mojang();
    
    private static final double factor = 1319D / 1165D; // altening logo factor
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		this.drawDefaultBackground();
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF090909);
		
		boolean cracked = false;
		String str = "OfflinePlayer:" + mc.getSession().getUsername();
        UUID crackedUUID = UUID.nameUUIDFromBytes(str.getBytes());
        if(crackedUUID.equals(mc.getSession().func_148256_e().getId())) {
        	cracked = true;
        }
        
        // "C'est pas évident"
        // e.setTo(e.getFrom());
		
        Fonts.font.drawStringWithShadow(mc.getSession().getUsername(), 94, 25, -1);
        Fonts.font.drawStringWithShadow("Type: " + EnumChatFormatting.GRAY + (cracked ? "Cracked" : "Premium"), 94, 50, -1);
        
        Fonts.font.drawStringWithShadow("Authentification: " + EnumChatFormatting.GRAY + authenticator.getService(), 2, sr.getScaledHeight() - 16, -1);
        mc.renderEngine.bindTexture(RenderUtils.loadHead());
        GL11.glPushMatrix();
		{
			GL11.glColor4d(1, 1, 1, 1);
			Gui.func_152125_a(25, 25, 0, 0, 64, 64, 64, 64, 64, 64);
		}
		GL11.glPopMatrix();
        
        boolean hovered = mouseX > sr.getScaledWidth() - 69 - 9 && mouseY < (int) (64*factor + 5 + 5);
        
        if(hovered)
        	Gui.drawRect(sr.getScaledWidth() - 69 - 9, 0, sr.getScaledWidth(), (int) (64*factor + 5 + 5), 0xCC000000);
        
        GL11.glPushMatrix();
        {
        	GL11.glEnable(GL11.GL_BLEND);
        	GL11.glColor4d(1, 1, 1, 1);
        	if(authenticator.getService() == AlteningServiceType.MOJANG) {
        		RenderUtils.bindCachedResource(mojangLogo);
            	func_152125_a(sr.getScaledWidth() - 69, 5, 0, 0, 128, 128, 64, (int) (64), 128, 128);
        	}else {
        		RenderUtils.bindCachedResource(alteningLogo);
            	func_152125_a(sr.getScaledWidth() - 69, 5, 0, 0, 1165, 1319, 64, (int) (64 * factor), 1165, 1319);
        	}
        }
        GL11.glPopMatrix();
        
        Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 80, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 - 80 + 20, 0xCC000000);
        Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 80 + 19, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 - 80 + 20, 0xFFA211A2);
        
        if(authenticator.getService() == AlteningServiceType.MOJANG) {
        	Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 40, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 - 40 + 20, 0xCC000000);
            Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 + 20, 0xCC000000);
            Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 40 + 19, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 - 40 + 20, 0xFFA211A2);
            Gui.drawRect(width / 2 - 250 / 2, sr.getScaledHeight() / 2 + 19, width / 2 - 250 / 2 + 250, sr.getScaledHeight() / 2 + 20, 0xFFA211A2);
        	
        	Fonts.font.drawStringWithShadow("Mail:", width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 93 - 2, -1);
            usernameField.drawTextBox();
            Fonts.font.drawStringWithShadow("Password:", width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 93 + 40 - 2, -1);
            passwordField.drawTextBox();
            Fonts.font.drawStringWithShadow("Combo (mail:pass):", width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 93 + 40 + 40 - 2, -1);
            usernamePasswordField.drawTextBox();
        }else {
        	Fonts.font.drawStringWithShadow("Token:", width / 2 - 250 / 2, sr.getScaledHeight() / 2 - 93 - 2, -1);
            usernameField.drawTextBox();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) {
        	if(authenticator.getService() == AlteningServiceType.THEALTENING) {
        		loginAltening();
        	}else {
        		login();
        	}
        }

        if (button.id == 4) {
            String data;
            try {
                data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return;
            }
            if (data != null) {
            	if(authenticator.getService() == AlteningServiceType.THEALTENING) {
            		usernameField.setText(data);
            		loginAltening();
            	}else {
            		usernamePasswordField.setText(data);
                    login();
                    usernamePasswordField.setText("");
            	}
            }
        }

        if (button.id == 5) {
            mc.displayGuiScreen(prev);
        }

        super.actionPerformed(button);
    }
    
    private void loginAltening() {
    	Session session = null;
    	
//    	try {
//			session = AuthenticationHelper.createPremiumSession(usernameField.getText(), "a");
//		} catch (AuthenticationException e) {
//			mc.displayGuiScreen(new CErrorScreen(this, "Login Error", e.getMessage().replace("username", "token")));
//			return;
//		}
    	
    	if(session == null) {
    		mc.displayGuiScreen(new CErrorScreen(this, "Login Error", "Null session"));
    		return;
        }
    	 
        try {
        	boolean obf = !((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"));
            ReflectedClass rc = ReflectedClass.of(Minecraft.getMinecraft());
            rc.set0(obf ? "field_71449_j" : "session", session);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

	private void login() {    	
        if (usernamePasswordField.getText().length() != 0 && usernamePasswordField.getText().contains(":") && !usernamePasswordField.getText().endsWith(":")) {
            usernameField.setText(usernamePasswordField.getText().split(":")[0]);
            passwordField.setText(usernamePasswordField.getText().split(":")[1]);
            usernamePasswordField.setText("");
        }
        
        if(usernameField.getText().equalsIgnoreCase("")) return;
        
        Session session = null;
//        if(passwordField.getText().equalsIgnoreCase("")) {
//        	if(usernameField.getText().contains("@")) {
//            	mc.displayGuiScreen(new CErrorScreen(this, "Login Error", "Invalid login credentials"));
//            	return;
//            } else {
//            	session = AuthenticationHelper.createCrackedSession(usernameField.getText());
//            }
//        }else {
//        	try {
//				session = AuthenticationHelper.createPremiumSession(usernameField.getText(), passwordField.getText());
//			} catch (AuthenticationException e) {
//				mc.displayGuiScreen(new CErrorScreen(this, "Login Error", e.getMessage()));
//				return;
//			}
//        }
        
        if(session == null) {
        	mc.displayGuiScreen(new CErrorScreen(this, "Login Error", "Null session"));
			return;
        }
        
        try {
        	boolean obf = !((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"));
            ReflectedClass rc = ReflectedClass.of(Minecraft.getMinecraft());
            rc.set0(obf ? "field_71449_j" : "session", session);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        usernameField.textboxKeyTyped(typedChar, keyCode);
        if(authenticator.getService() == AlteningServiceType.MOJANG) {
        	passwordField.textboxKeyTyped(typedChar, keyCode);
            usernamePasswordField.textboxKeyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    	boolean hovered = mouseX > ScaledUtils.gen().getScaledWidth() - 69 - 9 && mouseY < (int) (64*factor + 5 + 5);
        if(hovered) {
        	authenticator.updateService(authenticator.getService() == AlteningServiceType.MOJANG ? AlteningServiceType.THEALTENING : AlteningServiceType.MOJANG);
        	mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        	return;
        }
    	
        usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        if(authenticator.getService() == AlteningServiceType.MOJANG) {
        	passwordField.mouseClicked(mouseX, mouseY, mouseButton);
            usernamePasswordField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private UIRoundedButton clipboard;
    
	@Override
    public void initGui() {
		super.initGui();
		
    	ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    	
        usernameField = new GuiTextField(mc.fontRenderer, width / 2 - 250 / 2 + 5, sr.getScaledHeight() / 2 - 80 + 6, 240, 20);
        usernameField.setMaxStringLength(100);
        usernameField.setEnableBackgroundDrawing(false);
        passwordField = new PasswordField(mc.fontRenderer, width / 2 - 250 / 2 + 5, sr.getScaledHeight() / 2 - 40 + 6, 250, 20);
        passwordField.setMaxStringLength(100);
        passwordField.setEnableBackgroundDrawing(false);
        usernamePasswordField = new GuiTextField(mc.fontRenderer, width / 2 - 250 / 2 + 5, sr.getScaledHeight() / 2 + 6, 250, 20);
        usernamePasswordField.setMaxStringLength(190);
        usernamePasswordField.setEnableBackgroundDrawing(false);
        
        buttonList.add(new UIRoundedButton(3, width / 2 - 250 / 2, sr.getScaledHeight() / 2 + (25 * 1), 250, 20, "Login"));
        buttonList.add(clipboard = new UIRoundedButton(4, width / 2 - 250 / 2, sr.getScaledHeight() / 2 + (25 * 2), 250, 20, "Clipboard"));
        buttonList.add(new UIRoundedButton(5, width / 2 - 250 / 2, sr.getScaledHeight() / 2 + (25 * 3), 250, 20, "Back"));
    }
}