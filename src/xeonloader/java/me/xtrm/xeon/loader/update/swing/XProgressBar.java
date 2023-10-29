package me.xtrm.xeon.loader.update.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class XProgressBar extends BasicProgressBarUI {

	@Override
    protected Dimension getPreferredInnerVertical() {
        return new Dimension(20, 146);
    }

    @Override
    protected Dimension getPreferredInnerHorizontal() {
        return new Dimension(146, 20);
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double iStrokWidth = 1D;
        double width = this.progressBar.getWidth();
        double height = this.progressBar.getHeight();
        Rectangle2D rect = new Rectangle2D.Double(0.0D, 0.0D, (double)width, (double)height);
        g2d.setColor(Color.BLACK);
        g2d.fill(rect);
        double iInnerHeight = height - iStrokWidth * 4;
        double iInnerWidth = width - iStrokWidth * 4;
        g2d.setColor(new Color(-11448236));
        Rectangle2D fill = new Rectangle2D.Double(0.0D, 0.0D, (double)iInnerWidth, (double)iInnerHeight);
        g2d.fill(fill);
        g2d.setColor(Color.BLACK);
        Rectangle2D fill1 = new Rectangle2D.Double(1.0D, 1.0D, (double)(iInnerWidth - 2), (double)(iInnerHeight - 2));
        g2d.fill(fill1);
        iInnerWidth = (double)(iInnerWidth - 4) * this.getProgress();
        g2d.setColor(Color.WHITE);
        Rectangle2D fill2 = new Rectangle2D.Double(2.0D, 2.0D, (double)(iInnerWidth - 4), (double)(iInnerHeight - 4));
        g2d.fill(fill2);
        g2d.dispose();
    }

    private double getProgress() {
        double dProgress = this.progressBar.getPercentComplete();
        if (dProgress < 0.01D) {
            return 0.01D;
        } else {
            return dProgress > 1.0D ? 1.0D : dProgress;
        }
    }

    protected void paintIndeterminate(Graphics g, JComponent c) {
        super.paintIndeterminate(g, c);
    }

}