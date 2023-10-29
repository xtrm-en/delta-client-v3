package me.xtrm.delta.client.management.module.impl.combat;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.CombatUtils;
import me.xtrm.delta.client.utils.RotationUtils;
import me.xtrm.delta.client.utils.angle.Angle;
import me.xtrm.delta.client.utils.angle.AngleUtility;
import me.xtrm.delta.client.utils.vector.impl.Vector3;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import static me.xtrm.delta.client.utils.Wrapper.*;

public class Aimbot extends Module {

	public Aimbot() {
		super("Aimbot", Category.COMBAT);
		setDescription("Aide à viser les enemis");
		
		registerSettings(
				new Setting("Smooth Aim", this, false), 
				new Setting("X Smoothing", this, 1, 1, 10, true), 
				new Setting("Y Smoothing", this, 1, 1, 10, true)
		);
	}
	
	private AngleUtility angleUtility = new AngleUtility(70, 125, 20, 65);
	
	@Handler
	public void onEventMotion(EventMotion event) {
		if(event.getType() == EventType.POST) return;
		
		EntityLivingBase target = CombatUtils.findEntity();
		
		if(!CombatUtils.isValid(target)) return;
		if(!CombatUtils.isInReach(target, 6.0)) return;		
		
		if(getSetting("Smooth Aim").getCheckValue()) {			
			Vector3<Double> enemyCoords = new Vector3<>(target.boundingBox.minX + (target.boundingBox.maxX - target.boundingBox.minX) / 2, target.boundingBox.minY + target.height * .85, target.boundingBox.minZ + (target.boundingBox.maxZ - target.boundingBox.minZ) / 2);
	        Vector3<Double> myCoords = new Vector3<>(mc.thePlayer.boundingBox.minX + (mc.thePlayer.boundingBox.maxX - mc.thePlayer.boundingBox.minX) / 2, mc.thePlayer.posY, mc.thePlayer.boundingBox.minZ + (mc.thePlayer.boundingBox.maxZ - mc.thePlayer.boundingBox.minZ) / 2);
	        
			Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords);
			Angle srcAngle = new Angle(mc.thePlayer.prevRotationYaw, mc.thePlayer.prevRotationPitch);
			Angle smoothedAngle = angleUtility.smoothAngle(dstAngle, srcAngle, (float)getSetting("X Smoothing").getSliderValue(), (float)getSetting("Y Smoothing").getSliderValue()); 
			
			event.setYaw(mc.thePlayer.prevRotationYaw + MathHelper.wrapAngleTo180_float(smoothedAngle.getYaw() - Minecraft.getMinecraft().thePlayer.prevRotationYaw));
			event.setPitch(smoothedAngle.getPitch());
			
			event.setSilent(false);			
		} else {
			float[] rotations = RotationUtils.getRotations(target);
			
			event.setYaw(mc.thePlayer.prevRotationYaw + MathHelper.wrapAngleTo180_float(rotations[0] - Minecraft.getMinecraft().thePlayer.prevRotationYaw));
			event.setPitch(rotations[1]);
			
			event.setSilent(false);
		}
	}

}
