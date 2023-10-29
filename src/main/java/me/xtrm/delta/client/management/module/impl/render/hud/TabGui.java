package me.xtrm.delta.client.management.module.impl.render.hud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.DeltaAPI;
import me.xtrm.delta.client.api.event.events.player.EventKeyboard;
import me.xtrm.delta.client.api.event.events.render.EventRender2D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.IModule;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.client.utils.fontRenderer.SynapticFontRenderer;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

public class TabGui extends GuiScreen {
	
	public int selectedCat = 0;
	public int selectedMod = 0;
	public int catTargetY = 0;
	public int modTargetY = 0;
	public int modSelectorX = 0, modSelectorY = -666;
	public int x, y, width, height;
	public int catSelectorX = 0, catSelectorY = -666;
	
	public Category selectedCategory;
	
	public boolean collapsed = false;
	
	@Handler
	public void onRender(EventRender2D event) {
		if(!DeltaAPI.getClient().getModuleManager().getModule("HUD").getSetting("TabGui").getCheckValue())
			return;
		
		if(Wrapper.mc.gameSettings.showDebugInfo) return;
		
		final String mode = DeltaAPI.getClient().getModuleManager().getModule("HUD").getSetting("Mode").getComboValue();
		
		if(mode.equalsIgnoreCase("Delta")) {
			x = 5;
			y = 80;
			height = 11;
		} else if(mode.equalsIgnoreCase("Ancien")) {
			x = 5;
			y = 42;
			height = 13;
		} else {
			x = 3;
			y = 18;
			height = 12;
		}
		
		width = 78;
		catSelectorX = x + 1;
		
		int modX = x + width + 2;
		
		int catCount = 0;
		for(Category c : Category.values()){
			if(c.isVisible()){
				catCount++;
			}
		}
		
		if(selectedCat > catCount - 1){
			selectedCat = 0;
		}
		
		if(selectedCat < 0){
			selectedCat = catCount - 1;
		}
		
		selectedCategory = Category.values()[selectedCat];
		
		catTargetY = y + (selectedCat * height);
		
		if(catSelectorY == -666)
			catSelectorY = catTargetY;
		
		if(catSelectorY < catTargetY){
			catSelectorY++;
		}
		if(catSelectorY > catTargetY){
			catSelectorY--;
		}
		if(catSelectorY < catTargetY){
			catSelectorY++;
		}
		if(catSelectorY > catTargetY){
			catSelectorY--;
		}
		
		int count1 = 0;
		for(Category c : Category.values()){
			if(c.isVisible()){
				drawRect(x, y + (count1 * height), x + width, y + (count1 + 1) * height, 0x55000000);
				count1++;
			}
		}
		
//		CategorySelector
		
		GuiUtils.getInstance().drawBorderedRect(catSelectorX - 1, catSelectorY, x + width, catSelectorY + height, 0xEE000000, 0xAAA211A2);
		
		int count2 = 0;
		for(Category c : Category.values()){
			if(c.isVisible()){
				String name = c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase();
				
				if(mode.equalsIgnoreCase("Delta"))
					Fonts.fontHUD.drawStringWithShadow(name, x + (Category.values()[selectedCat].name().equalsIgnoreCase(c.name()) ? 4 : 1), y + (count2 * height), 0xFFFFFF);
				else if(mode.equalsIgnoreCase("Ancien"))
					Fonts.font.drawStringWithShadow(name, x + (Category.values()[selectedCat].name().equalsIgnoreCase(c.name()) ? 4 : 1), y + (count2 * height), 0xFFFFFF);
				else
					Fonts.mcFont.drawStringWithShadow(name, x + (Category.values()[selectedCat].name().equalsIgnoreCase(c.name()) ? 6 : 3), y + (count2 * height) + 2, 0xFFFFFF);
				count2++;
			}
		}
		
		GuiUtils.getInstance().drawBorderedRect(x, y, width + x, y + (count1) * height, 0xEE000000, 0x00000000);
		
		if(collapsed){
			List<IModule> modules = new ArrayList<>();
			DeltaAPI.getClient().getModuleManager().getModules().stream().filter(m -> m.getCategory() == selectedCategory).forEach(modules::add);
			
			if(mode.equalsIgnoreCase("Altas"))
				Collections.sort(modules, new Comparator<IModule>() {
			    	public int compare(IModule m1, IModule m2) {		    		
				        if (Fonts.mcFont.getStringWidth(m1.getName()) > Fonts.mcFont.getStringWidth(m2.getName())) {
				        	return -1;
				        }
				        if (Fonts.mcFont.getStringWidth(m1.getName()) < Fonts.mcFont.getStringWidth(m2.getName())) {
				        	return 1;
				        }
				        return 0;
			    	}
			    });
			else
				Collections.sort(modules, new Comparator<IModule>() {					
			    	public int compare(IModule m1, IModule m2) {	
			    		SynapticFontRenderer font;
						
						if(mode.equalsIgnoreCase("Delta")) {
							font = Fonts.fontHUD;
						}else {
							font = Fonts.font;
						}
						
				        if (font.getStringWidth(m1.getName()) > font.getStringWidth(m2.getName())) {
				        	return -1;
				        }
				        if (font.getStringWidth(m1.getName()) < font.getStringWidth(m2.getName())) {
				        	return 1;
				        }
				        return 0;
			    	}
			    });
			
			int modCount = 0;
			int maxWidth = 0;
			
			for(IModule m : modules){
				if(m.getCategory() == (selectedCategory)){
					if(mode.equalsIgnoreCase("Delta")) {
						if(Fonts.fontHUD.getStringWidth(m.getName()) > maxWidth){
							maxWidth = Fonts.fontHUD.getStringWidth(m.getName());
						}
					}else if(mode.equalsIgnoreCase("Ancien")) {
						if(Fonts.font.getStringWidth(m.getName()) > maxWidth){
							maxWidth = Fonts.font.getStringWidth(m.getName());
						}
					}else if(mode.equalsIgnoreCase("Altas")) {
						if(Fonts.mcFont.getStringWidth(m.getName()) > maxWidth){
							maxWidth = Fonts.mcFont.getStringWidth(m.getName());
						}
					}
					modCount++;
				}
			}
			
			maxWidth += 10;
			
			if(selectedMod > modCount - 1){
				selectedMod = 0;
			}
			
			if(selectedMod < 0){
				selectedMod = modCount - 1;
			}
			
			int selectedCatY = selectedCat * height;
			modTargetY = y + selectedCatY + (selectedMod * height);
			
			if(modSelectorY < modTargetY){
				modSelectorY++;
			}
			if(modSelectorY > modTargetY){
				modSelectorY--;
			}
			if(modSelectorY < modTargetY){
				modSelectorY++;
			}
			if(modSelectorY > modTargetY){
				modSelectorY--;
			}
			
			int count3 = 0;
			for(IModule m : modules){
				if(m.getCategory() == (selectedCategory)){
					drawRect(modX, y + selectedCatY + (count3 * height), modX + maxWidth + 1, y + selectedCatY + (count3 + 1) * height, 0x55000000);
					count3++;
				}
			}
			
//			ModSelector
			GuiUtils.getInstance().drawBorderedRect(modX + modSelectorX, modSelectorY, modX + maxWidth + 1, modSelectorY + height, 0xEE000000, 0xAAA211A2);
			
			GuiUtils.getInstance().drawBorderedRect(modX, y + selectedCatY, modX + maxWidth + 1, y + selectedCatY + ((count3) * height), 0xEE000000, 0x00000000);
			
			int count4 = 0;
			for(IModule m : modules){
				if(m.getCategory() == (selectedCategory)){
					if(mode.equalsIgnoreCase("Delta"))
						Fonts.fontHUD.drawStringWithShadow((m.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + m.getName(), modX + (modules.get(selectedMod).getName().equalsIgnoreCase(m.getName()) ? 4 : 1), y + selectedCatY + (count4 * height), 0xFFFFFF);
					else if(mode.equalsIgnoreCase("Ancien"))
						Fonts.font.drawStringWithShadow((m.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + m.getName(), modX + (modules.get(selectedMod).getName().equalsIgnoreCase(m.getName()) ? 4 : 1), y + selectedCatY + (count4 * height), 0xFFFFFF);
					else
						Fonts.mcFont.drawStringWithShadow((m.isEnabled() ? EnumChatFormatting.WHITE : EnumChatFormatting.GRAY) + m.getName(), modX + (modules.get(selectedMod).getName().equalsIgnoreCase(m.getName()) ? 6 : 3), y + selectedCatY + (count4 * height) + 2, 0xFFFFFF);
					count4++;
				}
			}
			if(tog){
				tog = false;
				modules.get(selectedMod).toggle();
			}
		}
	}
	
	boolean tog = false;
	boolean c = false;
	boolean c2 = false;

	@Handler
	public void onKey(EventKeyboard e) {
		if(!DeltaAPI.getClient().getModuleManager().getModule("HUD").getSetting("TabGui").getCheckValue())
			return;
		
		int key = e.getKey();
		if(!collapsed){
			if(key == Keyboard.KEY_UP){
				selectedCat--;
			}
			if(key == Keyboard.KEY_DOWN){
				selectedCat++;
			}
			if(key == Keyboard.KEY_RETURN || key == Keyboard.KEY_RIGHT){
				collapsed = true;
				modSelectorY = y + (selectedCat * height) + (selectedMod * height);
			}
		}else{
			if(key == Keyboard.KEY_UP){
				selectedMod--;
			}
			if(key == Keyboard.KEY_DOWN){
				selectedMod++;
			}
			if(key == Keyboard.KEY_RETURN || key == Keyboard.KEY_RIGHT){
				tog = true;
			}
			if(key == Keyboard.KEY_LEFT){
				collapsed = false;
			}
		}
	}

}