package org.su.easy.unisim.robot;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.sim.world.WorldObj;
import org.su.easy.unisim.util.Line;
import org.su.easy.unisim.util.Shape2D;

/**
 * The RangeFinder class simulates a line of sight rangefinder (e.g. laser or
 * infrared). A collection of WorldObjs is provided at each update step; the
 * rangefinder loops through them and asks for intersection points from the
 * WorldObj. The RangeFinder must be moved manually by calling setBasePoint() to
 * keep it synchronised with the robot position.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class RangeFinder {

    private final Line line;

    /**
     * Initialises a new RangeFinder with specified base point, angle and
     * maximum ray length.
     *
     * @param basePoint Vector2D world coordinates of the point the range finder
     * projects out from.
     * @param angle Absolute angle in radians that the range finder projects
     * along
     * @param maxRayLength Maximum length of the ray that this range finder can
     * detect intersections; ensure this is greater than the diagonal of the
     * world bounds, or in rare instances intersections may be missed.
     */
    public RangeFinder(Vector2D basePoint, float angle, float maxRayLength) {
        this.line = Line.fromPolarVec(basePoint, angle, maxRayLength);
    }

    /**
     * Calculates the distance from the base point to the nearest object that
     * the ray intersects with.
     *
     * @param objects Collection of WorldObjs to check.
     * @return Distance, in metres, of the closest intersecting object. Returns
     * 0 if no intersection is found, although this should never happen if the
     * object collection is set up properly and the max ray length is long
     * enough.
     */
    public float findRange(Collection<Shape2D> objects) {
        double lowestDist = 0; //will return this if no intersection found

        for (Shape2D obj : objects) {
            Double dist = obj.getSmallestLineIntersectionDist(line);
            if(!dist.isNaN()) {
                if (lowestDist == 0 | dist < lowestDist) {
                    lowestDist = dist;
                }
            }
        }

        return (float) lowestDist;
    }

   public void move(double distance, double deltaAngle) {
       line.move(distance, deltaAngle);
   }

    public void rotate(double deltaAngle) {
        line.rotate(line.p1, deltaAngle);
    }
}
