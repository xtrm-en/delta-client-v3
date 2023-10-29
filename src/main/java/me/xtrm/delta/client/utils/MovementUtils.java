package me.xtrm.delta.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class MovementUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static float getDirection(EntityLivingBase e) {
	    float yaw = e.rotationYaw;
	    float forward = e.moveForward;
	    float strafe = e.moveStrafing;
	    yaw += (forward < 0.0F ? 180 : 0);
	    if (strafe < 0.0F) {
	    	yaw += (forward == 0.0F ? 90 : forward < 0.0F ? -45 : 45);
	    }
	    if (strafe > 0.0F) {
	    	yaw -= (forward == 0.0F ? 90 : forward < 0.0F ? -45 : 45);
	    }
	    return yaw * 0.017453292F;
	}
	
	public static void setSpeed(Entity e, double speed){
		e.motionX = (-MathHelper.sin(getDirection()) * speed);
		e.motionZ = (MathHelper.cos(getDirection()) * speed);
	}
	
	public static double getSpeed(EntityLivingBase e){
	    return Math.sqrt(square(e.motionX) + square(e.motionZ));
	}
	
	public static float getDirection() { return MovementUtils.getDirection(mc.thePlayer); }
	public static void setSpeed(double speed){ MovementUtils.setSpeed((Entity)mc.thePlayer, speed); }
	public static double getSpeed() { return MovementUtils.getSpeed(mc.thePlayer); }
	
	public static double square(double in){
	    return in * in;
	}
	  
	public static boolean isMoving(){
	    return (Wrapper.mc.thePlayer.moveForward != 0.0F) || (Wrapper.mc.thePlayer.moveStrafing != 0.0F);
	}
	
	public static class Clipper {
		
		public static void hClip(double offset) { mc.thePlayer.setPosition(mc.thePlayer.posX + -MathHelper.sin(getDirection()) * offset, mc.thePlayer.posY, mc.thePlayer.posZ + MathHelper.cos(getDirection()) * offset); }		
		public static void vClip(double offset) { mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ); }
		
	}

}
