package me.xtrm.delta.client.gui.overrides.mainmenu.old;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.altmanager.GuiAltLogin;
import me.xtrm.delta.client.gui.ui.AnimatedButton;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.gui.ui.oldparticle.ParticleGen;
import me.xtrm.delta.client.gui.ui.particle.ParticleSystem;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.ColorUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class CMainMenu extends GuiNormalizedScreen {
	
	/** Memes */
	private int random;
	
	/** ParticleSystem instance (DeltaMenu) */
	public static ParticleSystem particleSystem;
	
	/** ParticleGen instance (AtlasMenu) */
	public static ParticleGen particleGen;
	
	/** TimeHelper instance for particles tick*/
	private TimeHelper timer;
	
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
	private DynamicTexture viewportTexture;
	private ResourceLocation field_110351_G;
	private int panoramaTimer;
	
	public CMainMenu() {
		if(particleGen == null)
			particleGen = new ParticleGen(150);
		if(particleSystem == null)
			particleSystem = new ParticleSystem(300);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
		
		timer = new TimeHelper();
		random = (new Random()).nextInt(300);
		
		this.buttonList.clear();
		
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int buttonWidth = (sr.getScaledWidth() - 50) / 4;
		int buttonWidth2 = (sr.getScaledWidth() - 50) / 6;
		
		this.buttonList.add(new UIRoundedButton(42069, sr.getScaledWidth() - 100 - 10, sr.getScaledHeight() - 35, 100, 20, "Mode: " + (Wrapper.atlasMode ? "Altas" : "Delta")));
		
		if(Wrapper.atlasMode) return;
		
		// Add DeltaMainMenu AnimatedButton(s)
		this.buttonList.add(new AnimatedButton(1, 26, sr.getScaledHeight() / 2 - 85, buttonWidth, 85 - 35, "Solo"));
		this.buttonList.add(new AnimatedButton(2, 26 + buttonWidth, sr.getScaledHeight() / 2 - 85, buttonWidth, 85 - 35, "Multijoueur"));
		this.buttonList.add(new AnimatedButton(69, 26 + buttonWidth * 2, sr.getScaledHeight() / 2 - 85, buttonWidth2, 85 - 35, "Delta"));
		this.buttonList.add(new AnimatedButton(0, 26 + buttonWidth * 2 + buttonWidth2, sr.getScaledHeight() / 2 - 85, buttonWidth2, 85 - 35, "Options"));
		this.buttonList.add(new AnimatedButton(4, 26 + buttonWidth * 2 + buttonWidth2 * 2, sr.getScaledHeight() / 2 - 85, buttonWidth2, 85 - 35, "Quitter"));
	}	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(Wrapper.atlasMode) {
			altasMenu(mouseX, mouseY, partialTicks);
		}else {
			deltaMenu(mouseX, mouseY, partialTicks);
		}
		
    	super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void deltaMenu(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		this.drawDefaultBackground();
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF0f0f0f);
    	
		if(timer.hasReached(50)) {
			timer.reset();
			particleSystem.tick(50);
		}
		particleSystem.render(1, mouseX, mouseY);
		
		GuiUtils.getInstance().drawRoundedRect(26, sr.getScaledHeight() / 2 - 85, sr.getScaledWidth() - 27, sr.getScaledHeight() / 2 - 35, 0xCC000000, 0xCC0a0a0a);
		
		if(random == 69) {			
			Fonts.fontBig.drawStringWithShadow(
					EnumChatFormatting.GRAY + "code: ", 
					sr.getScaledWidth() / 2 - Fonts.fontBig.getStringWidth("code:") - 2, 
					sr.getScaledHeight() / 2 + 75 / 8, 
					-1
			);
			Fonts.fontBig.drawStringWithShadow(
					"Zero",
					sr.getScaledWidth() / 2 + 4, 
					sr.getScaledHeight() / 2 + 75 / 8, 
					0xA211A2
			);
			
			super.drawScreen(mouseX, mouseY, partialTicks);
			return;
		}
		
		String name = Consts.NAME.substring(0,1) + "" + EnumChatFormatting.WHITE + Consts.NAME.substring(1, Consts.NAME.length());
		
//		System.out.println("" + name);
		Fonts.fontBig.drawStringWithShadow(
				name, 
				sr.getScaledWidth() / 2 - Fonts.fontBig.getStringWidth(Consts.NAME) / 2, 
				sr.getScaledHeight() / 2 + 75 / 8, 
				0xA211A2
		);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 42069)
		{
			Wrapper.atlasMode = !Wrapper.atlasMode;
			this.mc.displayGuiScreen(new CMainMenu());
		}
		
		if (button.id == 69)
		{
//			this.mc.displayGuiScreen(new GuiDeltaMenu(this));
		}
		
		if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 4)
        {
            this.mc.shutdown();
        }
        
		super.actionPerformed(button);
	}
	
	private void altasMenu(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		this.drawDefaultBackground();
		this.panoramaTimer++;
		
		ScaledResolution sr = ScaledUtils.gen();
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.renderSkybox(p_73863_1_, p_73863_2_, p_73863_3_);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		
        particleGen.drawParticles();
        
		Gui.drawRect(0, 0, sr.getScaledWidth(), 30, 0xCE2F2F2F);
		Gui.drawRect(0, 0, 30, 30, 0xCE2F2F2F);
		if(isHovering(p_73863_1_, p_73863_2_, 0, 0, 30, 30)) {
			Gui.drawRect(0, 0, 30, 30, 0xEE2F2F2F);
		}
		
//		this.mc.getTextureManager().bindTexture(new ResourceLocation("palaclient:textures/logo.png"));
//	    this.drawTexturedModalRect(0, 0, 32, 32, 128, 128);
	    int y = 100;
	    int height = 200;
	    int buttonheight = 40;
	    
	    boolean singlehovered = isHovering(p_73863_1_, p_73863_2_, sr.getScaledWidth() / 2 - 90, y + 75 + 20, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight);
	    boolean multihovered = isHovering(p_73863_1_, p_73863_2_, sr.getScaledWidth() / 2 - 90, y + 75 + 20 + buttonheight + 5, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight + buttonheight + 5);
	    boolean optionshovered = isHovering(p_73863_1_, p_73863_2_, 30, 0, 30 + 98, 30);
	    boolean langhovered = isHovering(p_73863_1_, p_73863_2_, 30 + 98, 0, 30 + 98 + 99, 30);
	    boolean acchovered = isHovering(p_73863_1_, p_73863_2_, sr.getScaledWidth() - 200, 0, sr.getScaledWidth() - 75, 30);
	    boolean quithovered = isHovering(p_73863_1_, p_73863_2_, sr.getScaledWidth() - 75, 0, sr.getScaledWidth(), 30);
	    
	    Gui.drawRect(sr.getScaledWidth() - 200, 0, sr.getScaledWidth() - 75, 30, 0xBB0A0A0A);
	    if(acchovered) {
	    	Gui.drawRect(sr.getScaledWidth() - 200, 0, sr.getScaledWidth() - 75, 30, 0x550A0A0A);
	    }
	    
	    if(optionshovered)
	    	Gui.drawRect(30, 0, 30 + 98, 30, 0x55000000);
	    
	    if(langhovered)
	    	Gui.drawRect(30 + 98, 0, 30 + 98 + 99, 30, 0x55000000);
	    
	    GuiUtils.getInstance().drawRoundedRect(sr.getScaledWidth() / 2 - 115, y - 5, sr.getScaledWidth() / 2 + 115, height + y + 5, 0xAAE0E0E0, 0xAAE0E0E0);
	    GuiUtils.getInstance().drawRoundedRect(sr.getScaledWidth() / 2 - 110, y, sr.getScaledWidth() / 2 + 110, height + y, 0xBBE0E0E0, 0xBBE0E0E0);	    	
	    
	    GuiUtils.getInstance().drawRoundedRect(sr.getScaledWidth() / 2 - 90, y + 75 + 20, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight, singlehovered ? 0xFF272727 : 0xFF2d2d2d, singlehovered ? 0xFF272727 : 0xFF2d2d2d);
	    GuiUtils.getInstance().drawRoundedRect(sr.getScaledWidth() / 2 - 90, y + 75 + 20 + buttonheight + 5, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight + buttonheight + 5, multihovered ? 0xFF272727 : 0xFF2d2d2d, multihovered ? 0xFF272727 : 0xFF2d2d2d); //enabled button
//	    Gui.drawRect(sr.getScaledWidth() / 2 - 90, y + 75 + 20 + buttonheight + 5, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight + buttonheight + 5, multihovered ? 0xFF0b0b0b : 0xFF0b0b0b); //disabled button
	    
//	    double x = 65, yy = 160;
//		for(int i = (int)x*2; i > 0; i--) {
//			GuiUtils.getInstance().drawRect((float)(sr.getScaledWidth() / 2 - x + i), (float)yy, (float)(sr.getScaledWidth() / 2 - x + i + 1), (float)(yy + 4), RainbowUtils.effect((long)(i * 500000L), 0.9F, 100).getRGB());
//		}
	    
	    GuiUtils.getInstance().drawRoundedRect((int)(sr.getScaledWidth() / 2) - (4 * Fonts.mcFont.getStringWidth(Consts.NAME) / 2) - 15, 157, (int)(sr.getScaledWidth() / 2) + (4 * Fonts.mcFont.getStringWidth(Consts.NAME) / 2) + 15, 161, 0xBB0A0A0A, 0xBB0A0A0A);
	    
	    GL11.glPushMatrix();
	    GL11.glScalef(4, 4, 4);
	    this.drawCenteredString(Fonts.mcFont, EnumChatFormatting.DARK_PURPLE + Consts.NAME.substring(0,1) + EnumChatFormatting.GRAY + Consts.NAME.substring(1), (int)(sr.getScaledWidth() / 8), 30, 0xff422d);
	    GL11.glPopMatrix();	
	    
	    GL11.glPushMatrix();
	    GL11.glScalef(2, 2, 2);
	    Fonts.mcFont.drawStringWithShadow(EnumChatFormatting.GRAY + "Version b" + Consts.VER, (int)(sr.getScaledWidth() / 4) - Fonts.mcFont.getStringWidth("Version b" + Consts.VER) / 2, 42 * 2, -1);
	    GL11.glPopMatrix();
	    
	    GL11.glPushMatrix();	
	    GL11.glScalef(3, 3, 3);	    	
	    this.drawCenteredString(Fonts.mcFont, "D", 5, 1, ColorUtils.effect(1000000, 0.65F, 10000000).getRGB());
	    GL11.glPopMatrix();
	    
	    GL11.glPushMatrix();
	    GL11.glScalef(1.5f, 1.5f, 1.5f);
	    this.drawCenteredString(Fonts.mcFont, I18n.format("menu.singleplayer", new Object[0]), (sr.getScaledWidth() / 3), y + 40, -1);
//	    this.drawCenteredString(Fonts.mcFont, EnumChatFormatting.STRIKETHROUGH + " " + I18n.format("menu.multiplayer", new Object[0]), (sr.getScaledWidth() / 3), y + 40 + 30, -1);
	    this.drawCenteredString(Fonts.mcFont, I18n.format("menu.multiplayer", new Object[0]), (sr.getScaledWidth() / 3), y + 40 + 30, -1);
		GL11.glPopMatrix();
		
		if(quithovered) {
			drawRect(sr.getScaledWidth() - 75, 0, sr.getScaledWidth(), 30, 0x55000000);
		}
		
		int diff = (int)(75 / 2);
		Fonts.mcFont.drawStringWithShadow("Quit", (sr.getScaledWidth() - diff) - (Fonts.mcFont.getStringWidth("Quit") / 2), 10, -1);
		Fonts.mcFont.drawStringWithShadow("" + Wrapper.mc.getSession().getUsername(), (sr.getScaledWidth() - 100 - diff) - (Fonts.mcFont.getStringWidth(Wrapper.mc.getSession().getUsername()) / 2), 10, -1);
		Fonts.mcFont.drawStringWithShadow("Options", 30 + (98 / 2 - Fonts.mcFont.getStringWidth("Options") / 2), 10, -1);
		Fonts.mcFont.drawStringWithShadow("Language", 30 + 98 + (99 / 2 - Fonts.mcFont.getStringWidth("Language") / 2), 10, -1);
		
//		Color c = new Color(RainbowUtils.effect(1, 1, 1).getRed(), RainbowUtils.effect(1, 1, 1).getGreen(), RainbowUtils.effect(1, 1, 1).getBlue(), 100);
//		GuiUtils.getInstance().drawGradientRect(0, sr.getScaledHeight() / 2, sr.getScaledWidth(), sr.getScaledHeight() * 2, 0x00000000, c.getRGB());
		
		Fonts.mcFont.drawStringWithShadow("Copyright Mojang AB. Do not distribute!", sr.getScaledWidth() - Fonts.mcFont.getStringWidth("Copyright Mojang AB. Do not distribute!"), sr.getScaledHeight() - 10, -1);
		Fonts.mcFont.drawStringWithShadow(Consts.NAME + " " + Consts.VER_STR + " by xTrM_", 2, sr.getScaledHeight() - 10, -1);
	}
	
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if(!Wrapper.atlasMode) {
			super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
			return;
		}
		
		ScaledResolution sr = ScaledUtils.gen();
		int y = 100;
	    int buttonheight = 40;
	    
	    boolean singlehovered = isHovering(p_73864_1_, p_73864_2_, sr.getScaledWidth() / 2 - 90, y + 75 + 20, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight);
	    boolean multihovered = isHovering(p_73864_1_, p_73864_2_, sr.getScaledWidth() / 2 - 90, y + 75 + 20 + buttonheight + 5, sr.getScaledWidth() / 2 + 90, y + 75 + 20 + buttonheight + buttonheight + 5);
	    boolean optionshovered = isHovering(p_73864_1_, p_73864_2_, 30, 0, 30 + 98, 30);
	    boolean langhovered = isHovering(p_73864_1_, p_73864_2_, 30 + 98, 0, 30 + 98 + 99, 30);
	    boolean acchovered = isHovering(p_73864_1_, p_73864_2_, sr.getScaledWidth() - 200, 0, sr.getScaledWidth() - 75, 30);
	    boolean quithovered = isHovering(p_73864_1_, p_73864_2_, sr.getScaledWidth() - 75, 0, sr.getScaledWidth(), 30);
		
		if(isHovering(p_73864_1_, p_73864_2_, 0, 0, 30, 30) && p_73864_3_ == 0) {
//			mc.displayGuiScreen(new GuiDeltaMenu(this));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(singlehovered && p_73864_3_ == 0) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(multihovered && p_73864_3_ == 0) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(quithovered && p_73864_3_ == 0) {
			mc.shutdown();
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(acchovered && p_73864_3_ == 0) {
			mc.displayGuiScreen(new GuiAltLogin(this));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(optionshovered && p_73864_3_ == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		if(langhovered && p_73864_3_ == 0) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
		}
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
	}
	
	private boolean isHovering(int mouseX, int mouseY, int x, int y, int x2, int y2) {
		return mouseX > x && mouseX < x2 && mouseY > y && mouseY < y2;
	}
	
	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        byte b0 = 8;

        for (int k = 0; k < b0 * b0; ++k)
        {
            GL11.glPushMatrix();
            float f1 = ((float)(k % b0) / (float)b0 - 0.5F) / 64.0F;
            float f2 = ((float)(k / b0) / (float)b0 - 0.5F) / 64.0F;
            float f3 = 0.0F;
            GL11.glTranslatef(f1, f2, f3);
            GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 6; ++l)
            {
                GL11.glPushMatrix();

                if (l == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (l == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (l == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
                float f4 = 0.0F;
                tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - f4), (double)(0.0F + f4));
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - f4), (double)(1.0F - f4));
                tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + f4), (double)(1.0F - f4));
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float p_73968_1_)
    {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        byte b0 = 3;

        for (int i = 0; i < b0; ++i)
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = this.width;
            int k = this.height;
            float f1 = (float)(i - b0 / 2) / 256.0F;
            tessellator.addVertexWithUV((double)j, (double)k, (double)this.zLevel, (double)(0.0F + f1), 1.0D);
            tessellator.addVertexWithUV((double)j, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 1.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + f1), 0.0D);
            tessellator.addVertexWithUV(0.0D, (double)k, (double)this.zLevel, (double)(0.0F + f1), 0.0D);
        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_)
    {
        this.mc.getFramebuffer().unbindFramebuffer();
        GL11.glViewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f1 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float f2 = (float)this.height * f1 / 256.0F;
        float f3 = (float)this.width * f1 / 256.0F;
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        int l = this.height;
        tessellator.addVertexWithUV(0.0D, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F + f3));
        tessellator.addVertexWithUV((double)k, (double)l, (double)this.zLevel, (double)(0.5F - f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F - f3));
        tessellator.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + f2), (double)(0.5F + f3));
        tessellator.draw();
    }
}
