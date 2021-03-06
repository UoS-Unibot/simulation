package org.evors.rs.sim.core;

import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.geometry.Intersection;
import org.evors.core.util.geometry.Line;
import org.evors.core.util.geometry.Polygon;
import org.evors.core.util.geometry.Shape2D;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 * The SimulationWorld represents a collection of objects making up the world,
 * including 4 bounding lines. Can check for collisions between the robot and
 * any objects in the world (including bounding lines)
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulationWorld {

    private final LinkedList<Shape2D> objects = new LinkedList<>();
    private final LinkedList<CollisionListener> listeners = new LinkedList<>();
    private String filename = "";

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    

    /**
     * Creates a new SimulationWorld with specified bounds. LineObjs are
     * automatically generated to form the bounding box.
     *
     * @param bounds Vector2D size of bounding box in metres.
     */
    public SimulationWorld(Vector2D bounds) {
        float pi2 = (float) Math.PI / 2;
        float w = (float) bounds.getX(),
                h = (float) bounds.getY();
        objects.add(Line.fromCenterPoint(0, h / 2, w, 0));
        objects.add(Line.fromCenterPoint(w / 2, 0, h, pi2));
        objects.add(Line.fromCenterPoint(0, -h / 2, w, 0));
        objects.add(Line.fromCenterPoint(-w / 2, 0, h, pi2));

    }
    
    public void addListener(CollisionListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds all objects in a Collection to the world.
     *
     * @param objects Collection of WorldObjs
     */
    public void addWorldObjects(Collection<Shape2D> objects) {
        this.objects.addAll(objects);
    }

    public void checkCollisions(SimulatedRobotBody robot) {
        for (Shape2D obj : objects) {
            if (obj.intersectsWith(robot.getShape())) {
                for(CollisionListener cl : listeners) {
                    if(cl.collisionOccured())
                        break;
                }
                robot.doCollision(obj);
            }
        }
    }

    /**
     * Adds a single WorldObj to the world.
     *
     * @param object WorldObj to add.
     */
    public void createWorldObject(Polygon object) {
        objects.add(object);
    }

    /**
     * @return a collection of the current objects in the world.
     */
    public Collection<Shape2D> getObjects() {
        return objects;
    }

    @Override
    public String toString() {
        return "SimulationWorld{" + "objects=" + objects + ", listeners=" + listeners + '}';
    }
    
    public Vector2D getBounds() {
        return new Vector2D(5,5);
    }

    public float traceRay(Line rangeFinderLine) {
        double lowestDist = rangeFinderLine.getLength(); //will return this if no intersection found

        for (Shape2D obj : objects) {
            Intersection li = obj.getSmallestIntersection(rangeFinderLine);
            if (li.isIntersection) {
                if (li.getSmallestLineDist() < lowestDist) {
                    lowestDist = li.getSmallestLineDist();
                }
            }
        }
        return (float) lowestDist;
    }

}
