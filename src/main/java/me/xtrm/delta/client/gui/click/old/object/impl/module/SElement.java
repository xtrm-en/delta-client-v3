package me.xtrm.delta.client.gui.click.old.object.impl.module;

import me.xtrm.delta.client.api.setting.ISetting;
import me.xtrm.delta.client.gui.click.old.object.Element;

public class SElement extends Element {
	
	public ISetting set;
	
	public SElement(ISetting set, MButton parent) {
		super(parent);
		this.set = set;
	}

}