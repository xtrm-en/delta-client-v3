package me.xtrm.delta.client.gui.click.old.element.setting;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.gui.click.old.object.impl.module.SElement;
import me.xtrm.delta.client.utils.Fonts;
import net.minecraft.client.gui.Gui;

public class CheckboxElement extends SElement {

	public CheckboxElement(ISetting set, MButton parent) {
		super(set, parent);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Gui.drawRect((int)x + 3, (int)y + 2, (int)x + 3 + (int)(height - 4), (int)y + 2 + (int)(height - 4), 0xCC0B0B0B);
		if(set.getCheckValue())
//			GuiUtils.getInstance().drawCheck((int)x + 5, (int)y + 3, new Color(0xCCA211A2));
			Gui.drawRect((int)x + 4, (int)y + 3, (int)x + 4 + (int)(height - 6), (int)y + 3 + (int)(height - 6), 0xCCA211A2);
		Fonts.fontHUD.drawStringWithShadow(set.getDisplayName(), x + 18, y + height / 2 - Fonts.fontHUD.getFontHeight() / 2, -1);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) 
			if(state == 0) 
				set.setCheckValue(!set.getCheckValue());
		
		super.mouseClicked(mouseX, mouseY, state);
	}

}
