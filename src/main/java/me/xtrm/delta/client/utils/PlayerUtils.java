package me.xtrm.delta.client.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

@SuppressWarnings("all")
public class PlayerUtils { 
	
	public static void sendMessage(String msg) {
		if(Wrapper.mc.thePlayer != null)
			Wrapper.mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "D " + EnumChatFormatting.GRAY + "> " + msg));
	}
	
	public static void makePlayerSendMessage(String msg) {
		if(Wrapper.mc.thePlayer != null)
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(msg));
	}
	
	public static void updateToBestTool(Location loc) {
		if(Wrapper.mc.thePlayer == null) return;
		Block block = loc.getBlock();
        float strength = 1.0f;
        int bestItemIndex = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = Wrapper.mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null || itemStack.func_150997_a(block) <= strength) continue;
            strength = itemStack.func_150997_a(block);
            bestItemIndex = i;
        }
        if (bestItemIndex != -1) {
            Wrapper.mc.thePlayer.inventory.currentItem = bestItemIndex;
        }
	}
	
	// TODO: Replace
	public static void dig(Location loc) {
		if(Wrapper.mc.thePlayer == null) return;
		
		double dist = loc.getDistanceToEntity(Wrapper.mc.thePlayer);
		if(dist > 5)
			return;
		
		Location locx = new Location(Math.floor(Wrapper.mc.thePlayer.posX), Math.floor(Wrapper.mc.thePlayer.posY), Math.floor(Wrapper.mc.thePlayer.posZ));
		boolean doTheThing = locx.getBlock() == Blocks.lava || locx.getBlock() == Blocks.flowing_lava;
		
		if(!doTheThing) {
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(0, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
			if(!Wrapper.mc.thePlayer.capabilities.isCreativeMode) {
				Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(2, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
			}
		}else {
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(0, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
			Wrapper.mc.thePlayer.swingItem();
			Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(2, (int)loc.getX(), (int)loc.getY(), (int)loc.getZ(), 1));
		}
	}

}
