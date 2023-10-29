package me.xtrm.delta.client.management.module.impl.combat;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.CombatUtils;
import me.xtrm.delta.client.utils.Modes;
import me.xtrm.delta.client.utils.MovementUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class TPAura extends Module {

	public TPAura() {
		super("TPAura", Category.COMBAT);		
		registerSettings(
				new Setting("Mode", this, "10Blocks", Modes.build("10Blocks", "Follow")),
				new Setting("CPS", this, 10, 0, 20, true)
		);
		
		timer = new TimeHelper();
		setDescription("Mode 10Blocks: Peut taper de 10 blocks (peut rollback)");
	}
	
	public void updateDesc() {
		switch(getMode()) {
			case "10Blocks":
				setDescription("Mode 10Blocks: Peut taper de 10 blocks (peut rollback)");
				break;
			case "Follow":
				setDescription("Mode Follow: Se téléporte derrière un joueur et le tape");
				break;
		}
	}
	
	private TimeHelper timer;
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		
		switch(getMode()) {
			case "10Blocks":				
				if(getSetting("CPS").getSliderValue() == 0) return;
				
				if(!timer.hasReached(1000 / (long)getSetting("CPS").getSliderValue()))
					return;
				timer.reset();
				
				for(Object o : mc.theWorld.loadedEntityList) {
					if(o instanceof EntityLivingBase) {
						if(o != null && ((EntityLivingBase)o) != mc.thePlayer && ((EntityLivingBase)o).isEntityAlive() && ((EntityLivingBase)o).getDistanceToEntity(mc.thePlayer) <= 10) {
							if(o instanceof EntityPlayer)
								if(DeltaAPI.getClient().getFriendManager().isFriend(((EntityPlayer)o).getCommandSenderName()))
									continue;
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(((EntityLivingBase)o).posX, ((EntityLivingBase)o).posY, ((EntityLivingBase)o).posY + (mc.thePlayer.posY - mc.thePlayer.boundingBox.minY), ((EntityLivingBase)o).posZ, false));
							CombatUtils.attack(((EntityLivingBase)o), false, true, 0);
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, mc.thePlayer.posY, mc.thePlayer.posZ, true));
						}
					}
				}
				break;
			case "Follow":
				EntityLivingBase target = CombatUtils.findEntity();
				
				if(!CombatUtils.isValid(target)) return;
				if(!CombatUtils.isInReach(target, 8)) return;
				
				toggle();
				
				if(target instanceof EntityPlayer)
					if(DeltaAPI.getClient().getFriendManager().isFriend(((EntityPlayer)target).getCommandSenderName()))
						break;
				
				double calcX, calcZ;
				
				calcX = target.posX;
				calcZ = target.posZ;
				
				double offset = 1;
				
				float dir = MovementUtils.getDirection(target);
				
				calcX -= -MathHelper.sin(dir) * offset;
				calcZ -= MathHelper.cos(dir) * offset;
				
				mc.thePlayer.setPosition(calcX, target.posY + 2, calcZ);
				mc.thePlayer.motionY = 0;
				
				if(getSetting("CPS").getSliderValue() == 0) return;
				
				if(!timer.hasReached(1000 / (long)getSetting("CPS").getSliderValue()))
					return;
				timer.reset();
				
				CombatUtils.attack(target, false, true, 0);
				
				break;
		}
	}

}
