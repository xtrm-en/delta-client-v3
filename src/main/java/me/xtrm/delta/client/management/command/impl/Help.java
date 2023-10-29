package me.xtrm.delta.client.management.command.impl;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import net.minecraft.util.EnumChatFormatting;

public class Help implements ICommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public boolean execute(ICommandListener cp, String[] args) {
		if(args.length == 1) {
			String cmdName = args[0];
			ICommand cmd = null;
			for(ICommand ccc : DeltaAPI.getClient().getCommandManager().getCommands()) {
				if(ccc.getName().equalsIgnoreCase(cmdName)) {
					cmd = ccc;
				}
				for(String alia : ccc.getAliases()) {
					if(alia.equalsIgnoreCase(cmdName))
						cmd = ccc;
				}
			}
			if(cmd == null) {
				printMessage(cp, EnumChatFormatting.RED + "Erreur, commande \"" + cmdName + "\" inconnue.");
				return true;
			}
			printMessage(cp, "Commande: " + EnumChatFormatting.DARK_PURPLE + cmd.getName());
			printMessage(cp, "Description: " + EnumChatFormatting.GRAY + cmd.getDescription());
			printMessage(cp, "Usage: " + EnumChatFormatting.GRAY + cmd.getHelp());
			
			StringBuilder sb = new StringBuilder();
			for(String s : cmd.getAliases()) {
				sb.append(EnumChatFormatting.DARK_PURPLE + s);
				sb.append(EnumChatFormatting.GRAY + ", ");
			}
			String list = sb.toString();
			list = list.substring(0, list.length() - 2);
			
			printMessage(cp, "Aliases: " + list);
		}else if(args.length == 0) {
			for(ICommand cmd : DeltaAPI.getClient().getCommandManager().getCommands()) {
				printMessage(cp, EnumChatFormatting.DARK_PURPLE + cmd.getName() + EnumChatFormatting.GRAY + " - " + cmd.getDescription());
			}
		}else {
			printMessage(cp, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + getHelp());
		}
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Donne des informations sur les commandes";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"h", "hlp", "aide"};
	}

	@Override
	public String getHelp() {
		return "\"help\" ou \"help <Commande>\"";
	}

}