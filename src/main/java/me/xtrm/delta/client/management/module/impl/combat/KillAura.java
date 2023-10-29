package me.xtrm.delta.client.management.module.impl.combat;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.CombatUtils;
import me.xtrm.delta.client.utils.Modes;
import me.xtrm.delta.client.utils.RotationUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.EnumChatFormatting;

public class KillAura extends Module {

	public KillAura() {
		super("KillAura", Keyboard.KEY_R, Category.COMBAT);
		setDescription("Attaque les joueurs autour de soi");
		
		registerSettings(
				new Setting("CPS", this, 13D, 0D, 20D, true),
				new Setting("Reach", this, 4.5D, 3D, 7D, false),
				new Setting("Priority", this, "Range", Modes.build("Range", "Health")),
				new Setting("BlockHit", this, true),
				new Setting("BlockMode", this, "Vanilla", Modes.build("Vanilla", "NCP")),
				new Setting("NoSwing", this, false)
		);
	}

	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() == EventType.POST) return;
			
		setDisplayName(getName() + " " + EnumChatFormatting.GRAY + "Switch");
		switchMode(e);
	}
	
	private TimeHelper timer = new TimeHelper();
	private List<EntityLivingBase> switchTargets = new ArrayList<>();
	
	public void switchMode(EventMotion event) {
		double reach = getSetting("Reach").getSliderValue();
		int cps = (int)getSetting("CPS").getSliderValue();

		if(switchTargets.isEmpty()) {
			Iterator<Object> iter = mc.theWorld.loadedEntityList.iterator();
			while(iter.hasNext()) {
				Object o = iter.next();

				if(o instanceof EntityLivingBase) {
					EntityLivingBase e = (EntityLivingBase)o;

					if(!CombatUtils.isValid(e)) continue;
					if(!CombatUtils.isInReach(e, reach)) continue;

					switchTargets.add(e);
				}
			}
			
			String mode = getSetting("Priority").getComboValue();

			switch(mode) {
			case "Range":
				Collections.sort(switchTargets, new Comparator<EntityLivingBase>() { // reach compare
					@Override
					public int compare(EntityLivingBase e1, EntityLivingBase e2) {
						if(mc.thePlayer.getDistanceToEntity(e1) < mc.thePlayer.getDistanceToEntity(e2)) {
							return -1;
						}
						if(mc.thePlayer.getDistanceToEntity(e1) > mc.thePlayer.getDistanceToEntity(e2)) {
							return 1;
						}
						return 0;
					}
				});
				break;
			case "Health":
				Collections.sort(switchTargets, new Comparator<EntityLivingBase>() { // health compare
					@Override
					public int compare(EntityLivingBase e1, EntityLivingBase e2) {
						if(e1.getHealth() < e2.getHealth()) {
							return -1;
						}
						if(e1.getHealth() > e2.getHealth()) {
							return 1;
						}
						return 0;
					}
				});
				break;
			}
		}

		if(switchTargets.isEmpty()) return;

		EntityLivingBase target = switchTargets.get(0);
		if(!CombatUtils.isValid(target)) {
			switchTargets.remove(target);
			return;
		}
		if(!CombatUtils.isInReach(target, reach)) {
			switchTargets.remove(target);
			return;
		}

		float[] rots = RotationUtils.getRotations(target);
		float yaw = rots[0];
		float pitch = rots[1];
		event.setYaw(yaw);
		event.setPitch(pitch);

		if ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && getSetting("BlockHit").getCheckValue()) || mc.thePlayer.isBlocking()) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
        }

		if(!timer.hasReached(1000 / cps)) return;
		timer.reset();

		if(getSetting("NoSwing").getCheckValue())
			mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation(mc.thePlayer, 1));
		else
			mc.thePlayer.swingItem();

		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));

		switchTargets.remove(target);
	}
	
	public void vanilla(EventMotion e) {
		for(Object o : mc.theWorld.loadedEntityList) {
			if(o instanceof EntityLivingBase) {
				if(((EntityLivingBase)o) != null && ((EntityLivingBase)o) != mc.thePlayer && ((EntityLivingBase)o).isEntityAlive()) {
					if(o instanceof EntityPlayer)
						if(DeltaAPI.getClient().getFriendManager().isFriend(((EntityPlayer)o).getCommandSenderName()))
							continue;
					if(((EntityLivingBase)o).getDistanceToEntity(mc.thePlayer) <= getSetting("Reach").getSliderValue()) {
						if(!getSetting("NoSwing").getCheckValue())
							mc.thePlayer.swingItem();
						
						EntityLivingBase singleTarget = ((EntityLivingBase)o);
						
						float[] rotations = RotationUtils.getRotations(singleTarget);
						
						e.setYaw(rotations[0]);
						e.setPitch(rotations[1]);						
						
						if ((mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && getSetting("BlockHit").getCheckValue()) || mc.thePlayer.isBlocking()) {
				            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
				        }
						
						if(!timer.hasReached((long) (1000 / getSetting("CPS").getSliderValue())))
							continue;
						
						mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(((EntityLivingBase)o), Action.ATTACK));
					}
				}
			}
		}
		if(!timer.hasReached((long) (1000 / getSetting("CPS").getSliderValue())))
			return;
		timer.reset();
	}
	
}
