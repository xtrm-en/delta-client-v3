package me.xtrm.delta.client.management.command.impl;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.api.module.IModule;
import net.minecraft.util.EnumChatFormatting;

public class Toggle implements ICommand {

	@Override
	public String getName() {
		return "toggle";
	}

	@Override
	public boolean execute(ICommandListener panel, String[] args) {
		if(args.length == 1) {
			String modName = args[0];
			IModule m = DeltaAPI.getClient().getModuleManager().getModule(modName);
			if(m != null) {
				m.toggle();
				printMessage(panel, EnumChatFormatting.GRAY + "Module " + EnumChatFormatting.DARK_PURPLE + modName + (m.isEnabled() ? (EnumChatFormatting.GREEN + " activé") : EnumChatFormatting.RED + " desactivé"));
			}else {
				printMessage(panel, EnumChatFormatting.RED + "Erreur, module \"" + modName + "\" inconnu.");
			}
		}else {
			printMessage(panel, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + getHelp());
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Active ou desactive un module";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"t", "tgl", "togle"};
	}

	@Override
	public String getHelp() {
		return "toggle <Module>";
	}

}
