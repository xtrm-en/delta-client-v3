package me.xtrm.delta.client.management.command.impl;

import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.utils.MovementUtils.Clipper;
import net.minecraft.util.EnumChatFormatting;

public class VClip implements ICommand {

	@Override
	public String getName() {
		return "vclip";
	}

	@Override
	public boolean execute(ICommandListener cp, String[] args) {
		if(args.length == 1) {
			try {
				Integer i = Integer.parseInt(args[0]);
				if(i == 0) {
					printMessage(cp, EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre non nul.");
					return true;
				}
				Clipper.vClip(i);
				printMessage(cp, "Vous avez été téléporté " + EnumChatFormatting.DARK_PURPLE + i + EnumChatFormatting.GRAY + " blocks vers le " + EnumChatFormatting.DARK_PURPLE + (i > 0 ? "haut" : "bas"));
			} catch(Exception e) {
				printMessage(cp, EnumChatFormatting.RED + "Erreur, veuillez entrer un nombre valide.");
			}
		}else {
			printMessage(cp, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + getHelp());
		}
		
		return true;
	}

	@Override
	public String getDescription() {
		return "Vous téléporte verticalement";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"verticalclip", "vc"};
	}

	@Override
	public String getHelp() {
		return "vclip <Nombre>";
	}

}
