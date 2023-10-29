package me.xtrm.delta.client.management.hook;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.player.EventClick;
import me.xtrm.delta.client.api.event.events.player.EventClick.ClickType;
import me.xtrm.delta.client.api.event.events.player.EventClick.MouseButton;
import me.xtrm.delta.client.api.event.events.player.EventJump;
import me.xtrm.delta.client.api.event.events.player.EventKeyboard;
import me.xtrm.delta.client.api.event.events.render.EventRender2D;
import me.xtrm.delta.client.api.event.events.render.EventRender3D;
import me.xtrm.delta.client.api.event.events.update.EventTick;
import me.xtrm.delta.client.api.event.events.update.EventUpdate;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.gui.click.old.ClickGUI;
import me.xtrm.delta.client.gui.overrides.mainmenu.newest.GuiLoadingDelta;
import me.xtrm.delta.client.gui.overrides.mainmenu.newest.NewMainMenu;
import me.xtrm.delta.client.gui.overrides.mainmenu.newest.TempLoadingGui;
import me.xtrm.delta.client.management.rpc.RPCManager;
import me.xtrm.delta.client.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Holy grail of 1.7.10 Forge Hacks 
 * Transmitting Forge Events to our EventBus
 * 
 * TODO: Move to asm
 */
public class FMLEventHook {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpen(GuiOpenEvent event) {
		if(event.gui != null && !(event.gui instanceof GuiChat) && !(event.gui instanceof GuiIngameMenu) && !(event.gui instanceof GuiChest) && !(event.gui instanceof GuiInventory) && !(event.gui instanceof GuiContainerCreative) && !(event.gui instanceof GuiCommandBlock) && !(event.gui instanceof ClickGUI) && !(event.gui instanceof GuiEnchantment) && !(event.gui instanceof GuiRepair) && !(event.gui instanceof GuiInventory) && !(event.gui instanceof GuiCrafting) && !(event.gui instanceof GuiFurnace) && !(event.gui instanceof GuiBeacon) && !(event.gui instanceof GuiDispenser) && !(event.gui instanceof GuiHopper) && !(event.gui instanceof GuiBrewingStand) && !(event.gui instanceof GuiEditSign) && !(event.gui instanceof ForgeConfigGui)) {
			RPCManager.INSTANCE.updateRPC("In menus", "Clicking buttons");
		}
		
		if((event != null && event.gui != null && event.gui.getClass() != null && event.gui.getClass().getName() != null)) {
			if(!event.gui.getClass().getName().toLowerCase().contains("delta")) {
				if(event.gui.getClass().getName().toLowerCase().contains("main") && event.gui.getClass().getName().toLowerCase().contains("menu")) {
					event.gui = new GuiLoadingDelta(new TempLoadingGui(new NewMainMenu()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onMouse(MouseEvent event) {		
		if(event.button != -1) {
			EventClick e = new EventClick(event.button == 0 ? MouseButton.LEFT : (event.button == 2 ? MouseButton.MIDDLE : MouseButton.RIGHT), event.buttonstate ? ClickType.PRESS : ClickType.RELEASE);
			e.call();
		}
	}
	
	@SubscribeEvent
	public void onKey(KeyInputEvent event) {
		if(!Keyboard.getEventKeyState())
			return;
		EventKeyboard e = new EventKeyboard(Keyboard.getEventKey());
		e.call();
	}
	
	@SubscribeEvent
	public void onJump(LivingEvent.LivingJumpEvent event) {
		if(event.entity instanceof EntityPlayerSP) {
			EventJump e = new EventJump();
			e.call();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRender2D(RenderGameOverlayEvent event) {
		if(event.type == RenderGameOverlayEvent.ElementType.ALL) {
			EventRender2D e = new EventRender2D(event.partialTicks);
			e.call();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRender3D(RenderWorldLastEvent event) {
		EventRender3D e = new EventRender3D(event.partialTicks);
		e.call();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onNametag(RenderLivingEvent.Specials.Pre event) {
		IModule mod = DeltaAPI.getClient().getModuleManager().getModule("Nametags");
		if(mod != null && mod.isEnabled()) {
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onUpdate(LivingEvent.LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayerSP) {
			EventUpdate e = new EventUpdate();
			e.call();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTick(PlayerTickEvent event) {
		if(event.player != Wrapper.mc.thePlayer) return;
		
		EventTick e = new EventTick();
		e.call();
	}
}