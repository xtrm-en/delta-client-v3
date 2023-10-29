package me.xtrm.delta.client.management.command.impl.module;

import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.management.module.impl.player.Spammer;
import net.minecraft.util.EnumChatFormatting;

public class SpammerCMD implements ICommand {

	@Override
	public String getName() {
		return "spammer";
	}

	@Override
	public boolean execute(ICommandListener cp, String[] args) {
		if(args.length == 0) {
			printMessage(cp, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. \n" + EnumChatFormatting.RED + "Utilisez: " + getHelp());
		}else {
			StringBuilder sb = new StringBuilder();
			for(String s : args) {
				sb.append(s + " ");
			}
			String str = sb.toString().trim();
			
			if(str.length() > 230) {
				str = str.substring(0, 230);
			}
			
			if(str.equalsIgnoreCase("off")) {
				printMessage(cp, "CustomMessage du Spammer desactivé");
				Spammer.str = "off";
				return true;
			}
			Spammer.str = str;
			printMessage(cp, "Message du Spammer définis en: ");
			printMessage(cp, "\"" + str + "\"");
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Définit le message du Spammer";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"spam", "automsg", "autosay"};
	}

	@Override
	public String getHelp() {
		return "\"spammer <Message>\" ou \"spammer off\"";
	}

}
