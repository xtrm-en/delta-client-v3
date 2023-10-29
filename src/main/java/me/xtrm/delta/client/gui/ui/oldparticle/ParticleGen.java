package me.xtrm.delta.client.gui.ui.oldparticle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.xtrm.delta.client.gui.ui.oldparticle.impl.BaseParticle;
import me.xtrm.delta.client.utils.render.ScaledUtils;
import net.minecraft.client.gui.ScaledResolution;

public class ParticleGen {

    private final int amount;

    private final List<AParticle> particles = new ArrayList<>();
    private final Set<BorderingSide> borderedSides = new HashSet<>();

    private static final int CONNECT_RANGE = 50;

    // public:

    public ParticleGen(int amount) {
        this.amount = amount;
    }

    public void drawParticles() {
        if (particles.size() < amount)
            particles.add(new BaseParticle(2F));

        for(AParticle particle : particles) {
        	final BorderingSide borderingSide = resolveBorderingSide(particle.getX(), particle.getY());

            if (borderingSide != null) {
                particle.deploy(borderingSide);

                if (!borderedSides.contains(borderingSide)) borderedSides.add(borderingSide);
            } else {
                particle.setX(particle.getX() + particle.getXIncrease());
                particle.setY(particle.getY() + particle.getYIncrease());
            }

            if (particles.size() == amount && borderedSides.size() == BorderingSide.values().length) {
                particles.stream().filter(possiblyConnectable ->
                        (possiblyConnectable.getX() > particle.getX() && possiblyConnectable.getX() - particle.getX() < CONNECT_RANGE || particle.getX() > possiblyConnectable.getX() && particle.getX() - possiblyConnectable.getX() < CONNECT_RANGE) && (possiblyConnectable.getY() > particle.getY() && possiblyConnectable.getY() - particle.getY() < CONNECT_RANGE || particle.getY() > possiblyConnectable.getY() && particle.getY() - possiblyConnectable.getY() < CONNECT_RANGE)).forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }
            particle.draw();
        }
    }

    private BorderingSide resolveBorderingSide(int x, int y) {
        final ScaledResolution scaledResolution = ScaledUtils.gen();
        if (x < 0)
            return BorderingSide.LEFT;
        else if (y < 0)
            return BorderingSide.TOP;
        else if (x > scaledResolution.getScaledWidth())
            return BorderingSide.RIGHT;
        else if (y > scaledResolution.getScaledHeight())
            return BorderingSide.BOTTOM;
        else return null;
    }

}