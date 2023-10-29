package me.xtrm.delta.client.gui.alt2;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public final class GuiAltLogin extends GuiNormalizedScreen {
	
    private PasswordField password;
    private final GuiAltManager previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiAltManager previousScreen) {
        this.previousScreen = previousScreen;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.previousScreen);
                break;
            }
            case 0: {
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText(), AltManager.authType);
                this.thread.start();
            }
        }
    }

    @Override
    public void drawScreen(int x2, int y2, float z2) {
    	Gui.drawRect(0, 0, width, height, 0xFF222222);
    	
    	this.username.drawTextBox();
        this.password.drawTextBox();
        
        this.drawCenteredString(this.mc.fontRenderer, "Alt Login", width / 2, 10, -1);
        this.drawCenteredString(this.mc.fontRenderer, this.thread == null ? LoginStatus.IDLE.getText() : this.thread.getStatus().getText(), width / 2, 20, -1);
        if (this.username.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "Username / E-Mail", width / 2, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "Password", width / 2, 106, -7829368);
        }
        
        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, "Login"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
        this.username = new GuiTextField(this.mc.fontRenderer, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRenderer, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        super.keyTyped(character, key);
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        super.mouseClicked(x2, y2, button);
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}

