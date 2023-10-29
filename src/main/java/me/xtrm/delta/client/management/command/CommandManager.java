package me.xtrm.delta.client.management.command;

import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.api.command.ICommand;
import me.xtrm.delta.client.api.command.ICommandListener;
import me.xtrm.delta.client.api.command.ICommandManager;
import me.xtrm.delta.client.management.command.impl.Bind;
import me.xtrm.delta.client.management.command.impl.Friend;
import me.xtrm.delta.client.management.command.impl.Help;
import me.xtrm.delta.client.management.command.impl.Toggle;
import me.xtrm.delta.client.management.command.impl.VClip;
import me.xtrm.delta.client.management.command.impl.module.SpammerCMD;
import net.minecraft.util.EnumChatFormatting;

public class CommandManager implements ICommandManager {
	
	private List<ICommand> commands;
	
	public CommandManager() {
		commands = new ArrayList<>();
	}
	
	@Override
	public void init() {
		commands.add(new Bind());
		commands.add(new Friend());
//		commands.add(new HClip());
		commands.add(new Help());
		commands.add(new Toggle());
		commands.add(new VClip());
		
		commands.add(new SpammerCMD());
		
//		commands.add(new TestTP());
	}
	
	@Override
	public void runCommand(ICommandListener panel, String msg) {
		panel.print(EnumChatFormatting.GRAY + "> " + msg);
		
		if(msg.startsWith("./"))
			msg = msg.replaceFirst("./", "");
		
		if(msg.equalsIgnoreCase("clear") || msg.equalsIgnoreCase("cls")) {
			panel.clear();
			return;
		}
		
		boolean commandResolved = false;
		String readString = msg.trim();
		boolean hasArgs = readString.trim().contains(" ");
		String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
		String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];
		for(ICommand command : commands){
			if(command.getName().trim().equalsIgnoreCase(commandName.trim())) {
				commandResolved = true;
				if(!command.execute(panel, args)) {
					panel.print(EnumChatFormatting.RED + "Une erreur s'est produite durant l'execution.");
				}
				break;
			}
			for(String alias : command.getAliases()) {
				if(alias.trim().equalsIgnoreCase(commandName.trim())) {
					commandResolved = true;
					if(!command.execute(panel, args)) {
						panel.print(EnumChatFormatting.RED + "Une erreur s'est produite durant l'execution.");
					}
					break;
				}
			}
			if(commandResolved)
				break;
		}
		if(!commandResolved)
			panel.print(EnumChatFormatting.RED + "Commande inconnue. Tapez " + EnumChatFormatting.GOLD + "\"help\" " + EnumChatFormatting.RED + "pour l'aide");
	}
	
	@Override
	public List<ICommand> getCommands(){
		return commands;
	}
}
