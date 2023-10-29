package me.xtrm.delta.client.gui.click.old.element.setting;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.gui.click.old.object.impl.module.SElement;
import me.xtrm.delta.client.management.module.impl.combat.TPAura;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;

public class ComboElement extends SElement {
	
	public ComboElement(ISetting set, MButton parent) {
		super(set, parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		String text = set.getDisplayName() + ": " + set.getComboValue();
		
		Gui.drawRect(((int)x + ((int)width / 2) - (Fonts.fontHUD.getStringWidth(text) / 2 + 7)), (int)y + 1, ((int)x + ((int)width / 2) + (Fonts.fontHUD.getStringWidth(text) / 2 + 8)), (int)y + (int)height - 1, isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY) ? 0xFF3F3F3F : 0xFF2C2C2C);
		Gui.drawRect(((int)x + ((int)width / 2) - (Fonts.fontHUD.getStringWidth(text) / 2 + 7)) + 1, (int)y + 2, ((int)x + ((int)width / 2) + (Fonts.fontHUD.getStringWidth(text) / 2 + 8)) - 1, (int)y + (int)height - 2, 0xFF0F0F0F);

		Fonts.fontHUD.drawStringWithShadow(text, x + 10 + (((x + width - 10) - (x + 10)) / 2) - Fonts.fontHUD.getStringWidth(text) / 2, y + 1, -1);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {	
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY) && state == 0) {
			this.set.setComboValue(this.set.getComboNextOption());
			
			if(this.set.getParent().getName().equalsIgnoreCase("TPAura")) {
				((TPAura)this.set.getParent()).updateDesc();
			}
		}
	}
}