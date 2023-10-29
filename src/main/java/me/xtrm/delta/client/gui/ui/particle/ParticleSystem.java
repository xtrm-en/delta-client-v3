package me.xtrm.delta.client.gui.ui.particle;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import me.xtrm.delta.client.utils.MathUtils;
import me.xtrm.delta.client.utils.render.GuiUtils;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.ScaledResolution;

public class ParticleSystem {
	
    private static final float SPEED = 0.15f;
    
    private List<Particle> particleList = new ArrayList<>();
    private int dist;

    public ParticleSystem(int initAmount) {
        addParticles(initAmount);
        this.dist = 80;
    }

    public void addParticles(int amount) {
        for (int i = 0; i < amount; i++) {
            particleList.add(Particle.generateParticle());
        }
    }

    public void tick(int delta) {
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    public void render(double positionOffsetFactor, float mouseX, float mouseY) {
    	ScaledResolution sr = ScaledUtils.gen();
        for (Particle particle : particleList) {
        	if(particle.getX() >= 0 && particle.getY() >= 0 && particle.getX() <= sr.getScaledWidth() * positionOffsetFactor && particle.getY() <= sr.getScaledHeight() * positionOffsetFactor) {
        		GuiUtils.getInstance().drawCircle(particle.getX(), particle.getY(), 1, 10, -1);
                
                float distance = (float) MathUtils.distance(particle.getX(), particle.getY(), mouseX, mouseY);
                if (distance < dist) {
                    float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - distance / dist));
                    drawLine(particle.getX(),
                            particle.getY(),
                            mouseX,
                            mouseY,
                            alpha);
                }
                
                float nearestDistance = 0;
                Particle nearestParticle = null;

                for (Particle particle1 : particleList) {
                    distance = particle.getDistanceTo(particle1);
                    if (distance <= dist
                            && (MathUtils.distance(mouseX, mouseY, particle.getX(), particle.getY()) <= dist
                            || MathUtils.distance(mouseX, mouseY, particle1.getX(), particle1.getY()) <= dist)
                            && (nearestDistance <= 0 || distance <= nearestDistance)) {

                        nearestDistance = distance;
                        nearestParticle = particle1;

                    }
                }

                if (nearestParticle != null) {
                    float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));
                    drawLine(particle.getX(),
                            particle.getY(),
                            nearestParticle.getX(),
                            nearestParticle.getY(),
                            alpha);
                }
        	}
        }
    }

    private void drawLine(float x, float y, float x1, float y1, float alpha) {
    	GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(0.5F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

}
