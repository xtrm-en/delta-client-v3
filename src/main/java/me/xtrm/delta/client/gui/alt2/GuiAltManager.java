package me.xtrm.delta.client.gui.alt2;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.gui.GuiNormalizedScreen;
import me.xtrm.delta.client.gui.ui.UIButton;
import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.auth.AuthenticationType;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.RenderUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiAltManager extends GuiNormalizedScreen {
	private static final CachedResource logofull = new CachedResource("https://nkosmos.github.io/assets/deltalogo2_full.png");
	private GuiButton remove;	
	private AltLoginThread loginThread;
	private int offset;
	public Alt selectedAlt = null;
	private LoginStatus status;

	private GuiScreen parent;

	public GuiAltManager(GuiScreen parent) {
		this.parent = parent;
		this.status = LoginStatus.NOTHING_SELECTED;
	}

	@Override
	public void actionPerformed(GuiButton button) {
		switch (button.id) {
			case 0: {
				this.mc.displayGuiScreen(parent);
				break;
			}
			case 1: {
				String user = this.selectedAlt.getUsername();
				String pass = this.selectedAlt.getPassword();
				this.loginThread = new AltLoginThread(user, pass, AltManager.authType);
				this.loginThread.start();
				break;
			}
			case 2: {
				if (this.loginThread != null) {
					this.loginThread = null;
				}
				AltManager.registry.remove(this.selectedAlt);
				this.status = LoginStatus.REMOVED;
				this.selectedAlt = null;
				break;
			}
			case 3: {
				this.mc.displayGuiScreen(new GuiAddAlt(this));
				break;
			}
			case 4: {
				this.mc.displayGuiScreen(new GuiAltLogin(this));
				break;
			}
	
			case 6: {
				this.mc.displayGuiScreen(new GuiRenameAlt(this));
				break;
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float par3) {

		if (Mouse.hasWheel()) {
			int wheel = Mouse.getDWheel();
			if (wheel != 0) {
				System.out.println(wheel);
			}
			if (wheel < 0) {
				this.offset += 40;
				if (this.offset < 0) {
					this.offset = 0;
				}
			} else if (wheel > 0) {
				this.offset -= 40;
				if (this.offset < 0) {
					this.offset = 0;
				}
			}
		}

		int accListWidth = (int) (width / 3.69D);
		int accListX = (int) (width - accListWidth);
		int accListY = 70;

		Gui.drawRect(0, 0, width, height, 0xFF222222);
		
		Gui.drawRect(0, 0, width, 68, 0xFF151515);
		
		Fonts.fontHUD.drawStringWithShadow("Alt(s): " + AltManager.registry.size(), width / 64, 10, -1);
		Fonts.fontHUD.drawStringWithShadow(this.loginThread == null ? this.status.getText() : this.loginThread.getStatus().getText(), width / 64, 25, -1);
		RenderUtils.bindCachedResource(logofull);
		GL11.glPushMatrix();
		{
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glTranslated(0, 0, 0);
			func_152125_a((width/2) - 128/2, height/128, 0, 0, 256, 256, 128, (int) (64), 256, 256);
		}
		GL11.glPopMatrix();	
    	
		//////////////////////////////////////////////////////////////
		if(selectedAlt != null) {
			
			String pass = selectedAlt.getAuthenticationType().getName();
			Gui.drawRect(width / 64, height / 6, width / 64 + 256, height - (height / 4) + 64 + 16, 0xFF151515);
			Fonts.fontGui.drawStringWithShadow(selectedAlt.getDisplayName(), (width / 64) + 8 + 64 + 8, (height / 6) + 8, -1);
			Fonts.fontHUD.drawStringWithShadow("Type: " + pass, (width / 64) + 8 + 64 + 8, (height / 6) + 8 + 32, 5592405);
			Fonts.fontHUD.drawStringWithShadow("Mdp: " + "************", (width / 64) + 8, (height / 6) + 8 + 32 + 40, 5592405);
			Fonts.fontHUD.drawStringWithShadow("UUID: " + RenderUtils.getUUID((selectedAlt.getAuthenticationType() == AuthenticationType.OFFLINE ? "OfflinePlayer" : "") + selectedAlt.getDisplayName()), (width / 64) + 8, height / 6 + 8 + 32 + 40 + 24, 5592405);
			Fonts.fontHUD.drawStringWithShadow("Temps de jeu total: " + "9999999999d 23h 59min", (width / 64) + 8, (height / 6) + 8 + 32 + 40 + 24 + 24, 5592405);
			
			mc.renderEngine.bindTexture(RenderUtils.loadHead(selectedAlt.getDisplayName()));
	        GL11.glPushMatrix();
			{
				GL11.glColor4d(1, 1, 1, 1); 
				Gui.func_152125_a((width / 64) + 8, height / 6 + 8, 0, 0, 64, 64, 64, 64, 64, 64);
			}
			GL11.glPopMatrix();
			// Gui.drawRect((width  / 2) - 200 - 4, (height / 2) - 25 - 4, (width  / 2) - 200 + 142 + 4, (height / 2) - 25 + 230 + 4, 0xFF151515);
			
			mc.renderEngine.bindTexture(RenderUtils.loadFullBody(RenderUtils.getUUID(selectedAlt.getDisplayName())));
			GL11.glPushMatrix();
			{
				GL11.glColor4d(1, 1, 1, 1);
				Gui.func_152125_a(((width / 64 + 256) / 2) - 142/2, (height / 2) - 22, 0, 0, 158, 256, 142, 230, 158, 256);
			}
			GL11.glPopMatrix();
		}
		//////////////////////////////////////////////////////////////
		
		GL11.glPushMatrix();
		this.prepareScissorBox(0.0f, accListY - 2, width, height);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);

		Gui.drawRect(accListX - 4, 0, width, height, 0xFF191919);

		for (Alt alt2 : AltManager.registry) {
			if (!this.isAltInArea(accListY))
				continue;

			String name = alt2.getDisplayName().equals("") ? alt2.getUsername() : alt2.getDisplayName();
			String pass = alt2.getAuthenticationType().getName(); 
				
			if (alt2 == this.selectedAlt) {
				if (this.isMouseOverAlt(mouseX, mouseY, accListY - this.offset) && Mouse.isButtonDown(0)) {
					GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4,
							accListY - this.offset + 36, 0x20000000);
				} 
				if (this.isMouseOverAlt(mouseX, mouseY, accListY - this.offset)) {
					GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4,
							accListY - this.offset + 36, 0x20000000);
				}
				GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4, accListY - this.offset + 36, 0x60000000);
			}
			if (this.isMouseOverAlt(mouseX, mouseY, accListY - this.offset) && Mouse.isButtonDown(0)) {
				GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4,
						accListY - this.offset + 36, 0x20000000);
			}
			if (this.isMouseOverAlt(mouseX, mouseY, accListY - this.offset)) {
				GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4,
						accListY - this.offset + 36, 0x20000000);
			}

			GuiUtils.getInstance().drawRect(accListX, accListY - this.offset + 2, width - 4,
					accListY - this.offset + 36, 0x20000000);

			GL11.glPushMatrix();
			{
				mc.renderEngine.bindTexture(RenderUtils.loadHead(name));
				GL11.glColor4d(1, 1, 1, 1);
				Gui.func_152125_a(accListX + 2, accListY - this.offset + 2 + 2, 0, 0, 64, 64, 30, 30, 64, 64);
			}
			GL11.glPopMatrix();

			Fonts.fontHUD.drawStringWithShadow(name, accListX + 35, accListY - this.offset + 4, -1);
			Fonts.fontHUD.drawStringWithShadow(pass, accListX + 35, accListY - this.offset + 4 + 10, 5592405);
			accListY += 40;
		}
			
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL11.glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, par3);
		
		if (this.selectedAlt == null) {
			this.remove.enabled = false;
		} else {
			this.remove.enabled = true;
		}
		if (Keyboard.isKeyDown(200)) {
			this.offset -= 40;
			if (this.offset < 0) {
				this.offset = 0;
			}
		} else if (Keyboard.isKeyDown(208)) {
			this.offset += 40;
			if (this.offset < 0) {
				this.offset = 0;
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		
		this.buttonList.add(new UIButton(0, width - (int) (width / 3.69D) / 2 + 1, 4, (int) (width / 3.69D) / 2 -2, 30, "Cancel"));
		this.buttonList.add(remove = new UIButton(2, width - (int) (width / 3.69D) - 4, 4, (int) (width / 3.69D) / 2 + 4, 30, "Remove"));
		this.buttonList.add(new UIButton(3, width - (int) (width / 3.69D) - 4, 36, (int) (width / 3.69D) + 4, 30, "+ Add")); // "ah ouais, continue" wtfffffffffffffffffff
		
		this.remove.enabled = false;
	}

	private boolean isAltInArea(int y2) {
		if (y2 - this.offset <= height - (height / 8)) {
			return true;
		}
		return false;
	}

	private boolean isMouseOverAlt(int x2, int y2, int y1) {
		int accListWidth = (int) (width / 3.69D) + 4;
		int accListX = (int) (width - accListWidth) - 4;

		if (x2 >= accListX && y2 >= y1 && x2 <= width && y2 <= y1 + 40 && x2 >= 0 && y2 >= 70 && x2 <= width
				&& y2 <= height - 4) {
			return true;
		}
		return false;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if (this.offset < 0) {
			this.offset = 0;
		}
		System.out.println("test " + par3);
		
		int y2 = 70 - this.offset;
		for (Alt alt2 : AltManager.registry) {
			if (this.isMouseOverAlt(par1, par2, y2)) {
				if (alt2 == this.selectedAlt) {
					String user = this.selectedAlt.getUsername();
					String pass = this.selectedAlt.getPassword();
					this.loginThread = new AltLoginThread(user, pass, AltManager.authType);
					this.loginThread.start();
					return;
				}
				this.selectedAlt = alt2;
			}
			y2 += 40;
		}
		super.mouseClicked(par1, par2, par3);
	}

	public void prepareScissorBox(float x2, float y2, float x22, float y22) {
		ScaledResolution scale = ScaledUtils.gen();
		int factor = scale.getScaleFactor();
		GL11.glScissor((int) (x2 * (float) factor), (int) (((float) scale.getScaledHeight() - y22) * (float) factor),
				(int) ((x22 - x2) * (float) factor), (int) ((y22 - y2) * (float) factor));
	}
}
