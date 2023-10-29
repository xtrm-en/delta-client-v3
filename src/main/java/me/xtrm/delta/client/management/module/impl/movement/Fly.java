package me.xtrm.delta.client.management.module.impl.movement;

import org.lwjgl.input.Keyboard;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import static me.xtrm.delta.client.utils.Wrapper.*;

public class Fly extends Module {
	
	public Fly() {
		super("Fly", Keyboard.KEY_F, Category.MOVEMENT);
		setDescription("I believe I can fly!");
		
		registerSettings(new Setting("Speed", this, 1D, 0, 5, false));
	}
	
	private TimeHelper timer = new TimeHelper();
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.POST) return;
		
		mc.thePlayer.motionY = mc.gameSettings.keyBindJump.getIsKeyPressed() ? (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 0 : getSetting("Speed").getSliderValue()) : (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -getSetting("Speed").getSliderValue() : 0);
		
		MovementUtils.setSpeed(0);
		if(MovementUtils.isMoving()) {
			MovementUtils.setSpeed(MovementUtils.getSpeed() + getSetting("Speed").getSliderValue());
		}
	}
	
	private double calculateGround() {
	    AxisAlignedBB playerBoundingBox = mc.thePlayer.boundingBox;
	    double h = 0.25;
	    
	    for (double flyHeight = 0; flyHeight < mc.thePlayer.posY; flyHeight += h) {
	    	AxisAlignedBB nextBox = playerBoundingBox.copy().offset(0, -flyHeight, 0);
	    	if (mc.theWorld.checkBlockCollision(nextBox)) {
	    		return mc.thePlayer.posY - flyHeight;
	    	}
	    }
	    return 0;
	    
	}
	
	private void goToGround() {
		double stance = mc.thePlayer.posY - mc.thePlayer.boundingBox.minY;		
		if(stance > 1.65 || stance < 0.1) return;
		
	    double ground = calculateGround();

	    if (ground == 0.0)
	        return;

	    for (double posY = mc.thePlayer.posY; posY > ground; posY -= 8.0) {
	        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY - stance, posY, mc.thePlayer.posZ, true));

	        if (posY - 8.0 < ground) break; // Prevent next step
	    }

	    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, ground - stance, ground, mc.thePlayer.posZ, true));
	    
	    for (double posY = ground; posY < mc.thePlayer.posY; posY += 8.0) {
	    	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, posY - stance, posY, mc.thePlayer.posZ, true));

	        if (posY + 8.0 > mc.thePlayer.posY) break; // Prevent next step
	    }

	    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	}
}
