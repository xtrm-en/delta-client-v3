package me.xtrm.delta.client.management.command.impl;

import org.lwjgl.input.Keyboard;

import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.api.module.IModule;
import net.minecraft.util.EnumChatFormatting;

public class Bind implements ICommand {

	@Override
	public String getName() {
		return "bind";
	}

	@Override
	public boolean execute(ICommandListener cp, String[] args) {
		if(args.length == 2) {
			String modName = args[0];
			String keyName = args[1];
			IModule m = DeltaAPI.getClient().getModuleManager().getModule(modName);
			if(m != null) {
				if(keyName.equalsIgnoreCase("None")) {
					m.setKey(0);
					printMessage(cp, "Module " + EnumChatFormatting.DARK_PURPLE + m.getName() + EnumChatFormatting.GRAY + " unbind");
				}else {
					int key = Keyboard.getKeyIndex(keyName);
					m.setKey(key);
					printMessage(cp, "Module " + EnumChatFormatting.DARK_PURPLE + m.getName() + EnumChatFormatting.GRAY + " bind sur " + EnumChatFormatting.DARK_PURPLE + Keyboard.getKeyName(key));
				}
			}else {
				printMessage(cp, EnumChatFormatting.RED + "Erreur, module \"" + modName + "\" inconnu.");
			}
		}else {
			printMessage(cp, EnumChatFormatting.RED + "Erreur, utilisation incorrecte. Utilisez: " + getHelp());
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Définis la touche d'un module";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"b", "bound"};
	}

	@Override
	public String getHelp() {
		return "\"bind <Module> <Key>\" ou \"bind <Module> None\"";
	}

}
