package org.su.easy.unisim.sandpit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Traces the path of a robot throughout a simulation, rendering it.
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class PathTracer {
    Path2D curPath = new Path2D.Double();

    public PathTracer(Vector2D initPosition) {
        curPath.moveTo(initPosition.getX(), initPosition.getY());
    }
    
    public void step(Vector2D newPosition) {
        curPath.lineTo(newPosition.getX(), newPosition.getY());
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(0.01f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2.draw(curPath);
    }
}
