package me.xtrm.delta.client.gui.click.old.element.setting;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.object.impl.module.MButton;
import me.xtrm.delta.client.gui.click.old.object.impl.module.SElement;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.GuiUtils;

public class SliderElement extends SElement {

	private boolean dragging;
	
	public SliderElement(ISetting set, MButton parent) {
		super(set, parent);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		double firstX = x + 10,
			   lastX = x + width - 10,
			   widthX = (x + width - 10) - (x + 10);
		
		GuiUtils.getInstance().drawRect(firstX, y + 1, firstX + widthX, y + (int)height - 1, 0xFF555555);
		
		double mx = Math.min(Math.max(firstX, mouseX), lastX);
		
		double min = set.getSliderMin();
		double max = set.getSliderMax();

		double percentage = ((mx - firstX) / widthX) * 100;
		percentage *= 100;
		percentage = Math.round(percentage);
		percentage /= 100;
		
		if(dragging) {
			double val = ((percentage / 100) * (max - min)) + min;
			set.setSliderValue(set.isSliderOnlyInt() ? (int)val : val);
		}

		double val = set.getSliderValue();
		
		GuiUtils.getInstance().drawRect(firstX, y + 1, firstX + (((val - min) / (max - min)) * widthX), y + (int)height - 1, 0xFFA211A2);

		val *= 100;
		val = Math.round(val);
		val /= 100;
		
		String text = set.getDisplayName() + ": " + (set.isSliderOnlyInt() ? (int)val : val);
		Fonts.fontHUD.drawStringWithShadow(text, x + (width / 2) - (Fonts.fontHUD.getStringWidth(text) / 2), y + height / 2 - Fonts.fontHUD.getFontHeight() / 2, -1);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		super.mouseClicked(mouseX, mouseY, state);
		
		if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
			if(state == 0) {
				dragging = true;
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		super.mouseReleased(mouseX, mouseY);
		dragging = false;
	}
}
