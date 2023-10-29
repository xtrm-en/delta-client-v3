package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.render.EventRender3D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.vector.mc.MCVec3;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Module {

	public Tracers() {
		super("Tracers", Category.RENDER);
		setDescription("Trace des traits vers les autres joueurs");
		
		registerSettings(
				new Setting("Players", this, true),
				new Setting("Monsters", this, true),
				new Setting("Animals", this, true),
				new Setting("Invisibles", this, true)
		);
	}
	
	@Handler
	public void onRender3D(EventRender3D e) {
		mc.gameSettings.viewBobbing = false;
		for(Object o : mc.theWorld.loadedEntityList) {
			if(o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)o;
				if(isValid(entity)) {
					int dist = (int) (mc.thePlayer.getDistanceToEntity(entity) * 2);
					if (dist > 255) dist = 255;
					
					Color color = new Color(255 - dist, dist, 0, 150);
					
					drawTracer(entity, color, e.getPartialTicks());
				}
			}
		}
	}
	
	public boolean isValid(EntityLivingBase e) {
		if(e instanceof EntityClientPlayerMP) return false;
		
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
	
	private void drawTracer(Entity entity, Color color, float partialTicks) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ;

        MCVec3 eyeVector = new MCVec3(0.0, 0.0, 1.0)
                .rotatePitch((float) (-Math.toRadians(mc.thePlayer.rotationPitch)))
                .rotateYaw((float) (-Math.toRadians(mc.thePlayer.rotationYaw)));

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4d(color.getRed() / 255D, color.getGreen() / 255D, color.getBlue() / 255D, color.getAlpha() / 255D);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(eyeVector.xCoord, eyeVector.yCoord, eyeVector.zCoord);
        GL11.glVertex3d(x, y + entity.getEyeHeight(), z);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }
	
	private boolean old;
	
	@Override
	public void onEnable() {
		old = mc.gameSettings.viewBobbing;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.viewBobbing = old;
		super.onDisable();
	}

}
