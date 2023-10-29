package me.xtrm.delta.client.management.module.impl.movement;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.util.ArrayDeque;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.network.EventPacket;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Blink extends Module {

	private ArrayDeque<C03PacketPlayer> packets = new ArrayDeque<>();
	private EntityOtherPlayerMP freecamEntity;
	
	public Blink() {
		super("Blink", Category.MOVEMENT);
		setDescription("Mets en attente les mouvements du joueur, permet de simuler du lag.");
	}
	
	@Handler
	public void onPacket(EventPacket e) {
		if(e.getType() != EventType.SEND) return;
		Packet p = e.getPacket();
		
		if(!(p instanceof C03PacketPlayer))
			return;
		
		C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
		C03PacketPlayer prevPacket = packets.peekLast();
		
		if(prevPacket != null 
			&& packet.func_149465_i() == prevPacket.func_149465_i()  // ground
			&& packet.func_149462_g() == prevPacket.func_149462_g()  // yaw
			&& packet.func_149470_h() == prevPacket.func_149470_h()  // pitch
			&& packet.func_149464_c() == prevPacket.func_149464_c()  // x
			&& packet.func_149471_f() == prevPacket.func_149471_f()  // y
			&& packet.func_149472_e() == prevPacket.func_149472_e()) // z
			return;
		
		packets.addLast(packet);
		e.setCancelled(true);
	}
	
	@Override
	public void onEnable() {
		if (mc.thePlayer == null) { 
            return;
        }
		
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
	        mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
		}
        mc.renderGlobal.loadRenderers();
        
        packets.forEach(mc.thePlayer.sendQueue::addToSendQueue);
        packets.clear();
        
		super.onDisable();
	}

}
