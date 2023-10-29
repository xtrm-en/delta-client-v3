package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.event.events.network.EventPacket;
import me.xtrm.delta.client.api.event.events.other.EventIsNormalBlock;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Freecam extends Module {

	public Freecam() {
		super("Freecam", Category.MOVEMENT);
		setDescription("Permet de sortir de son corps et d'explorer les environs");
		
		registerSettings(new Setting("Speed", this, 1D, 0D, 5D, false));
	}
	
	private EntityOtherPlayerMP freecamEntity;
	
	@Handler
	public void onPacket(EventPacket event) {
		if(event.getType() == EventType.RECIEVE)
			return;
		
		if(event.getPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity interact = (C02PacketUseEntity)event.getPacket();
			if(interact.func_149564_a(mc.theWorld) == freecamEntity) {
				event.setCancelled(true);
			}
		}
		
		if (event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }else if(event.getPacket() instanceof C0BPacketEntityAction && (((C0BPacketEntityAction)event.getPacket()).func_149513_d() == 4 || ((C0BPacketEntityAction)event.getPacket()).func_149513_d() == 5 || ((C0BPacketEntityAction)event.getPacket()).func_149513_d() == 1 || ((C0BPacketEntityAction)event.getPacket()).func_149513_d() == 2)) {
        	event.setCancelled(true);
        }
	}
	
	@Handler
	public void blockNormalization(EventIsNormalBlock e) {
		e.setCancelled(true);
	}
	
	@Handler
	public void onMotion(EventMotion event) {
		mc.thePlayer.noClip = true;
		
		mc.thePlayer.motionY = mc.gameSettings.keyBindJump.getIsKeyPressed() ? (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 0 : 1) : (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -1 : 0);
		
		MovementUtils.setSpeed(0);
		if(MovementUtils.isMoving()) {
			MovementUtils.setSpeed(MovementUtils.getSpeed() + 1);
		}
	}
	
	@Override
	public void onEnable() {
		if (mc.thePlayer == null) { 
            return;
        }
		
		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 2));
		mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, 5));
		
		this.freecamEntity = new EntityOtherPlayerMP(mc.theWorld, mc.getSession().func_148256_e());
        this.freecamEntity.inventory = mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        this.freecamEntity.onGround = mc.thePlayer.onGround;
        
        mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
        mc.renderGlobal.loadRenderers();
        
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(freecamEntity != null) {
			mc.thePlayer.setPosition(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ);
	        mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
		}
        mc.renderGlobal.loadRenderers();
        mc.thePlayer.noClip = false;
        mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
        
		super.onDisable();
	}

}