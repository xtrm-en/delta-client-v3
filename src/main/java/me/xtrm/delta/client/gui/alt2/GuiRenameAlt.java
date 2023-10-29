package me.xtrm.delta.client.gui.alt2;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public class GuiRenameAlt extends GuiNormalizedScreen {
	
    private final GuiAltManager manager;
    private GuiTextField nameField;
    private PasswordField pwField;
    private LoginStatus status = LoginStatus.WAITING;

    public GuiRenameAlt(GuiAltManager manager) {
        this.manager = manager;
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 0: {
                this.manager.selectedAlt.setDisplayName(this.nameField.getText());
                this.manager.selectedAlt.setPassword(this.pwField.getText());
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
    	Gui.drawRect(0, 0, width, height, 0xFF222222);
        
        this.drawCenteredString(this.fontRendererObj, "Edit Alt", width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.status.getText(), width / 2, 20, -1);
        
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        if (this.nameField.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "New name", width / 2, 66, -7829368);
        }
        if (this.pwField.getText().isEmpty()) {
            this.drawCenteredString(this.mc.fontRenderer, "New password", width / 2, 106, -7829368);
        }
        super.drawScreen(par1, par2, par3);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 92 + 12, "Edit"));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 116 + 12, "Cancel"));
        this.nameField = new GuiTextField(this.mc.fontRenderer, width / 2 - 100, 60, 200, 20);
        this.pwField = new PasswordField(this.mc.fontRenderer, width / 2 - 100, 100, 200, 20);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        this.nameField.textboxKeyTyped(par1, par2);
        this.pwField.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.nameField.isFocused() || this.pwField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
            this.pwField.setFocused(!this.pwField.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        this.nameField.mouseClicked(par1, par2, par3);
        this.pwField.mouseClicked(par1, par2, par3);
    }
}

