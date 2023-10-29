package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.other.EventIsNormalBlock;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.Location;
import me.xtrm.delta.client.utils.MovementUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public class NoClip extends Module {
	
	public NoClip() {
		super("NoClip", Category.MISC);
		setDescription("Traverse le sol si le joueur est dans un block (trapdoor, sable, etc...)");
	}
	
	@Handler
	public void onUpdate(EventUpdate e) {
		if(DeltaAPI.getClient().getModuleManager().getModule("Freecam").isEnabled()) return;
		
		Location loc = new Location(Math.floor(mc.thePlayer.posX), Math.floor(mc.thePlayer.posY), Math.floor(mc.thePlayer.posZ)).offset(0, -1, 0);
		Block block = mc.theWorld.getBlock((int)loc.getX(), (int)loc.getY(), (int)loc.getZ());
		mc.thePlayer.noClip = false;
		
		if(block == Blocks.air || block.getMaterial().isReplaceable() || block == Blocks.tallgrass)
			return;
		
		mc.thePlayer.noClip = true;		
		mc.thePlayer.motionY = mc.gameSettings.keyBindJump.getIsKeyPressed() ? (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? 0 : 0.4) : (mc.gameSettings.keyBindSneak.getIsKeyPressed() ? -0.4 : 0);
		mc.thePlayer.onGround = true;
		
		double offset = MovementUtils.isMoving() ? 0.2 : 0;
		double yoffset = 0;
		
		if(mc.thePlayer.isCollidedHorizontally || yoffset != 0)
			mc.thePlayer.setPosition(
					mc.thePlayer.posX + -MathHelper.sin(MovementUtils.getDirection()) * offset, 
					mc.thePlayer.posY + yoffset, 
					mc.thePlayer.posZ + MathHelper.cos(MovementUtils.getDirection()) * offset
			);
	}
	
	@Handler
	public void blockNormalization(EventIsNormalBlock e) {
		e.setCancelled(true);
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.noClip = false;
		super.onDisable();
	}
	
}
