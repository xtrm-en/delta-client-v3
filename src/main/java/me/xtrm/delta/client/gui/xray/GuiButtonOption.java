package me.xtrm.delta.client.gui.xray;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

@SideOnly(Side.CLIENT)
public class GuiButtonOption extends GuiButton
{
    /** Button width in pixels */
    protected int width;

    /** Button height in pixels */
    protected int height;

    /** The x position of this control. */
    public int xPosition;

    /** The y position of this control. */
    public int yPosition;

    /** The string displayed on this control. */
    public String displayString;

    /** ID for this control. */
    public int id;

    /** True if this control is enabled, false to disable. */
    public boolean enabled;

    /** Hides the button completely if false. */
    public boolean drawButton;
    protected boolean field_82253_i;
    
    public boolean center = false;
    
    public GuiButtonOption(int par1, int par2, int par3, int par4, int par5, String par6Str, boolean center) {
    	super(par1, par2, par3, par4, par5, par6Str);
        this.enabled = true;
        this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = par6Str;
        this.center = center;
    }

    public GuiButtonOption(int par1, int par2, int par3, String par4Str, boolean enabled) {
        this(par1, par2, par3, 228, 7, par4Str, false);
        this.enabled = enabled;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            FontRenderer var4 = par1Minecraft.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            
            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0xFF444444);

            Gui.drawRect(xPosition, yPosition, xPosition + width - 1, yPosition + height - 1, 0xFF555555);
            //this.drawRect(xPosition, yPosition + 1, xPosition + width, yPosition + height - 1, 0xFFFFFFFF);
            
            this.mouseDragged(par1Minecraft, par2, par3);
            int var6 = 14737632;

            if (this.field_82253_i && this.enabled) {
            	Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, 0x40FFFFFF);
            }
            
            GL11.glPushMatrix();
            if (!center) {
	    		ScaledResolution sr = new ScaledResolution(par1Minecraft, par1Minecraft.displayWidth, par1Minecraft.displayHeight);
	    		
	    		if (!par1Minecraft.fontRenderer.getUnicodeFlag()) {
	    			GL11.glTranslatef(xPosition+2, yPosition+1, 1);
		    		GL11.glScaled(1.0D / sr.getScaleFactor(), 1.0D / sr.getScaleFactor(), 1.0D);
		    		int scale = sr.getScaleFactor() + 1 >> 1;
		    		GL11.glScalef(scale, scale, 1.0F);
		    		this.drawString(var4, this.displayString, 0, 0, var6);
	    		} else {
	    			GL11.glTranslatef(xPosition+2, yPosition-1, 1);
		    		this.drawString(var4, this.displayString, 0, 0, var6);
	    		}
	    		
	    		
	            this.drawString(var4, this.displayString, 0, 0, var6);
            } else {
	    		GL11.glTranslatef(xPosition+(width/2), yPosition - 4 + height / 2, 1);
	            this.drawCenteredString(var4, this.displayString, 0, 0, var6);
            }
            GL11.glPopMatrix();
        }
    }

    public void func_82251_b(int par1, int par2) {}

    public boolean func_82252_a()
    {
        return this.field_82253_i;
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
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
	public void mouseReleased(int par1, int par2) {}
}