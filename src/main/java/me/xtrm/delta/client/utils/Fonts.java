package me.xtrm.delta.client.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.xtrm.delta.client.utils.fontRenderer.GlyphPage;
import me.xtrm.delta.client.utils.fontRenderer.SynapticFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Fonts {

	public static SynapticFontRenderer fontBig, fontWatermark, fontGui, font, fontLoadingGui, fontHUD;
	
	public static FontRenderer mcFont;
	
	static {
		mcFont = Minecraft.getMinecraft().fontRenderer;
		
		CachedResource cr = new CachedResource("https://nkosmos.github.io/assets/font/comfortaa.ttf");
		Font fontx = null;
		try {
			InputStream is = new FileInputStream(cr.getFile());
			fontx = Font.createFont(0, is);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}        
		
		if(fontx == null) {
			fontx = new Font("comfortaa", Font.PLAIN, 100);
			System.out.println("warning: font is shit");
		}
		
		char[] chars = new char[256];
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char)i;
		}
		
		GlyphPage page1 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 100), true, true);
		page1.generateGlyphPage(chars);
		page1.setupTexture();
		
		GlyphPage page2 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 74), true, true);
		page2.generateGlyphPage(chars);
		page2.setupTexture();
		
		GlyphPage page3 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 37), true, true);
		page3.generateGlyphPage(chars);
		page3.setupTexture();
		
		GlyphPage page4 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 24), true, true);
		page4.generateGlyphPage(chars);
		page4.setupTexture();

		GlyphPage page5 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 60), true, true, true);
		page5.generateGlyphPage(chars);
		page5.setupTexture();
		
		GlyphPage page6 = new GlyphPage(fontx.deriveFont(Font.PLAIN, 21), true, true);
		page6.generateGlyphPage(chars);
		page6.setupTexture();
		
		fontBig = new SynapticFontRenderer(page1, page1, page1, page1);
		fontWatermark = new SynapticFontRenderer(page2, page2, page2, page2);
		fontGui = new SynapticFontRenderer(page3, page3, page3, page3);
		font = new SynapticFontRenderer(page4, page4, page4, page4);
		fontLoadingGui = new SynapticFontRenderer(page5, page5, page5, page5);
		fontHUD = new SynapticFontRenderer(page6, page6, page6, page6);
	}
	
}
