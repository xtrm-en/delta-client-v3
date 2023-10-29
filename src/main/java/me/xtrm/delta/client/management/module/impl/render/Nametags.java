package me.xtrm.delta.client.management.module.impl.render;

import static me.xtrm.delta.client.utils.Wrapper.mc;

import java.util.regex.Pattern;

import org.lwjgl.opengl.GL11;

import me.hippo.api.lwjeb.annotation.Handler;
import me.xtrm.delta.client.api.event.events.render.EventRender3D;
import me.xtrm.delta.client.api.module.Category;
import me.xtrm.delta.client.api.module.Module;
import me.xtrm.delta.client.utils.Fonts;
import me.xtrm.delta.client.utils.render.GuiUtils;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class Nametags extends Module {

	private Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
	
	public Nametags() {
		super("Nametags", Category.RENDER);
		setDescription("Permet de voir le pseudo des autres joueurs clairement");
	}
	
	@Handler
	public void onRender3D(EventRender3D e) {
		for(Object o : mc.theWorld.loadedEntityList) {
			if(o instanceof EntityPlayer) {
				if(o instanceof EntityClientPlayerMP) continue;
				
				renderNametag((EntityPlayer)o, ((EntityPlayer)o).getCommandSenderName(), e.getPartialTicks());
			}
		}
	}
	
	private void renderNametag(EntityPlayer entity, String name, float partialTicks) {
		String text = name;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated( // Translate to player position with render pos and interpolate it
                entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX,
                entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY + entity.getEyeHeight() + 0.55,
                entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ
        );
		
		// Minecraft
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0F, 1F, 0F);
		GL11.glRotatef(RenderManager.instance.playerViewX, 1F, 0F, 0F);
		
		// Scale
        float distance = mc.thePlayer.getDistanceToEntity(entity) / 4F;

        if (distance < 1F)
            distance = 1F;

        float scale = distance / 100F;

        GL11.glScalef(-scale, -scale, scale);

        // Disable lightning and depth test
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Enable blend
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Draw nametag
        int width = Fonts.font.getStringWidth(text) / 2;
        GuiUtils.getInstance().drawRect(-width - 2F, -2F, width + 4F, Fonts.font.getFontHeight() + 2F, Integer.MIN_VALUE);
        Fonts.font.drawStringWithShadow(text, 1F + -width, 1.5F, -1);

        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glPopMatrix();
	}
}
