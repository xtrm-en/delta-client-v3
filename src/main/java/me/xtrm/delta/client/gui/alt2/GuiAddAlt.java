package me.xtrm.delta.client.gui.alt2;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public class GuiAddAlt extends GuiNormalizedScreen {
	
    private final GuiAltManager manager;
    
    private PasswordField password;
    private String status;
    private GuiTextField username;

    public GuiAddAlt(GuiAltManager manager) {
        this.manager = manager;
        this.status = EnumChatFormatting.GRAY + "Idle...";
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                AltLoginThread login = new AltLoginThread(this.username.getText(), this.password.getText(), AltManager.authType, true);
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
            }
        }
    }

    @Override
    public void drawScreen(int i2, int j2, float f2) {
    	Gui.drawRect(0, 0, width, height, 0xFF222222);
    	
        this.username.drawTextBox();
        this.password.drawTextBox();
        
        this.drawCenteredString(this.fontRendererObj, "Add Alt", width / 2, 10, -1);
        
        if (this.username.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "Username / E-Mail", width / 2, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "Password", width / 2, 106, -7829368);
        }
        this.drawCenteredString(this.fontRendererObj, this.status, width / 2, 20, -1);
        
        super.drawScreen(i2, j2, f2);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        this.username = new GuiTextField(this.mc.fontRenderer, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRenderer, width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
}