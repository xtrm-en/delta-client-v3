package me.xtrm.delta.client.management.command.impl;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.api.friend.IFriendManager;
import net.minecraft.util.EnumChatFormatting;

public class Friend implements ICommand {

	@Override
	public String getName() {
		return "friend";
	}

	@Override
	public boolean execute(ICommandListener cp, String[] args) {
		if(args.length == 1) {
			IFriendManager fm = DeltaAPI.getClient().getFriendManager();
			if(fm.isFriend(args[0])) {
				fm.removeFriend(args[0]);
				printMessage(cp, "Le joueur " + EnumChatFormatting.DARK_PURPLE + args[0] + EnumChatFormatting.GRAY + " a été " + EnumChatFormatting.RED + "enlevé" + EnumChatFormatting.GRAY + " de votre liste d'amis");
			}else {
				fm.addFriend(args[0]);
				printMessage(cp, "Le joueur " + EnumChatFormatting.DARK_PURPLE + args[0] + EnumChatFormatting.GRAY + " a été " + EnumChatFormatting.GREEN + "ajouté" + EnumChatFormatting.GRAY + " dans votre liste d'amis");
			}
		}else {
			printMessage(cp, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + getHelp());
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Ajoute ou enlève un joueur de votre liste d'amis";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"f", "friends", "ami", "amis"};
	}

	@Override
	public String getHelp() {
		return "friend <Joueur>";
	}

}
