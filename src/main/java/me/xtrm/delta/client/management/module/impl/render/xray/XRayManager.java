package me.xtrm.delta.client.management.module.impl.render.xray;

import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.management.module.impl.render.XRay;
import me.xtrm.delta.wrapper.Wrap;

public class XRayManager {
	
	public static IModule xray = null;
	
	@Wrap(id = "shouldRender")
	public static boolean shouldRender(boolean orig, Object self, Object p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		if(xray == null)
			return orig;
		if(!xray.isEnabled()) {
			return orig;
		}
		return XRay.shouldRender(orig, self, p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
	}

}
