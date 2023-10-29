package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.awt.Color;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.render.EventRender3D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.Modes;
import me.xtrm.delta.client.utils.render.ColorUtils;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module {

	public ESP() {
		super("ESP", Category.RENDER);
		setDescription("Dessine une boite autour de certaines entitées");
		
		registerSettings(
				new Setting("Mode", this, "Box", Modes.build("Outline", "Box", "2D")),
				new Setting("Players", this, true),
				new Setting("Monsters", this, true),
				new Setting("Animals", this, true),
				new Setting("Invisibles", this, true)
		);
	}
	
	@Handler
	public void onRender3D(EventRender3D e) {
		String mode = getMode();
		switch(mode) {
			case "Box":
				espBox(e.getPartialTicks());
				break;
			case "2D":
				esp2d(e.getPartialTicks());
				break;
		}
	}
	
	public void espBox(float partialTicks) {		
		RenderUtils.init3D();
		for(Object o : mc.theWorld.loadedEntityList) {
			if(o instanceof EntityLivingBase) {
				if(o != null && o != mc.thePlayer) {
					if(isValid((EntityLivingBase)o)) {
						boolean friend = false;
						if(o instanceof EntityPlayer) 
							if(DeltaAPI.getClient().getFriendManager().isFriend(((EntityPlayer)o).getCommandSenderName())) 
								friend = true;
						if(((EntityLivingBase)o).hurtTime > 0)
							RenderUtils.drawOutlinedEntity(partialTicks, (EntityLivingBase)o, Color.RED.getRGB());
						else
							RenderUtils.drawOutlinedEntity(partialTicks, (EntityLivingBase)o, friend ? Color.GREEN.getRGB() : ColorUtils.effect(0, 0.7F, 1).getRGB());
					}
				}
			}
		}
		RenderUtils.reset3D();
	}
	
	public void esp2d(float partialTicks) {
		for(Object o : mc.theWorld.loadedEntityList) {
			if(o instanceof EntityLivingBase) {
				if(o != null && o != mc.thePlayer) {
					if(isValid((EntityLivingBase)o)) {
						boolean friend = false;
						if(o instanceof EntityPlayer) 
							if(DeltaAPI.getClient().getFriendManager().isFriend(((EntityPlayer)o).getCommandSenderName())) 
								friend = true;
						if(((EntityLivingBase)o).hurtTime > 0)
							render2Desp((EntityLivingBase)o, Color.RED.getRGB(), partialTicks);
						else
							render2Desp((EntityLivingBase)o, friend ? Color.GREEN.getRGB() : ColorUtils.effect(0, 0.7F, 1).getRGB(), partialTicks);
					}
				}
			}
		}
	}
	
	public void render2Desp(EntityLivingBase e, int color, float partialTicks) {
		RenderUtils.setupFor2D(e, partialTicks);
		
		GuiUtils.getInstance().drawRect(31.0D, -79.0D, 26.0D, 11.0D, -16777216);
		GuiUtils.getInstance().drawRect(-31.0D, -79.0D, -26.0D, 11.0D, -16777216);
		GuiUtils.getInstance().drawRect(-30.0D, 6.0D, 30.0D, 11.0D, -16777216);
		GuiUtils.getInstance().drawRect(-30.0D, -79.0D, 30.0D, -74.0D, -16777216);
        
		GuiUtils.getInstance().drawRect(30.0D, -75.0D, 27.0D, 10.0D, color);
		GuiUtils.getInstance().drawRect(-30.0D, -75.0D, -27.0D, 10.0D, color);
		GuiUtils.getInstance().drawRect(-30.0D, 7.0D, 30.0D, 10.0D, color);
		GuiUtils.getInstance().drawRect(-30.0D, -78.0D, 30.0D, -75.0D, color);
		
		RenderUtils.disableFor2D();
	}
	
	public boolean isValid(EntityLivingBase e) {
		if(!getSetting("Players").getCheckValue())
			if(e instanceof EntityPlayer)
				return false;
		if(!getSetting("Monsters").getCheckValue())
			if(e instanceof EntityMob || e instanceof EntitySlime)
				return false;
		if(!getSetting("Animals").getCheckValue())
			if(e instanceof EntityAnimal || e instanceof EntitySquid || e instanceof EntityBat)
				return false;
		if(!getSetting("Invisibles").getCheckValue())
			if(e.isInvisible())
				return false;
		return true;
	}

}
