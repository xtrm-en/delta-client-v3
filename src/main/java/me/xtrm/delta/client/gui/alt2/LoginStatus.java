package me.xtrm.delta.client.gui.alt2;

import me.xtrm.delta.client.utils.Wrapper;
import net.minecraft.util.EnumChatFormatting;

public enum LoginStatus {
	NOTHING_SELECTED(EnumChatFormatting.GRAY + "Aucun alt sélectionné."),
	IDLE(EnumChatFormatting.GRAY + "En attente..."), 
	WAITING(EnumChatFormatting.GRAY + "En attente..."),
	LOGGINGIN(EnumChatFormatting.YELLOW + "Connexion en cours.."), 
	LOGGEDIN(EnumChatFormatting.GREEN + "Connecté sur %s"), 
	FAILED(EnumChatFormatting.RED + "Connexion Raté"), 
	ERROR(EnumChatFormatting.RED + "Erreur"),
	REMOVED(EnumChatFormatting.GREEN + "Retiré");
	
	private String text;
	
	private LoginStatus(String text) {
		this.text = text;
	}
	
	public String getText() {
		if(text.contains("%s"))
			return String.format(text, Wrapper.mc.getSession().getUsername());
		return text;
	}
}
