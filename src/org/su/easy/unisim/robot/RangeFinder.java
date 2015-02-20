package org.su.easy.unisim.robot;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.sim.world.WorldObj;

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

    private final float maxRayLength;
    private Vector2D basePoint;
    private float angle;

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
        this.basePoint = basePoint;
        this.angle = angle;
        this.maxRayLength = maxRayLength;
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
    public float findRange(Collection<WorldObj> objects) {
        double lowestDist = 0; //will return this if no intersection found

        //calculate line coordinates and create a new Line2D
        float x1 = (float) basePoint.getX(),
                y1 = (float) basePoint.getY(),
                x2 = (float) (basePoint.getX() + Math.cos(angle) * maxRayLength),
                y2 = (float) (basePoint.getX() + Math.sin(angle) * maxRayLength);

        final Line2D line = new Line2D.Float(x1, y1, x2, y2);

        for (WorldObj obj : objects) {
            //get list of intersection distances from the object. If there are no
            //intersections, the for loop will be skipped
            ArrayList<Double> dists = obj.getLineIntersectionDists(line);
            for (double d : dists) {
                //check if an intersection has been found yet, and if one has,
                //whether this intersection is closer
                if (lowestDist == 0 | d < lowestDist) {
                    lowestDist = d;
                }
            }
        }

        return (float) lowestDist;
    }

    /**
     * Sets the base point of the Range Finder.
     *
     * @param basePoint World coordinates of the base point this RangeFinder
     * projects from
     */
    public void setBasePoint(Vector2D basePoint) {
        this.basePoint = basePoint;
    }

    /**
     * Set the angle of projection of the ray
     *
     * @param angle Angle in radians of the ray
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * Get the maximum distance this RangeFinder will check for intersections.
     *
     * @return Maximum ray length in metres.
     */
    public float getMaxRayLength() {
        return maxRayLength;
    }

    /**
     * Get the angle of projection of the ray
     *
     * @return Angle in radians.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Get the current base point of the ray.
     *
     * @return Vector2D in world coordinates of the ray base point.
     */
    public Vector2D getBasePoint() {
        return basePoint;
    }

}
