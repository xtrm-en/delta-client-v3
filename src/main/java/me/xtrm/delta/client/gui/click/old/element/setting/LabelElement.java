package me.xtrm.delta.client.gui.click.old.element.setting;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.gui.click.old.object.impl.module.SElement;
import me.xtrm.delta.client.utils.Fonts;

public class LabelElement extends SElement {

	public LabelElement(ISetting set, MButton parent) {
		super(set, parent);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Fonts.fontHUD.drawStringWithShadow(set.getLabelString(), x + 3, y + height / 2 - Fonts.fontHUD.getFontHeight() / 2 + 1, -1);
	}

}
