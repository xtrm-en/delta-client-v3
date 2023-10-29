package me.xtrm.delta.client.management.module.impl.misc;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.player.EventClick;
import me.xtrm.delta.client.api.event.events.player.EventClick.ClickType;
import me.xtrm.delta.client.api.event.events.player.EventClick.MouseButton;
import me.xtrm.delta.client.api.friend.IFriendManager;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class MCF extends Module {
	
	public MCF() {
		super("MCF", Category.MISC);
		setDescription("\"MiddleClickFriend\", ajoute en ami le joueur visé au click molette");
	}
	
	@Handler
	public void onMidClick(EventClick e) {
		if(e.getMouseButton() != MouseButton.MIDDLE || e.getClickType() != ClickType.RELEASE)
			return;
		
		if(mc.pointedEntity != null && mc.pointedEntity instanceof EntityPlayer) {
			EntityPlayer ep = (EntityPlayer)mc.pointedEntity;
			String player = ep.getCommandSenderName();
			
			IFriendManager fm = DeltaAPI.getClient().getFriendManager();
			
			if(fm.isFriend(player)) {
				PlayerUtils.sendMessage("Le joueur " + EnumChatFormatting.DARK_PURPLE + player + EnumChatFormatting.GRAY + " a été " + EnumChatFormatting.RED + "enlevé" + EnumChatFormatting.GRAY + " de votre liste d'amis");
				fm.removeFriend(player);
			}else {
				PlayerUtils.sendMessage("Le joueur " + EnumChatFormatting.DARK_PURPLE + player + EnumChatFormatting.GRAY + " a été " + EnumChatFormatting.GREEN + "ajouté" + EnumChatFormatting.GRAY + " dans votre liste d'amis");
				fm.addFriend(player);
			}
		}
	}
}
