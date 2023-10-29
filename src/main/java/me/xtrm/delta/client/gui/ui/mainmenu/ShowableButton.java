package me.xtrm.delta.client.gui.ui.mainmenu;

import me.xtrm.delta.client.utils.TimeHelper;
import net.minecraft.client.gui.GuiButton;

public abstract class ShowableButton extends GuiButton {

	private TimeHelper showTimer;
	private boolean setup, skip;
	private long setupDelay;
	
	public ShowableButton(int id, int x, int y, int width, int height, String text, long showDelay) {
		super(id, x, y, width, height, text);
		this.setupDelay = showDelay;
	}
	
	public ShowableButton(int id, int x, int y, String text, long showDelay) {
		this(id, x, y, 100, 100, text, showDelay);
	}
	
	public void setupDisplay() {
		if(setup) return;
		
		if(showTimer == null) {
			showTimer = new TimeHelper();
			showTimer.reset();
		}
		
		if(!showTimer.hasReached(skip ? 0 : setupDelay)) return;
		
		setup = true;
		onSetupDisplay();
	}
	
	protected abstract void onSetupDisplay();
	
	public void setupSkip() {
		skip = true;
		onSetupSkip();
	}
	
	protected abstract void onSetupSkip();

}
