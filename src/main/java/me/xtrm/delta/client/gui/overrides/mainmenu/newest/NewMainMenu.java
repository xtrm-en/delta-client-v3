package me.xtrm.delta.client.gui.overrides.mainmenu.newest;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.gui.delta.GuiNewDelta;
import me.xtrm.delta.client.gui.ui.mainmenu.MainButton;
import me.xtrm.delta.client.gui.ui.mainmenu.ShowableButton;
import me.xtrm.delta.client.gui.ui.mainmenu.SmolButton;
import me.xtrm.delta.client.gui.ui.particle.ParticleSystem;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.TimeHelper;
import me.xtrm.delta.client.utils.animate.Translate;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class NewMainMenu extends MainMenuHelper {
	
	public NewMainMenu() {
		Fonts.fontBig.getFontHeight();
	}

	private static CachedResource logo = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_icon.png");
	private static CachedResource logo2 = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_title.png");
	
	private static CachedResource settingIcon = new CachedResource("https://nkosmos.github.io/assets/settings.png");
	private static CachedResource quitIcon = new CachedResource("https://nkosmos.github.io/assets/quit.png");
	
	private static ParticleSystem particleSystem;
	private static TimeHelper particleTimer;
	
	private static Translate translateAnim0, translateAnim1, rotateAnim1, rotateAnim2, rotateAnim3, scaleAnim1, scaleAnim2;
	
	private static TimeHelper animationTimer;	
	
	private static boolean initOnce, finishedInit;
	
	private static GuiScreen next;
	public static Translate cascadeAnim;
	
	/**
	 * if  0, no intro cascade
	 * if -1, intro leaving the right
	 * if  1, intro leaving the left
	 * if  2, intro coming from the right
	 * if -2, intro coming from the left
	 */
	public static int introCascade = 0;
	
	@Override
	public void initGui() {
		if(!initOnce) {
			cascadeAnim = new Translate(0, 0);
			introCascade = 1;
			
			translateAnim0 = new Translate(0, 0);
			translateAnim1 = null;
			rotateAnim1 = new Translate(0, 0);
			rotateAnim2 = new Translate(0, 0);
			rotateAnim3 = new Translate(0, 0);
			scaleAnim1 = new Translate(55, 55);
			scaleAnim2 = new Translate(0, 0);
			
			particleSystem = new ParticleSystem(300);
			
			particleTimer = new TimeHelper();
			particleTimer.reset();
			
			animationTimer = new TimeHelper();
			animationTimer.reset();
		}		
		initOnce = true;
		
		double scaleXD = 1;
		double positionOffsetFactor = 1 / scaleXD;
		
		super.initGui();
		
		ScaledResolution sr = ScaledUtils.gen();
		int correctWidth = 960;
		int currentWidth = sr.getScaledWidth();
		
		double diff = correctWidth / currentWidth;
		scaleXD = 1 / diff;
		positionOffsetFactor = diff;
		int renderedHeight = 333 / 2;
		
		int height = (int) (renderedHeight * 0.75F) + 1;
		
		this.buttonList.add(new MainButton(1, (int) ((sr.getScaledWidth() / 4 * positionOffsetFactor) + renderedHeight), (int) (sr.getScaledHeight() * positionOffsetFactor / 2 - (renderedHeight * 0.75F) / 2), 150, height, "Singleplayer", new Color(48, 222, 42, 150), 0));
		this.buttonList.add(new MainButton(2, (int) ((sr.getScaledWidth() / 4 * positionOffsetFactor) + renderedHeight) + 130, (int) (sr.getScaledHeight() * positionOffsetFactor / 2 - (renderedHeight * 0.75F) / 2), 150, height, "Multiplayer", new Color(224, 45, 45, 150), 250));
		
		this.buttonList.add(new SmolButton(3, (int)((sr.getScaledWidth() / 4 * positionOffsetFactor) + renderedHeight) + 130 + 130 + 70, (int) (sr.getScaledHeight() * positionOffsetFactor / 2 - (renderedHeight * 0.75F) / 2), height / 2, height / 2, "", settingIcon, 555, 555, 500));
		this.buttonList.add(new SmolButton(4, (int)((sr.getScaledWidth() / 4 * positionOffsetFactor) + renderedHeight) + 130 + 130 + 70, (int) (sr.getScaledHeight() * positionOffsetFactor / 2 - (renderedHeight * 0.75F) / 2) + height / 2, height / 2, height / 2, "", quitIcon, 555, 555, 750));
		
//		this.buttonList.add(new UIButton(42069, 20, 20, 80, 20, "Solo"));
//		this.buttonList.add(new UIButton(420069, 20, 42, 80, 20, "Multi"));
//		this.buttonList.add(new UIButton(4200069, 20, 64, 80, 20, "Settings"));
//		this.buttonList.add(new UIButton(4208690, 20, 86, 80, 20, "Alt Login"));
		
		if(finishedInit) {
			for(GuiButton b : (List<GuiButton>)this.buttonList) {
				if(b instanceof ShowableButton) {
					((ShowableButton)b).setupSkip();
				}
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		double scaleXD = 1;
		double positionOffsetFactor = 1 / scaleXD;
		
		ScaledResolution sr = ScaledUtils.gen();
		int correctWidth = 960;
		int currentWidth = sr.getScaledWidth();
		
		double diff = correctWidth / currentWidth;
		scaleXD = 1 / diff;
		positionOffsetFactor = diff;
		
		panoramaTimer = panoramaTimer2;
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
		
        GL11.glScaled(scaleXD, scaleXD, 0);
		
        if(translateAnim1 == null) {
        	translateAnim1 = new Translate(sr.getScaledWidth() + 350, sr.getScaledHeight() / 2 + 5);
        }
        
		if(particleTimer.hasReached(1000 / 20)) {
			particleSystem.tick(50);
			particleTimer.reset();
		}
		
		GL11.glColor4d(1, 1, 1, 1 * (translateAnim0.getX() / sr.getScaledWidth()));
		particleSystem.render(positionOffsetFactor, (float)(mouseX * positionOffsetFactor), (float)(mouseY * positionOffsetFactor));
		
		int renderedWidth = 333 / 2;
		int renderedHeight = 333 / 2;
		double scale = scaleAnim1.getX() / 100 + scaleAnim2.getX() / 100;
		
		if(finishedInit) { // if the anim is finished, update everything before draw
			update(positionOffsetFactor, sr);
		}
		
		Fonts.font.drawStringWithShadow("© nKosmos 2020, all rights reserved", sr.getScaledWidth() * positionOffsetFactor - (Fonts.font.getStringWidth("© nKosmos 2020, all rights reserved") + 5), sr.getScaledHeight() * positionOffsetFactor - (Fonts.font.getFontHeight() + 1), -1);
		Fonts.font.drawStringWithShadow(Consts.NAME + " Client " + Consts.VER_STR + " by " + Consts.AUTHOR, 2, sr.getScaledHeight() * positionOffsetFactor - (Fonts.font.getFontHeight() + 1), -1);
		
		Gui.drawRect(0, (int) (sr.getScaledHeight() * positionOffsetFactor / 2 - (renderedHeight * 0.75F) / 2), (int) (translateAnim0.getX() * positionOffsetFactor), (int) (sr.getScaledHeight() * positionOffsetFactor / 2 + (renderedHeight * 0.75F) / 2), 0xCC060606);
		
		GuiUtils.getInstance().drawFullCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), Math.abs(rotateAnim1.getX()) / 4, 0xFF1B1B1B);
		
		RenderUtils.bindCachedResource(logo);
		
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glTranslated(translateAnim1.getX() * positionOffsetFactor, translateAnim1.getY() * positionOffsetFactor, 0);
			GL11.glRotated(rotateAnim1.getX() + rotateAnim2.getX() + rotateAnim3.getX(), 0, 0, 1);
			GL11.glScaled(scale, scale, scale);
			Gui.func_152125_a(-renderedWidth / 2, -renderedHeight / 2, 0, 0, 333, 333, renderedWidth, renderedHeight, 333, 333);
		}
		GL11.glPopMatrix();
		
		if(rotateAnim2.getX() != -370) {
			GuiUtils.getInstance().drawCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), (float) (((rotateAnim2.getX() + (rotateAnim2.getX() != 0 ? -15 : 0))*2.2) * positionOffsetFactor), 360, 0xFF0A0A0A, 8);
		}
		GuiUtils.getInstance().drawCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), (float) Math.abs(rotateAnim2.getX()) / 4, 360, 0xFF0A0A0A, 8); 
		GuiUtils.getInstance().drawCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), (float) Math.abs(rotateAnim2.getX()) / 4, 360, 0xFF0A0A0A, 8); // yes, fuck you, whoever you are reading this code. it's already a fucking mess, so why not? (used to correct some render issues)
		
		double distance = Math.sqrt(Math.pow((translateAnim1.getX() - mouseX), 2) + Math.pow((translateAnim1.getY() - mouseY), 2));
		distance *= 100;
		distance = Math.round(distance);
		distance /= 100;
		
		if(distance < 94 * scaleXD && rotateAnim2.getX() == -370 && scaleAnim2.getX() == -70) {
			GuiUtils.getInstance().drawFullCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), Math.abs(rotateAnim1.getX()) / 4 + 1, 0x22FFFFFF);
			GuiUtils.getInstance().drawCircle((int)(translateAnim1.getX() * positionOffsetFactor), (int)(translateAnim1.getY() * positionOffsetFactor), (float) (Math.abs(rotateAnim2.getX()) / 4) + 0.1F, 360, 0xBBFFFFFF, 8);
		}
		
		if(translateAnim0.getX() != 0 && translateAnim0.getX() != sr.getScaledWidth() && !finishedInit) {
			RenderUtils.bindCachedResource(logo);	
			GL11.glPushMatrix();
			{
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4d(1, 1, 1, 1 - translateAnim0.getX() / sr.getScaledWidth());
				GL11.glTranslated(translateAnim1.getX() * positionOffsetFactor, translateAnim1.getY() * positionOffsetFactor, 0);
				double scalex = 1.25 + (translateAnim0.getX() / sr.getScaledWidth() * 1.4);
				GL11.glScaled(scalex, scalex, scalex);
				Gui.func_152125_a(-renderedWidth / 2, -renderedHeight / 2, 0, 0, 333, 333, renderedWidth, renderedHeight, 333, 333);
			}
			GL11.glPopMatrix();
		}
		
		if(!finishedInit) {
			update(positionOffsetFactor, sr);
		}
		
		super.drawScreen((int)(mouseX * positionOffsetFactor), (int)(mouseY * positionOffsetFactor), partialTicks);
		
		if(introCascade != 0) {
			boolean leaving = introCascade == 1 || introCascade == -1;
			boolean left = introCascade == 1 || introCascade == 2;
			
			double sped = 5;
			
			if(introCascade == 1) {
				sped = 2;
			}
			
			int uhhhhAddVariableThing = 50; // the "slope"
			int someOffset = 2; // for the animation
			boolean gud = cascadeAnim.interpolate((float) ((sr.getScaledWidth() + someOffset) * positionOffsetFactor), 0, sped);
			
			if(left) {
				if(leaving) {
//					GuiUtils.getInstance().drawParallelogram(-cascadeAnim.getX(), 0, (sr.getScaledWidth() + uhhhhAddVariableThing - cascadeAnim.getX()) * positionOffsetFactor, 0, (sr.getScaledWidth() - cascadeAnim.getX()) * positionOffsetFactor, (sr.getScaledHeight()) * positionOffsetFactor, (-uhhhhAddVariableThing - cascadeAnim.getX()) * positionOffsetFactor, (sr.getScaledHeight()) * positionOffsetFactor, 0xFF0A0A0A);
					GuiUtils.getInstance().drawParallelogram(
							- cascadeAnim.getX(), 
							0, 
							
							(sr.getScaledWidth() * positionOffsetFactor) - cascadeAnim.getX(), 
							0, 
							
							(sr.getScaledWidth() * positionOffsetFactor) - (cascadeAnim.getX() + uhhhhAddVariableThing), 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							- (cascadeAnim.getX() + uhhhhAddVariableThing), 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							0xFF0A0A0A
					);
				} else {
					GuiUtils.getInstance().drawParallelogram(
							sr.getScaledWidth() * positionOffsetFactor - cascadeAnim.getX(), 
							0, 
							
							sr.getScaledWidth() * positionOffsetFactor, 
							0, 
							
							sr.getScaledWidth() * positionOffsetFactor, 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							sr.getScaledWidth() * positionOffsetFactor - (cascadeAnim.getX() + uhhhhAddVariableThing), 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							0xFF0A0A0A
					);
				}
			}else {
				if(leaving) {
					GuiUtils.getInstance().drawParallelogram(
							cascadeAnim.getX(), 
							0, 
							
							sr.getScaledWidth() * positionOffsetFactor, 
							0, 
							
							sr.getScaledWidth() * positionOffsetFactor, 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							cascadeAnim.getX() + uhhhhAddVariableThing, 
							(sr.getScaledHeight()) * positionOffsetFactor, 
							
							0xFF0A0A0A
					);
				} else {
					
				}
			}
				
			if(gud) {
				introCascade = 0;
				if(next != null) {
					GuiScreen owo = next;
					next = null;
					mc.displayGuiScreen(owo);
				}
			}
		}
		GL11.glScaled(1/scaleXD, 1/scaleXD, 0);
	}
	
	private void update(double positionOffsetFactor, ScaledResolution sr) {
		
		if(animationTimer.hasReached(400)) {
			translateAnim1.interpolate(sr.getScaledWidth() / 4, sr.getScaledHeight() / 2 + 5, finishedInit ? 1000 : 2);
			if(translateAnim1.getX() <= ((sr.getScaledWidth() / 4) + 2)) {
				rotateAnim1.interpolate(370, 370, finishedInit ? 1000 : 2);
				scaleAnim1.interpolate(170, 170, finishedInit ? 1000 : 1.7);
				if(rotateAnim1.getX() >= 360 && scaleAnim1.getX() == 170) {
					rotateAnim2.interpolate(-370, -370, finishedInit ? 1000 : 2);
					scaleAnim2.interpolate(-70, -70, finishedInit ? 1000 : 1.7);
					if(rotateAnim2.getX() == -370 && scaleAnim2.getX() == -70) {
						if(translateAnim0.interpolate(sr.getScaledWidth(), 0, finishedInit ? 1000 : 2.5)) {
							finishedInit = true;
							
							for(GuiButton b : (List<GuiButton>)this.buttonList) {
								if(b instanceof ShowableButton) {
									((ShowableButton)b).setupDisplay();
								}
							}
						}					
						
						double t = (translateAnim0.getX() / sr.getScaledWidth());
						double alpha = Math.min(t, 1);
						
						int renderWidth = 333 / 2;
						int renderHeight = 333 / 2;
						
						RenderUtils.bindCachedResource(logo2);
						GL11.glPushMatrix();
						{
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glColor4d(1, 1, 1, alpha);
							Gui.func_152125_a((int)((sr.getScaledWidth() / 2 * positionOffsetFactor) - ((renderWidth) / 2)), (int)(sr.getScaledHeight() / 32 * positionOffsetFactor), 0, 0, 333, 333, renderWidth, renderHeight, 333, 333);
						}
						GL11.glPopMatrix();
						
//						Fonts.fontBig.drawStringWithShadow(Consts.NAME.substring(0,1) + "" + EnumChatFormatting.WHITE + Consts.NAME.substring(1, Consts.NAME.length()), (sr.getScaledWidth() / 2 * positionOffsetFactor) - Fonts.fontBig.getStringWidth(Consts.NAME) / 2, 50 * positionOffsetFactor, c.getRGB());
					}
				}
			}	
		}
	}
	
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if(introCascade != 0) return;
		
		if(!finishedInit) return;
		
		double scaleXD = 1;
		double positionOffsetFactor = 1 / scaleXD;
		
		ScaledResolution sr = ScaledUtils.gen();
		int correctWidth = 960;
		int currentWidth = sr.getScaledWidth();
		
		double diff = correctWidth / currentWidth;
		scaleXD = 1 / diff;
		positionOffsetFactor = diff;
		
		double distance = Math.sqrt(Math.pow((translateAnim1.getX() - p_73864_1_), 2) + Math.pow((translateAnim1.getY() - p_73864_2_), 2));
		distance *= 100;
		distance = Math.round(distance);
		distance /= 100;
		
		if(distance < 94 * scaleXD && rotateAnim2.getX() == -370 && scaleAnim2.getX() == -70 && p_73864_3_ == 0) {
			mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
			next = new GuiNewDelta(this);
			introCascade = 2;
			cascadeAnim = new Translate(0, 0);
			return;
		}
		
		super.mouseClicked((int) (p_73864_1_ * positionOffsetFactor), (int) (p_73864_2_ * positionOffsetFactor), p_73864_3_);
	}
	
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
//		super.keyTyped(p_73869_1_, p_73869_2_);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		if(button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		if(button.id == 3) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		if(button.id == 4) {
			mc.shutdown();
		}
		super.actionPerformed(button);
	}

	private static int panoramaTimer2 = 0;
	
	@Override 
	public void updateScreen() {
		panoramaTimer = panoramaTimer2++;
		super.updateScreen(); 
	}
}
