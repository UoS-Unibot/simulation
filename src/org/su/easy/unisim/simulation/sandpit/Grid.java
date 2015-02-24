package org.su.easy.unisim.simulation.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;

/**
 * Renders an onscreen grid, with major and minor intervals.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Grid {

    private SandPitCamera camera;

    public Grid(SandPitCamera camera) {
        this.camera = camera;
    }

    private BigDecimal gridMinorInterval = new BigDecimal(0.2);

    /**
     * Get the value of gridMinorInterval
     *
     * @return the value of gridMinorInterval
     */
    public BigDecimal getGridMinorInterval() {
        return gridMinorInterval;
    }

    /**
     * Set the value of gridMinorInterval
     *
     * @param gridMinorInterval new value of gridMinorInterval
     */
    public void setGridMinorInterval(BigDecimal gridMinorInterval) {
        this.gridMinorInterval = gridMinorInterval;
    }

    private BigDecimal gridMajorInterval = new BigDecimal(1.0);

    /**
     * Get the value of gridMajorInterval
     *
     * @return the value of gridMajorInterval
     */
    public BigDecimal getGridMajorInterval() {
        return gridMajorInterval;
    }

    /**
     * Set the value of gridMajorInterval
     *
     * @param gridMajorInterval new value of gridMajorInterval
     */
    public void setGridMajorInterval(BigDecimal gridMajorInterval) {
        this.gridMajorInterval = gridMajorInterval;
    }

    /**
     * Get the value of camera
     *
     * @return the value of camera
     */
    public SandPitCamera getCamera() {
        return camera;
    }

    /**
     * Set the value of camera
     *
     * @param camera new value of camera
     */
    public void setCamera(SandPitCamera camera) {
        this.camera = camera;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(new Color(130, 130, 130));
        g2.setStroke(new BasicStroke(1 / 5));

        Rectangle2D viewRect = camera.getViewPortInWorldCoords();
        g2.draw(viewRect);
        BigDecimal curXPos = getNearestMultipleOf(viewRect.getX(), gridMinorInterval);
        while (curXPos.compareTo(new BigDecimal(viewRect.getWidth())) < 0) {
            if (curXPos.remainder(gridMajorInterval).abs().compareTo(new BigDecimal(0.001)) < 0) {
                g2.setStroke(new BasicStroke((float) (0.5 / camera.getScale())));
            } else {
                g2.setStroke(new BasicStroke((float) ((0.2) / camera.getScale())));
                //g2.setColor(new Color(40, 40, 40));
            }
            Line2D line = new Line2D.Double(curXPos.doubleValue(), viewRect.getY(), curXPos.doubleValue(), viewRect.getHeight());
            g2.draw(line);
            curXPos = curXPos.add(gridMinorInterval);
        }
        BigDecimal curYPos = getNearestMultipleOf(viewRect.getY(), gridMinorInterval);
        while (curYPos.compareTo(new BigDecimal(viewRect.getHeight())) < 0) {
            if (curYPos.remainder(gridMajorInterval).abs().compareTo(new BigDecimal(0.001)) < 0) {
                g2.setStroke(new BasicStroke((float) (0.5 / camera.getScale())));
            } else {
                g2.setStroke(new BasicStroke((float) ((0.2) / camera.getScale())));
                //g2.setColor(new Color(40, 40, 40));
            }
            Line2D line = new Line2D.Double(viewRect.getX(), curYPos.doubleValue(), viewRect.getWidth(), curYPos.doubleValue());
            g2.draw(line);
            curYPos = curYPos.add(gridMinorInterval);
        }

        if (viewRect.contains(0, 0)) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke((float) (2 / camera.getScale())));
            Line2D line1 = new Line2D.Double(0, 0, 0, 80 * gridMinorInterval.doubleValue() / camera.getScale());
            g2.draw(line1);
            Line2D line2 = new Line2D.Double(0, 0, 80 * gridMinorInterval.doubleValue() / camera.getScale(),0);
            g2.draw(line2);
        }

    }

    private BigDecimal getNearestMultipleOf(double x, BigDecimal multiple) {
        BigDecimal newX = new BigDecimal(x);

        if (multiple == BigDecimal.ZERO) {
            return BigDecimal.ZERO;
        }
        BigDecimal remainder = newX.abs().remainder(multiple);
        if (remainder == BigDecimal.ZERO) {
            return new BigDecimal(x);
        }
        if (x < 0) {
            return (newX.abs().subtract(remainder)).negate();
        }
        return newX.add(multiple).subtract(remainder);
    }

}
