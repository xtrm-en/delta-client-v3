package me.xtrm.delta.client.utils.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class GLUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void push() {
		GL11.glPushMatrix();
	}
	
	public static void pop() {
		GL11.glPopMatrix();
	}
	
	public static void color(Color color) {
		color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public static void color(double red, double green, double blue) {
		color(red, green, blue, 1);
	}
	
	public static void color(double red, double green, double blue, double alpha) {
		if(red > 1)
			red /= 255D;
		if(green > 1)
			green /= 255D;
		if(blue > 1)
			blue /= 255D;
		if(alpha > 1)
			alpha /= 255D;
		GL11.glColor4d(Math.max(0, red), Math.max(0, green), Math.max(0, blue), Math.max(0, alpha));
	}
	
	public static void scale(double scale) {
		scale(scale, scale);
	}
	
	public static void scale(double xScale, double yScale) {
		GL11.glScaled(xScale, yScale, 1);
	}
	
	public static void scissor(float x, float y, float x2, float y2) {
	    int factor = ScaledUtils.gen().getScaleFactor();
	    GL11.glScissor((int) (x * factor), (int) ((ScaledUtils.gen().getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
	}
	
	public static void enableScissorBox() {
		enable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void disableScissorBox() {
		disable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void enable(int cap) {
		GL11.glEnable(cap);
	}
	
	public static void disable(int cap) {
		GL11.glDisable(cap);
	}

}
