package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.render.EventRender3D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.EncryptionHelper;
import me.xtrm.delta.client.utils.Location;
import me.xtrm.delta.client.utils.render.ColorUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import net.minecraft.tileentity.TileEntity;

public class ChestESP extends Module {

	public ChestESP() {
		super("ChestESP", Category.RENDER);
		setDescription("Dessine une boite autour des coffres");
	}

	@Handler
	public void onRender3D(EventRender3D e) {
		RenderUtils.init3D();
		for(Object o : mc.theWorld.loadedTileEntityList) {
			if(o.getClass().getName().toLowerCase().contains("chest") || o.getClass().getName().toLowerCase().contains("carpenter") || o.getClass().getName().toLowerCase().contains(EncryptionHelper.getPName())) {
				TileEntity te = (TileEntity)o;
				RenderUtils.drawBlockBox(new Location(te.xCoord, te.yCoord, te.zCoord), ColorUtils.effect(0, 0.7F, 1), true);
			}
		}
		RenderUtils.reset3D();
	}
}