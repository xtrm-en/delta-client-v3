package me.xtrm.delta.client.gui.xray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import me.xtrm.delta.client.management.module.impl.render.XRay;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiBlacklist extends GuiScreen {
	
	private GuiTextField searchField;
	private int guiLeft, guiTop, xSize, ySize = 0;
	private float currentScroll = 0;
	private List<ItemStack> itemList = new ArrayList<ItemStack>();
	private List<GuiButtonItem> itemButtons = new ArrayList<GuiButtonItem>();
	private boolean wasClicking;
	private boolean isScrolling;
	
	private GuiScreen parent;
	
	private final CachedResource rl = new CachedResource("https://nkosmos.github.io/assets/blacklist_gui.png");
	private final ResourceLocation rl1 = new ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
	
	public GuiBlacklist(GuiScreen parent) {
        this.allowUserInput = true;
        this.ySize = 136;
        this.xSize = 195;
        this.parent = parent;
    }
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 998) {
            mc.displayGuiScreen(parent);
            mc.renderGlobal.loadRenderers();
        }
        if (par1GuiButton.id == 999) {
            mc.displayGuiScreen(null);
            mc.renderGlobal.loadRenderers();
        }
    }
	
	@Override
	public boolean doesGuiPauseGame() {
        return false;
    }
	
	@Override
	public void drawScreen(int mx, int my, float delta) {
		this.drawDefaultBackground();
		this.updateScroll(mx, my, delta);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderUtils.bindCachedResource(rl);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int var1 = guiLeft + 175;
		int var2 = guiTop + 18;
		int var3 = var2 + 112;
		
		mc.getTextureManager().bindTexture(rl1);
		this.drawTexturedModalRect(var1,var2 + (int)((var3 - var2 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
		
		this.searchField.drawTextBox();
		
		this.drawCenteredString(fontRendererObj, "XRay Config", width / 2, guiTop + 6, 0xFFFFFF);
		
		super.drawScreen(mx, my, delta);
		
		RenderHelper.enableGUIStandardItemLighting();
		for (GuiButtonItem btn : itemButtons) {
			btn.drawButton(mx, my);
		}
		
		for (GuiButtonItem btn : itemButtons) {
			btn.drawItemStackTooltip(mx, my);
		}
		
		RenderHelper.disableStandardItemLighting();
	}
	
	@Override
	public void handleMouseInput() {
        super.handleMouseInput();
        int var1 = Mouse.getEventDWheel();

        if (var1 != 0 && this.needsScrollBars())
        {
            int var2 = itemList.size() / 9 - 5 + 1;

            if (var1 > 0)
            {
                var1 = 1;
            }

            if (var1 < 0)
            {
                var1 = -1;
            }

            float tempScroll = this.currentScroll;
            this.currentScroll = (float)(this.currentScroll - (double)var1 / (double)var2);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }
            
            if (tempScroll != this.currentScroll) {
            	updateSearch();
            }
        }
    }
	
	@Override
	public void initGui() {
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.searchField = new GuiTextField(this.fontRendererObj, this.guiLeft + 25, this.guiTop + 140, xSize - 50, 14);
		this.searchField.setMaxStringLength(15);
		this.searchField.setTextColor(16777215);
		this.buttonList.add(new GuiButtonOption(999, width / 2 - 33 , guiTop + ySize - 20 , 50, 12, I18n.format("gui.done"), true));
		
		updateSearch();
    }
	
	@Override
	protected void keyTyped(char par1, int par2) {
		
		if (par2 != 0) {
			this.searchField.setFocused(true);
		}
		
		if (this.searchField.isFocused() && this.searchField.textboxKeyTyped(par1, par2)) {
			this.updateSearch();
		} else {
			super.keyTyped(par1, par2);
		}
    }

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
        searchField.mouseClicked(par1, par2, par3);
        
        if (par3 == 0 || par3 == 1) {
	        for (GuiButtonItem btn : itemButtons) {
	        	if (btn.mousePressed(mc, par1, par2)) {
	        		int value = -1;
	        		
	        		XRay.blacklist.removeAll(Item.getIdFromItem(btn.itemstack.getItem()));
	        		
	        		if (btn.enabled) {
	        			XRay.blacklist.remove(Item.getIdFromItem(btn.itemstack.getItem()), value);
	        		} else {
	        			XRay.blacklist.add(Item.getIdFromItem(btn.itemstack.getItem()), value);
	        		}
	        		updateSearch();
	        		break;
	        	}
	        }
        } 

        super.mouseClicked(par1, par2, par3);
    }
	
	private boolean needsScrollBars() {
		return itemList.size() >= 45;
	}
	
	@Override
	public void updateScreen() {
    	searchField.updateCursorCounter();
    }
    
    public void updateScroll(int par1, int par2, float par3) {
        boolean var4 = Mouse.isButtonDown(0);
        int var5 = this.guiLeft;
        int var6 = this.guiTop;
        int var7 = var5 + 175;
        int var8 = var6 + 18;
        int var9 = var7 + 14;
        int var10 = var8 + 112;

        if (!this.wasClicking && var4 && par1 >= var7 && par2 >= var8 && par1 < var9 && par2 < var10)
        {
            this.isScrolling = this.needsScrollBars();
        }

        if (!var4)
        {
            this.isScrolling = false;
        }

        this.wasClicking = var4;

        if (this.isScrolling)
        {
            this.currentScroll = (par2 - var8 - 7.5F) / (var10 - var8 - 15.0F);

            if (this.currentScroll < 0.0F)
            {
                this.currentScroll = 0.0F;
            }

            if (this.currentScroll > 1.0F)
            {
                this.currentScroll = 1.0F;
            }
            
            updateSearch();
        }
    }
    
	private void updateSearch()
    {
		itemButtons.clear();
		
        itemList.clear();
        ArrayList<Item> tempItems = new ArrayList<Item>();
        
        Iterator itemObjects = Item.itemRegistry.iterator();
        
        while(itemObjects.hasNext()) {
        	Item item = (Item) itemObjects.next();
        	if (item != null)
        		tempItems.add(item);
        }

        for (int i = 0; i < tempItems.size(); ++i) {
            Item item = tempItems.get(i);

            if (item != null && item instanceof ItemBlock)  {
            	item.getSubItems(item, null, itemList);
            }
        }

       Iterator var9 = itemList.iterator();
       String var10 = this.searchField.getText().toLowerCase();
        
       if (!var10.isEmpty()) {
    	   while (var9.hasNext()) {
    		   ItemStack var11 = (ItemStack)var9.next();
    		   boolean var13 = false;
    		   Iterator var6 = var11.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();
	
    		   while (true)
    		   {
    			   if (var6.hasNext())
    			   {
    				   String var7 = (String)var6.next();
	
    				   if (!var7.toLowerCase().contains(var10))
    				   {
    					   continue;
    				   }
	
    				   var13 = true;
    			   }
	
    			   if (!var13)
    			   {
    				   var9.remove();
	           		}
	
    			   break;
    		   }
    	   }
       }

       int k = this.itemList.size() / 9 - 5 + 1;
       int l = (int)(currentScroll * k + 0.5D);

       if (l < 0)
       {
           l = 0;
       }

       for (int y = 0; y < 5; ++y) {
           for (int x = 0; x < 9; ++x) {
        	   int id = x + (y + l) * 9;

               if (id >= 0 && id < this.itemList.size()) {
            	   ItemStack item = itemList.get(id);
            	   if (item == null)
            		   continue;
       			
            	   int xi = guiLeft + 9 + 18 * x;
            	   int yi = guiTop + 18 + 18 * y;
            	   
            	   GuiButtonItem btn = new GuiButtonItem(mc, 0, xi, yi, item);
            	   btn.enabled = XRay.blacklist.contains(Item.getIdFromItem(item.getItem()), item.getItemDamage());
            	   this.itemButtons.add(btn);
               }	
           }
       }
       if (!this.needsScrollBars()) {
    	   this.currentScroll = 0F;
       }
	}

}