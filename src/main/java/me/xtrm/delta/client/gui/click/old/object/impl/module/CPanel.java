package me.xtrm.delta.client.gui.click.old.object.impl.module;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.gui.click.old.ClickGUI;
import me.xtrm.delta.client.gui.click.old.object.Panel;

public class CPanel extends Panel {
	
	public Category cat;
	
	public CPanel(Category cat, ClickGUI parent) {
		super(cat.name(), parent);
		this.cat = cat;
		
		for(IModule m : DeltaAPI.getClient().getModuleManager().getModulesInCategory(cat))
			elements.add(new MButton(m, this));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int state) {
		if(state == 0) {
			if(isHovered(mouseX, mouseY) && !isHoveredUp(mouseX, mouseY)) {
				for(Panel cp : parent.elements)
					cp.selected = false;
				this.selected = true;
			}
		}
		super.mouseClicked(mouseX, mouseY, state);
	}
}
