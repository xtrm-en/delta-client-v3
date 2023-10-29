package me.xtrm.delta.factory;

import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.event.events.network.EventPacket;
import me.xtrm.delta.client.api.event.events.other.EventIsNormalBlock;
import me.xtrm.delta.loader.api.event.data.EventType;
import me.xtrm.delta.wrapper.Wrap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;

public class DeltaEventFactory {	
	
	private static double cachedX, cachedY, cachedZ;
	private static float cachedYaw, cachedPitch;
	private static boolean cachedOnGround;
	private static boolean cachedSilent;
	
	@Wrap(id = "factoryMotionUpdate")
	public static boolean factoryMotionUpdate(Object o, boolean pre) {
		EntityPlayerSP ep = (EntityPlayerSP)o;
		if(pre) {			
			EventMotion e = new EventMotion(EventType.PRE, cachedX = ep.posX, cachedY = ep.posY, cachedZ = ep.posZ, cachedYaw = ep.rotationYaw, cachedPitch = ep.rotationPitch, cachedOnGround = ep.onGround);
			e.call();
			ep.posX = e.getX();
			ep.posY = e.getY();
			ep.posZ = e.getZ();
			ep.rotationYaw = e.getYaw();
			ep.rotationPitch = e.getPitch();
			ep.onGround = e.isOnGround();
			cachedSilent = e.isSilent();
			return e.isCancelled();
		}else {
			EventMotion e = new EventMotion(EventType.POST, ep.posX, ep.posY, ep.posZ, ep.rotationYaw, ep.rotationPitch, ep.onGround);
			if(cachedSilent) {
				ep.rotationYawHead = ep.rotationYaw;
				ep.renderYawOffset = ep.rotationYaw;
				
				ep.posX = cachedX;
				ep.posY = cachedY;
				ep.posZ = cachedZ;
				ep.rotationYaw = cachedYaw;
				ep.rotationPitch = cachedPitch;
			}
			ep.onGround = cachedOnGround;
			e.call();
			return e.isCancelled();
		}
	}
	
	// TODO: Direct ASM call
	@Wrap(id = "factoryShouldBeOpaque")
	public static boolean factoryShouldBeOpaque() {
		EventIsNormalBlock e = new EventIsNormalBlock();
		e.call();
		return !e.isCancelled();
	}
	
	// TODO: Direct ASM call
	@Wrap(id = "factoryPacket")
	public static boolean factoryPacket(Object packet, boolean in) {
		EventPacket e = new EventPacket((Packet)packet, in ? EventType.RECIEVE : EventType.SEND);
		e.call();
		return e.isCancelled();
	}
	
}
