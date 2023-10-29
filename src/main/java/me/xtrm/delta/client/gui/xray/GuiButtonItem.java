package me.xtrm.delta.client.gui.xray;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class GuiButtonItem extends GuiButton
{
	private final Minecraft mc;
	private final RenderItem itemRenderer = new RenderItem();
	private final FontRenderer fontRenderer;
	public final ItemStack itemstack;
	private boolean drawButton;
	private boolean mouseOver;
	
	
    public GuiButtonItem(Minecraft minecraft, int id, int x, int y, ItemStack itemstack) {
    	super(id, x, y, 0, 0, "");
        this.width = 16;
        this.height = 16;
        this.enabled = false;
        this.drawButton = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.itemstack = itemstack;
        this.mc = minecraft;
        this.fontRenderer = mc.fontRenderer;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(int par2, int par3) {
        if (this.drawButton) {
            this.mouseOver = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            drawItemStack();
            
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 500);
            if (enabled) {
            	//this.drawRect(this.xPosition+1, this.yPosition+1, this.xPosition+this.width-1, this.yPosition+this.height-1, 0xAA000000);
            	this.drawString(fontRenderer, "X", this.xPosition + 9, this.yPosition + 8, 0xFFFFFF);
            }
            
            if (this.mouseOver) {
            	Gui.drawRect(this.xPosition, this.yPosition, this.xPosition+this.width, this.yPosition+this.height, 0x80FFFFFF);
            }
            GL11.glPopMatrix();
        }
    }

    private void drawItemStack() {
        this.zLevel = 200.0F;
        itemRenderer.zLevel = 200.0F;
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, this.xPosition, this.yPosition);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, this.xPosition, this.yPosition);
        this.zLevel = 0.0F;
        itemRenderer.zLevel = 0.0F;
    }

    protected void drawItemStackTooltip(int mx, int my) {
    	if (!this.mouseOver)
    		return;
    	
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        List<?> var4 = itemstack.getTooltip(this.mc.thePlayer, true);

        if (!var4.isEmpty())
        {
            int var5 = 0;
            int var6;
            int var7;

            for (var6 = 0; var6 < var4.size(); ++var6)
            {
                var7 = this.fontRenderer.getStringWidth((String)var4.get(var6));

                if (var7 > var5)
                {
                    var5 = var7;
                }
            }

            var6 = mx + 12;
            var7 = my - 12;
            int var9 = 8;

            if (var4.size() > 1)
            {
                var9 += 2 + (var4.size() - 1) * 10;
            }

            if (var7 + var7 + var9 + 6 > this.height)
            {
                var7 = this.height - var9 + var7 - 6;
            }

            this.zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int var10 = -267386864;
            this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

            for (int var13 = 0; var13 < var4.size(); ++var13)
            {
                String var14 = (String)var4.get(var13);

                if (var13 == 0)
                {
                    var14 = "\u00a77" + itemstack.getRarity().rarityColor + var14;
                }
                else
                {
                    var14 = "\u00a77" + var14;
                }

                this.fontRenderer.drawStringWithShadow(var14, var6, var7, -1);

                if (var13 == 0)
                {
                    var7 += 2;
                }

                var7 += 10;
            }

            this.zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {}
    
    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
	public void mouseReleased(int par1, int par2) {}
}