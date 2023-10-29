package me.xtrm.delta.factory;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import me.xtrm.delta.wrapper.Wrap;

public class DeltaDisplayFactory {
	
	@Wrap(id = "overrideDisplay")
	public static void overrideDisplay() {		
		if(Display.isFullscreen()) return;
		
		try {
			Display.setDisplayMode(new DisplayMode(854, 480));
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

}
