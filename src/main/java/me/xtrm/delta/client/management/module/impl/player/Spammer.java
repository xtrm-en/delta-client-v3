package me.xtrm.delta.client.management.module.impl.player;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.util.Random;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.move.EventMotion;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.api.setting.Setting;
import me.xtrm.delta.client.utils.PlayerUtils;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.loader.api.event.data.EventType;
import net.minecraft.util.EnumChatFormatting;

public class Spammer extends Module {

	private static boolean lock = false;
	
	public Spammer() {
		super("Spammer", Category.PLAYER);
		setDescription("Spam un message dans le chat");
		
		registerSettings(
				new Setting("Delay", this, 3000, 0, 10000, true),
				new Setting("AntiSpam", this, true)
		);
	}
	
	private static TimeHelper timer = new TimeHelper();
	private static Random r = new Random();
	
	public static String str = "off";
	
	@Handler
	public void onUpdate(EventMotion e) {
		if(e.getType() != EventType.PRE) return;
		
		String discord = WebUtils.getDiscordUrl().replace("https://", "").replace("http://", "").replace(".", "(.)");
		
		if(!timer.hasReached((long)getSetting("Delay").getSliderValue()))
			return;
		timer.reset();
		
		String msgx = "Delta Client par xTrM_ | Puissant & Gratuit | Lien: " + discord;
		
		if(!str.equalsIgnoreCase("off"))
			msgx = str;
		
		if(getSetting("AntiSpam").getCheckValue())
			msgx = msgx + " " + Math.min(99999, r.nextInt(100000) + 10000);
		
		mc.thePlayer.sendChatMessage(msgx);
	}
	
	@Override
	public void onEnable() {
		if(!lock) {
			lock = true;
			toggle();
			PlayerUtils.sendMessage(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "ATTENTION! " + EnumChatFormatting.GRAY + "Vous êtes sur le point d'activer le " + EnumChatFormatting.RED + "Spammer" + EnumChatFormatting.GRAY + "! Faites attention si vous l'utilisez.");
		}
	}
}