package me.xtrm.delta.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

public class CombatUtils {

public static Minecraft mc = Minecraft.getMinecraft();
    
    public static EntityLivingBase findEntity() {
        double Dist = Double.MAX_VALUE;
        EntityLivingBase entity = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            if ((object instanceof EntityLivingBase)) {
                EntityLivingBase e = (EntityLivingBase) object;
                if (CombatUtils.isValid(e) && (mc.thePlayer.getDistanceToEntity(e) < Dist)) {
                    entity = e;
                    Dist = mc.thePlayer.getDistanceToEntity(entity);
                }
            }
        }
        return entity;
    }
    
    public static float getPitch(EntityLivingBase ent) {
    	double y = (ent.posY + ent.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        y /= mc.thePlayer.getDistanceToEntity(ent);
        double pitch = Math.asin(y) * 57;
        pitch = -pitch;
        return (float)pitch;
    }
 
    public static float getYaw(EntityLivingBase ent) {
        double x = ent.posX - mc.thePlayer.posX;
        double z = ent.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57;
        yaw = -yaw;
        return (float)yaw;
    }
    
    public static boolean isValid(EntityLivingBase ent) {
    	if(ent == null)
    		return false;
    	
    	if(ent == mc.thePlayer)
    		return false;
    	
    	if(!ent.isEntityAlive())
    		return false;

    	return true;
    }
    
    public static void attack(EntityLivingBase entity, boolean noSwing, boolean keepSprint, int crackSize) {
		if(noSwing)
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		else
			mc.thePlayer.swingItem();
		
        if(keepSprint)
        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        else {
        	mc.thePlayer.setSprinting(false);
        	mc.playerController.attackEntity(mc.thePlayer, entity);
        }
        
        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        if (sharpLevel > 0.0f && crackSize == 0) 
            mc.thePlayer.onEnchantmentCritical(entity);
        
        for(int i = 0; i < crackSize; i++) {
    		mc.thePlayer.onCriticalHit(entity);
    		mc.thePlayer.onEnchantmentCritical(entity);
    	}
    }
    
    public static boolean isInReach(EntityLivingBase entity, double reach) {
    	return mc.thePlayer.getDistanceToEntity(entity) <= reach;
    }
	
}
