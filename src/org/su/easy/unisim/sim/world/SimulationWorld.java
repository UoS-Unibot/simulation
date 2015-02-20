package org.su.easy.unisim.sim.world;

import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.robot.SimulatedUnibot;

/**
 * The SimulationWorld represents a collection of objects making up the world,
 * including 4 bounding lines. Can check for collisions between the robot and
 * any objects in the world (including bounding lines)
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class SimulationWorld {

    private final LinkedList<WorldObj> objects = new LinkedList<>();

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
        objects.add(new LineObj(new Vector2D(0, h / 2), w, 0));
        objects.add(new LineObj(new Vector2D(w / 2, 0), h, pi2));
        objects.add(new LineObj(new Vector2D(0, -h / 2), w, 0));
        objects.add(new LineObj(new Vector2D(-w / 2, 0), w, pi2));

    }

    /**
     * Adds all objects in a Collection to the world.
     *
     * @param objects Collection of WorldObjs
     */
    public void addWorldObjects(Collection<WorldObj> objects) {
        objects.addAll(objects);
    }

    public void checkCollisions(SimulatedUnibot robot) {
        for (WorldObj obj : objects) {
            if (obj instanceof LineObj) {
                if (robot.getRectangle().intersectsLine(((LineObj) obj).getLine())) {
                    robot.doCollision(obj);
                }
            } else if (obj instanceof RectangleObj) {
                if (robot.getRectangle().intersects(((RectangleObj) obj).getRectangle())) {
                    robot.doCollision(obj);
                }
            }
        }
    }

    /**
     * Adds a single WorldObj to the world.
     *
     * @param object WorldObj to add.
     */
    public void createWorldObject(WorldObj object) {
        objects.add(object);
    }

    /**
     * @return a collection of the current objects in the world.
     */
    public Collection<WorldObj> getObjects() {
        return objects;
    }

}
