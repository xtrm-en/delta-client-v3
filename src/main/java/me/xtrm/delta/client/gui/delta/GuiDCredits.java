package me.xtrm.delta.client.gui.delta;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URL;
import java.util.List;
import java.util.Random;
// import java.util.UUID;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.delta.creditData.CreditData;
import me.xtrm.delta.client.gui.ui.UIRoundedButton;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class GuiDCredits extends GuiNormalizedScreen {
	
	private int x, y, x0, y0, x1, y1;
	private boolean ASTOLFO_MODE;
	
	private static final CachedResource astolfo = new CachedResource("https://nkosmos.github.io/assets/god.png");
	
	private List<CreditData> creditDatas;
	private GuiScreen parent;
	
	public GuiDCredits(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		creditDatas = Lists.newArrayList(
				new CreditData("xTrM_", 	"7740d6e3-9f20-4381-a56a-060991a1c41c", "Développeur", 			"Créateur & Développeur de Delta Client"),
				new CreditData("Javatron",  "5a273ec4-ce00-48c0-992b-ec82e15af64b", "Ex. Dev", 				"Créateur de Delta Client"),
				new CreditData("MinRay", 	"61d0341d-8570-46bf-a302-cea0de260912", "Développeur", 			"Développeur de Delta Client, aide à la gestion des membres"),
				new CreditData("FurYzen_", 	"50c6a8cd-0a78-4e63-8cde-acbd2048dd8a", "Développeur", 			"Développeur de Delta Client"),
				new CreditData("Axo", 		"e2d71a3d-04d1-4f5d-8872-f837fdf49258", "Community Manager", 	"Créateur du delto meme, aide à la gestion des membres"),
				new CreditData("Andro24", 	"48a7999d-bb27-479f-89c5-4916033b31dd", "Ext. Dev", 			"Aide à l'analyse et à la conception de certains bypass"),
				new CreditData("??????",   	"0e68b123-6df9-4201-a822-4ffae822d429", "Ext. Dev",				"Allowing us to use ???????"),
				new CreditData("FlopiDiisk","01768c08-d221-43e7-8126-5dc97ebcaab4", "fdp", 					"gros fdp (mé pa tro)"),
				new CreditData("FuzeIII", 	"96e76f2a-d77d-4cb7-91a0-3eba67e74397", "Média", 				"Média publicitaire (et pour les mensonges osi)")
		);
		
		this.buttonList.add(new UIRoundedButton(42069, width / 2 - 100, height - 35, "Retour"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = ScaledUtils.gen();
		
		Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xFF0F0F0F);
		
		int diffEntreDeux = sr.getScaledHeight() / 35;
		int height = sr.getScaledHeight() / 16;
		
		double textFactor = (double)height / 31D; 
		double resetFactor = 1D / textFactor;
		
		if(ASTOLFO_MODE) {
			int xTimes = sr.getScaledWidth() / 32 + 1;
			int yTimes = sr.getScaledHeight() / 32 + 1;
			
			Random r = new Random();
			
			mc.renderEngine.bindTexture(RenderUtils.loadHead("5a273ec4ce0048c0992bec82e15af64b"));
			GL11.glPushMatrix();
			{
				GL11.glColor4d(1, 1, 1, 1);
				GL11.glTranslated(-16, -16, 0);
				for(int x = 0; x < xTimes; x++) {
					for(int y = 0; y < yTimes; y++) {
						int xPos = x * 33;
						int yPos = y * 33;
						Gui.func_152125_a(xPos, yPos, 0, 0, 64, 64, 32, 32, 64, 64);
					}
				}
			}
			GL11.glPopMatrix();
			
			Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xDD000000);
			
			double factor = 1163D/910D;
			int w = sr.getScaledWidth()/2;
			int h = (int) (double)(w * factor);
			
			RenderUtils.bindCachedResource(astolfo);
			
			GL11.glPushMatrix();
			{
				GL11.glScaled(0.75, 0.75, 0);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glColor4d(1, 1, 1, 1);
				GL11.glTranslated(r.nextInt(17) - 8, r.nextInt(17) - 8, 0);
				Gui.func_152125_a((int) (sr.getScaledWidth()*(1/0.75) / 2 - w/2), (int) (sr.getScaledHeight()*(1/0.75) / 2 - h / 2), 0, 0, 910, 1163, w, h, 910, 1163);
				GL11.glScaled(1/0.75, 1/0.75, 0);
			}
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			{				
				GL11.glTranslated(r.nextInt(17) - 8, r.nextInt(17) - 8, 0);
				Fonts.fontGui.drawStringWithShadow("God Astolfo > All", sr.getScaledWidth()/2 - 70, 10, -1);
			}
			GL11.glPopMatrix();
		}else {
			int xPos = sr.getScaledWidth() / 2 - sr.getScaledWidth() / 4;
			int yPos = 10;
			for(CreditData cdata : creditDatas) {
				GuiUtils.getInstance().drawRoundedRect(xPos - 2, yPos - 2, xPos + sr.getScaledWidth() / 2, yPos + height + 2, 0xCC222222, 0xCC222222);
				
				GL11.glScaled(textFactor, textFactor, textFactor);
				Fonts.font.drawStringWithShadow(cdata.name, (xPos + height + 2) * resetFactor, yPos * resetFactor, -1);
				Fonts.fontHUD.drawStringWithShadow("- " + cdata.role, (xPos + height + 6) * resetFactor, (yPos * resetFactor) + 13, -1);
				String str = EnumChatFormatting.GRAY + "" + EnumChatFormatting.ITALIC + cdata.moreInfo;
				Fonts.fontHUD.drawStringWithShadow(str, (xPos + sr.getScaledWidth() / 2 + 2)*resetFactor - Fonts.fontHUD.getStringWidth(str), yPos * resetFactor, -1);
				GL11.glScaled(resetFactor, resetFactor, resetFactor);
				
				mc.renderEngine.bindTexture(RenderUtils.loadHead(cdata.uuid));
				GL11.glPushMatrix();
				{
					GL11.glColor4d(1, 1, 1, 1);
					Gui.func_152125_a(xPos, yPos, 0, 0, 64, 64, height, height, 64, 64);
					
					if(cdata.name == "MinRay") {
						x = xPos;
						y = yPos;
					}else if(cdata.name == "xTrM_") {
						x0 = xPos;
						y0 = yPos;
					}else if(cdata.name == "FuzeIII") {
						x1 = xPos;
						y1 = yPos;
					}
				}
				GL11.glPopMatrix();
				yPos += height + diffEntreDeux;
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		ScaledResolution sr = ScaledUtils.gen(); 
		int height = sr.getScaledHeight() / 16;
		if(mouseX >= x && mouseY >= y && mouseX <= x + height && mouseY <= y + height && !ASTOLFO_MODE) {
			ASTOLFO_MODE = true;
			this.buttonList.clear();
			this.buttonList.add(new UIRoundedButton(0x45701F0, width / 2 - 100, this.height - 35, "fuck this shit i'm out"));
		} else if(mouseX >= x0 && mouseY >= y0 && mouseX <= x0 + height && mouseY <= y0 + height && !ASTOLFO_MODE) {
			try {
				if(Desktop.getDesktop().isSupported(Action.BROWSE)) {
					Desktop.getDesktop().browse(new URL("https://www.youtube.com/watch?v=S5RRCyCkiCk").toURI());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else if(mouseX >= x1 && mouseY >= y1 && mouseX <= x1 + height && mouseY <= y1 + height && !ASTOLFO_MODE) {
			try {
				if(Desktop.getDesktop().isSupported(Action.BROWSE)) {
					Desktop.getDesktop().browse(new URL("https://streamable.com/t34kv3").toURI());
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == 42069) {
			mc.displayGuiScreen(parent);
		}
		if(button.id == 0x45701F0) {
			mc.displayGuiScreen(new GuiDCredits(parent));
		}
		super.actionPerformed(button);
	}

}
