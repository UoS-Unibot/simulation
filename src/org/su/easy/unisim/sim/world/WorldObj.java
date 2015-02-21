package org.su.easy.unisim.sim.world;

import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.util.Line;
import org.su.easy.unisim.util.Shape2D;

/**
 * Represents an abstract simulated object in the world, which may be checked
 * for intersections.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public abstract class WorldObj extends Shape2D {
    

    /**
     * Checks whether the specified line intersects with the line/any lines this
     * WorldObj contains, and if so returns a list of the distances between the
     * line's first point (x1,y1) and the intersection point. Used by the
     * rangefinder and sonar models.
     *
     * @param line Line2D line to check for intersections with.
     * @return ArrayList with distances to intersection points.
     */
    public abstract Collection<Double> getLineIntersectionDists(Line line);
    
    public abstract boolean intersectsWith(WorldObj obj);

    /**
     * @return the current position of this WorldObj in world coordinates.
     */
    public Vector2D getPosition();

    /**
     * @return the current rotary translation of this WorldObj in radians.
     */
    public float getRotation();

}
