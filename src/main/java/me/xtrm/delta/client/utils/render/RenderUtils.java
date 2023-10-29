package me.xtrm.delta.client.utils.render;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DONT_CARE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetBoolean;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.xtrm.delta.client.utils.CachedResource;
import me.xtrm.delta.client.utils.Location;
import me.xtrm.delta.client.utils.WebUtils;
import me.xtrm.delta.client.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;

public class RenderUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	private static Map<CachedResource, DynamicTexture> resourceCache = Maps.newHashMap();
	
	public static int loadCachedResource(CachedResource c) {
		return resourceCache.computeIfAbsent(c, cr -> {
			try {
				return new DynamicTexture(ImageIO.read(cr.getFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}).getGlTextureId();
	}
	
	public static void bindCachedResource(CachedResource c) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, loadCachedResource(c));
	}
	
	private static Field fastRenderField;

    static {
        try {
            fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
        } catch(NoSuchFieldException ignored) { }
    }

    public static void disableFastRender() {
        try {
            if(fastRenderField != null) {
                if(!fastRenderField.isAccessible()) {
                    fastRenderField.setAccessible(true);
                }

                fastRenderField.setBoolean(mc.gameSettings, false);
                mc.gameSettings.saveOptions();
            }
        }catch(final IllegalAccessException ignored) {}
    }
    
    public static void drawBlockBox(Location blockPos, final Color color, final boolean outline) {
        final Timer timer = Wrapper.timer;

        final double x = blockPos.getX() - RenderManager.renderPosX;
        final double y = blockPos.getY() - RenderManager.renderPosY;
        final double z = blockPos.getZ() - RenderManager.renderPosZ;

        AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(x, y, z, x + 1.0, y + 1.0, z + 1.0);
        Block block = blockPos.getBlock();

        if (block != null) {
            final EntityPlayer player = mc.thePlayer;

            final double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) timer.renderPartialTicks;
            final double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) timer.renderPartialTicks;
            final double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) timer.renderPartialTicks;
            axisAlignedBB = block.getSelectedBoundingBoxFromPool(mc.theWorld, (int)blockPos.getX(), (int)blockPos.getY(), (int)blockPos.getZ())
                    .expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D)
                    .offset(-posX, -posY, -posZ);
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, color.getAlpha() != 255 ? color.getAlpha() / 255 : outline ? 26 : 35);
        drawFilledBox(axisAlignedBB);

        if (outline) {
            glLineWidth(1F);
            enableGlCap(GL_LINE_SMOOTH);
            glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, color.getAlpha() / 255);
            RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, color.getRGB());
        }

        glDepthMask(true);
        resetCaps();
    }

    public static void drawEntityBox(final Entity entity, final Color color, final boolean outline) {
        final Timer timer = Wrapper.timer;

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        enableGlCap(GL_BLEND);
        disableGlCap(GL_TEXTURE_2D, GL_DEPTH_TEST);
        glDepthMask(false);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                - RenderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                - RenderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                - RenderManager.renderPosZ;

        final AxisAlignedBB entityBox = entity.boundingBox;
        final AxisAlignedBB axisAlignedBB = AxisAlignedBB.getBoundingBox(
                entityBox.minX - entity.posX + x - 0.05D,
                entityBox.minY - entity.posY + y,
                entityBox.minZ - entity.posZ + z - 0.05D,
                entityBox.maxX - entity.posX + x + 0.05D,
                entityBox.maxY - entity.posY + y + 0.15D,
                entityBox.maxZ - entity.posZ + z + 0.05D
        );

        if (outline) {
            glLineWidth(1F);
            enableGlCap(GL_LINES);
            glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, 95 / 255);
            RenderGlobal.drawOutlinedBoundingBox(axisAlignedBB, color.getRGB());
        }

        glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, outline ? 26 / 255 : 35 / 255);
        drawFilledBox(axisAlignedBB);
        glDepthMask(true);
        resetCaps();
    }

    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color) {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(2F);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glColor4d(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, color.getAlpha() / 255);
        drawFilledBox(axisAlignedBB);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDisable(GL_BLEND);
    }

    public static void drawPlatform(final double y, final Color color, final double size) {
        final double renderY = y - RenderManager.renderPosY;

        drawAxisAlignedBB(AxisAlignedBB.getBoundingBox(size, renderY + 0.02D, size, -size, renderY, -size), color);
    }

    public static void drawPlatform(final Entity entity, final Color color) {
        final Timer timer = Wrapper.timer;

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                - RenderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                - RenderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                - RenderManager.renderPosZ;

        final AxisAlignedBB axisAlignedBB = entity.boundingBox
                .offset(-entity.posX, -entity.posY, -entity.posZ)
                .offset(x, y, z);

        drawAxisAlignedBB(
        		AxisAlignedBB.getBoundingBox(axisAlignedBB.minX, axisAlignedBB.maxY + 0.2, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY + 0.26, axisAlignedBB.maxZ),
                color
        );
    }

    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.draw();
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.draw();
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(GL_LINES);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        tessellator.addVertex(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        tessellator.draw();
    }
	
	public static void init3D() {
		glDisable( GL_TEXTURE_2D );
		glDisable( GL_DEPTH_TEST );
		glDisable( GL_CULL_FACE );
		glDepthMask(false);
		glEnable( GL_BLEND );
		glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		glLineWidth( 1f );
	}
	
	public static void reset3D() {
		glDepthMask(true);
		glDisable( GL_BLEND );
		glEnable( GL_TEXTURE_2D );
		glEnable( GL_DEPTH_TEST );
		glEnable( GL_CULL_FACE );
	}
	
	public static int getIntFromColor(int r, int g, int b){
        r = (r << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        g = (g << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        b = b & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | r | g | b; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
	
    public static int getIntFromColor(Color color){
        return color.getRGB();
    }
    
    public static Color getColorFromHex(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return new Color(r, g, b);
    }

    public static Map<String, ResourceLocation> cache2;
    public static ResourceLocation loadHead() {
    	return loadHead(mc.getSession().func_148256_e().getId());
	}
    
    public static ResourceLocation loadHead(UUID uuid) {
    	return loadHead(uuid.toString());
    }
    
    public static ResourceLocation loadHead(String string) {
    	if(cache2 == null)
    		cache2 = new HashMap<String, ResourceLocation>();
		if(cache2.containsKey(string))
			return cache2.get(string);
		
		ResourceLocation head = new ResourceLocation("heads/" + string);
		ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, String.format("https://minotar.net/helm/%s/64.png", string), null, null);
		Minecraft.getMinecraft().getTextureManager().loadTexture(head, textureHead);
		cache2.put(string, head);
		return head;
	}
    
    public static Map<String, ResourceLocation> cache = new HashMap<>();
    public static ResourceLocation loadFullBody(String uuid) {
    	if(cache.containsKey(uuid))
    		return cache.get(uuid);
    	
    	ResourceLocation head = new ResourceLocation("body/" + uuid);
		ThreadDownloadImageData textureHead = new ThreadDownloadImageData(null, String.format("https://visage.surgeplay.com/full/%s", uuid), null, null);
		Minecraft.getMinecraft().getTextureManager().loadTexture(head, textureHead);
		cache.put(uuid, head);
		return head;
    }
    
    private static Map<String, String> uuids = new HashMap<>();
    public static String getUUID(String username) {
    	if(uuids.containsKey(username)) {
    		return uuids.get(username);
    	}
    	
    	uuids.put(username, "c06f89064c8a49119c29ea1dbd1aab82");
    	
    	Thread t = new Thread( () -> {
    		try {
				String json = WebUtils.readUrl(new URL("https://api.mojang.com/users/profiles/minecraft/" + username));
				Gson gson = new GsonBuilder().create();
				JsonObject owo = gson.fromJson(json, JsonObject.class);
				uuids.put(username, owo.get("id").getAsString());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	});
    	t.setDaemon(true);
    	t.start();
    	
    	return "c06f89064c8a49119c29ea1dbd1aab82";
    }
    
	public static void drawFullCircle(double d, double e, double r, int c) {
		r *= 2.0D;
		d *= 2.0D;
		e *= 2.0D;
		float f = (c >> 24 & 0xFF) / 255.0F;
		float f2 = (c >> 16 & 0xFF) / 255.0F;
		float f3 = (c >> 8 & 0xFF) / 255.0F;
		float f4 = (c & 0xFF) / 255.0F;
		enableGL2D();
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glBegin(6);
		int i = 0;
		while (i <= 360) {
			double x = Math.sin(i * 3.141592653589793D / 180.0D) * r;
			double y = Math.cos(i * 3.141592653589793D / 180.0D) * r;
			GL11.glVertex2d(d + x, e + y);
			i++;
		}
		GL11.glEnd();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		disableGL2D();
	}
	
	public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline, int background) {
		Gui.drawRect((int)x1, (int)y1, (int)x2, (int)y2, outline);
        Gui.drawRect((int)x1+1, (int)y1+1, (int)x2-1, (int)y2-1, inline);
        Gui.drawRect((int)x1+2, (int)y1+2, (int)x2-2, (int)y2-2, background);
    }
	
	public static void drawRectWH(double x, double y, double width, double height, int color) {
        Gui.drawRect((int)x, (int)y, (int)x + (int)width, (int)y + (int)height, color);
    }
	
	public static void drawOutlinedBlock(float fx, double bx, double by, double bz, int color) {
		float dx = (float)mc.thePlayer.posX;
		float dy = (float)mc.thePlayer.posY;
		float dz = (float)mc.thePlayer.posZ;
		float mx = (float)mc.thePlayer.prevPosX;
		float my = (float)mc.thePlayer.prevPosY;
		float mz = (float)mc.thePlayer.prevPosZ;
		float px = mx + ( dx - mx ) * fx;
		float py = my + ( dy - my ) * fx;
		float pz = mz + ( dz - mz ) * fx;
		
		float f = 0.0f;
		float f1 = 1.0f;
		float y = 1f;
		
		Tessellator tes = Tessellator.instance;
		
		tes.startDrawing( GL_LINES );
		tes.setColorRGBA_I(color, 255);
		tes.setBrightness( 200 );
		
		// Bottom
		tes.addVertex( bx-px + f, by-py + f1, bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f1, bz-pz + f);
		tes.addVertex( bx-px + f1,  by-py + f1, bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f1, bz-pz + f1); 
		tes.addVertex( bx-px + f1,  by-py + f1, bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f1,  bz-pz + f1);
		tes.addVertex( bx-px + f,  by-py + f1,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f1,  bz-pz + f);

		// Top
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1);
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1);
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f);
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f);
		
		// Corners
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f1,  by-py + y,  bz-pz + f1); // Top Left
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + y,  bz-pz + f); // Bottom Left
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f1); // Top Right
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f); // Bottom Right
		
		tes.draw();
	}
	
	public static void drawOutlinedEntity(float fx, EntityLivingBase entity, int color) {
		float dx = (float)mc.thePlayer.posX;
		float dy = (float)mc.thePlayer.posY;
		float dz = (float)mc.thePlayer.posZ;
		float mx = (float)mc.thePlayer.prevPosX;
		float my = (float)mc.thePlayer.prevPosY;
		float mz = (float)mc.thePlayer.prevPosZ;
		float px = mx + ( dx - mx ) * fx;
		float py = my + ( dy - my ) * fx;
		float pz = mz + ( dz - mz ) * fx;
		
		double bx = entity.posX - (entity.width / 2);
		double by = entity.posY;
		double bz = entity.posZ - (entity.width / 2);
		
		float f = 0.0f;
		float f1 = entity.width;
		float y = entity.height;
		
		Tessellator tes = Tessellator.instance;
		
		tes.startDrawing( GL_LINES );
		tes.setColorRGBA_I(color, 255);
		tes.setBrightness( 200 );
		
		// Top
		tes.addVertex( bx-px + f, by-py + y, bz-pz + f); tes.addVertex( bx-px + f1,  by-py + y, bz-pz + f);
		tes.addVertex( bx-px + f1,  by-py + y, bz-pz + f); tes.addVertex( bx-px + f1,  by-py + y, bz-pz + f1); 
		tes.addVertex( bx-px + f1,  by-py + y, bz-pz + f1); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f1);
		tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f);

		// Bottom
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1);
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1);
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f);
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f);
		
		// Corners
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f1,  by-py + y,  bz-pz + f1); // Top Left
		tes.addVertex( bx-px + f1,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f1,  by-py + y,  bz-pz + f); // Bottom Left
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f1); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f1); // Top Right
		tes.addVertex( bx-px + f,  by-py + f,  bz-pz + f); tes.addVertex( bx-px + f,  by-py + y,  bz-pz + f); // Bottom Right
		
		tes.draw();
	}
	
	public static void drawCircle(int x, int y, float radius, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        glColor4f(red, green, blue, alpha);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);
        for (int i = 0; i <= 360; i++)
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1F, 1F, 1F, 1F);
    }

    public static void connectPoints(int xOne, int yOne, int xTwo, int yTwo) {
        glPushMatrix();
        glEnable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(0.5F);
        glBegin(GL_LINES);
        glVertex2i(xOne, yOne);
        glVertex2i(xTwo, yTwo);
        glEnd();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }
    
    public static void setupFor2D(EntityLivingBase entity, float partialTicks) {
    	glPushMatrix();
    	glTranslated((float)entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderManager.renderPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderManager.renderPosY, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderManager.renderPosZ);
    	glRotated(-((float)mc.thePlayer.rotationYaw), 0.0F, 1.0F, 0.0F);
    	glRotated(((float)mc.thePlayer.rotationPitch), 1.0F, 0.0F, 0.0F);
        glScaled(-0.018F, -0.018F, 0.018F);
        glDisable(GL11.GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);
    }
    
    public static void disableFor2D() {
    	glEnable(GL11.GL_DEPTH_TEST);
    	glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	glPopMatrix();
    }
    
    /**
     * Enables 2D GL constants for 2D rendering.
     */
    public static void enableGL2D() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    /**
     * Disables 2D GL constants for 2D rendering.
     */
    public static void disableGL2D() {
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }

    private static final Map<Integer, Boolean> glCapMap = new HashMap<>();
    
    public static void resetCaps() {
        glCapMap.forEach(RenderUtils::setGlState);
    }

    public static void enableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void enableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, true);
    }

    public static void disableGlCap(final int cap) {
        setGlCap(cap, true);
    }

    public static void disableGlCap(final int... caps) {
        for (final int cap : caps)
            setGlCap(cap, false);
    }

    public static void setGlCap(final int cap, final boolean state) {
        glCapMap.put(cap, glGetBoolean(cap));
        setGlState(cap, state);
    }

    public static void setGlState(final int cap, final boolean state) {
        if (state)
            glEnable(cap);
        else
            glDisable(cap);
    }
}
