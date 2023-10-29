package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.render.EventRender2D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.EncryptionHelper;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.MinecraftEnvironment;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.EnumChatFormatting;
public class UnclaimFinder extends Module {

	public UnclaimFinder() {
		super("UnclaimFinder", Category.RENDER);
		setDescription("Affiche l'interface d'Unclaim Finder");
	}

	public RenderItem itemRenderer;
	private List<ItemStack> items = new ArrayList<>();
	private Map<ItemStack, Integer> values = new HashMap<>();
	
	@Handler
	public void onRender(EventRender2D event) {
		int amountTiles = 0;
		items.clear();
		values.clear();
		
		if(MinecraftEnvironment.isPaladium()) {
			int chest = 0, moddedChests = 0, drawers = 0, palaMach = 0;
			
			for(Object o : mc.theWorld.loadedTileEntityList) {
				if(!o.getClass().getName().toLowerCase().contains("secretrooms"))
					amountTiles++;
				if(o instanceof TileEntityChest || o instanceof TileEntityEnderChest) {
					chest++;
				}else if(o.getClass().getName().toLowerCase().contains("chest") || o.getClass().getName().toLowerCase().contains(EncryptionHelper.getPName())) {
					moddedChests++;
				}else if(o.getClass().getName().toLowerCase().contains("drawer")) {
					drawers++;
				}
			}
			
			ItemStack chestI, mcI, drI, palaMachI;
			
			values.put(chestI = new ItemStack(Blocks.chest), chest);
			values.put(mcI = new ItemStack(Block.getBlockById(221), 1, 5), moddedChests);
			values.put(drI = new ItemStack(Block.getBlockById(2707)), drawers);
			values.put(palaMachI = new ItemStack(Block.getBlockById(243)), palaMach);
			
			items.add(chestI);
			items.add(mcI);
			items.add(drI);
			items.add(palaMachI);
		}else {
			int chest = 0, ec = 0;
			for(Object o : mc.theWorld.loadedTileEntityList) {
				amountTiles++;
				if(o instanceof TileEntityChest) {
					chest++;
				}
				if(o instanceof TileEntityEnderChest) {
					ec++;
				}
			}
			ItemStack chestI, endrI;
			
			values.put(chestI = new ItemStack(Blocks.chest), chest);
			values.put(endrI = new ItemStack(Blocks.ender_chest), ec);
			
			items.add(chestI);
			items.add(endrI);
		}
		
		
		int x = 120;
		if(DeltaAPI.getClient().getModuleManager().getModule("HUD").getSetting("Mode").getComboValue().equalsIgnoreCase("Altas")) {
			x = 90;
		}
		
		Fonts.font.drawStringWithShadow("Total: ", x + 4, 50 - 35, -1);
		Fonts.font.drawStringWithShadow(EnumChatFormatting.RED + "" + amountTiles + "%", x + 22 - Fonts.font.getStringWidth(amountTiles + "%") / 2, 62 - 35, -1);
		x += 50;
		
		for(ItemStack i : items) {
			GL11.glPushMatrix(); 
			{
				GL11.glScaled(2, 2, 2);
				itemRenderer.renderItemAndEffectIntoGUI(Fonts.mcFont, mc.renderEngine, i, x / 2, 40 / 2 - (35 / 2));
				GL11.glScaled(0.5, 0.5, 0.5);
			} 
			GL11.glPopMatrix();
			String str = Math.min(values.get(i), 100) + "%";
			Fonts.font.drawStringWithShadow(str, x + 16 - Fonts.font.getStringWidth(str) / 2, 70 - 35, -1);
			x += 50;
		}
//		values.clear();
	}
	
	@Override
	public void onEnable() {
		itemRenderer = new RenderItem();
		super.onEnable();
	}
	
}
