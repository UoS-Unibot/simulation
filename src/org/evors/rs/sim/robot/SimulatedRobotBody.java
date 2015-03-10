/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot;

import org.evors.core.IRobotBody;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.io.Loggable;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.util.geometry.Line;
import org.evors.core.util.geometry.Shape2D;

public class SimulatedRobotBody implements IRobotBody, Loggable<Double> {

    private final float timeStepLength;
    private Vector2D position;
    private double heading;
    private final Vector2D size;
    private final SimulationWorld world;
    private Line rangeFinderLine;
    private final Shape2D shape;
    private boolean live = true;
    private double range;
    private final float rangeFinderMaxLength;

    public SimulatedRobotBody(SimulationWorld world) {
        this(world, new Vector2D(0.6, 0.6), 1f / 60f);
    }

    public SimulatedRobotBody(SimulationWorld world, Vector2D size,
            float timeStepLength) {
        this.timeStepLength = timeStepLength;
        position = Vector2D.ZERO;
        heading = 0;
        this.world = world;
        this.size = size;
        rangeFinderMaxLength = (float) (2 * world.getBounds().getNorm());
        shape = Shape2D.createRectangleFromCenter(position, size, heading);
        rangeFinderLine = getNewRangeFinder();
    }

    /**
     * @return Where the base of the rangefinder is currently located.
     */
    private Vector2D getRangeFinderBase() {
        return shape.getLocalToWorldCoords(new Vector2D(size.getX() / 2, size.
                getY() / 2));
    }

    private Line getNewRangeFinder() {
        return Line.fromPolarVec(getRangeFinderBase(), heading,
                rangeFinderMaxLength
        );
    }

    public Line getShortenedRangeFinderLine() {
        fromPolarVec
                = Line.fromPolarVec(getRangeFinderBase(), heading, getRange());
        return fromPolarVec;
    }
    private Line fromPolarVec;

    @Override
    public double getRange() {
        return range;
    }

    @Override
    public double[] getSonars() {
        return new double[]{0, 0, 0, 0};
    }

    @Override
    public boolean isLive() {
        return live;
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getHeading() {
        return heading;
    }

    public Line getRangeFinderLine() {
        return rangeFinderLine;
    }

    public Shape2D getShape() {
        return shape;
    }

    @Override
    public void step(double velocity, double angularVelocity) {
        if (!live) {
            return;
        }
        //Calculate movement vector
        double dist = velocity * timeStepLength;
        Vector2D changeV = new Vector2D(dist * Math.cos(heading), dist * Math.
                sin(heading));
        shape.translate(changeV);
        position = position.add(changeV);
        rangeFinderLine = getNewRangeFinder();

        //Calculate actual rotation
        double changeA = (angularVelocity * timeStepLength) % (2 * Math.PI);
        shape.rotate(changeA);
        heading += changeA;
        
        world.checkCollisions(this);
        range = world.findRange(rangeFinderLine);
    }

    public void doCollision(Shape2D obj) {
        live = false;
    }

    public SimulationWorld getWorld() {
        return world;
    }

    @Override
    public List getHeaders() {
        return Lists.newArrayList(
                "RobotX",
                "RobotY",
                "Heading",
                "Rangefinder",
                "Sonar1",
                "Sonar2",
                "Sonar3",
                "Sonar4"
        );
    }

    @Override
    public List<Double> getDataRow() {
        return Lists.newArrayList(
                position.getX(),
                position.getY(),
                heading,
                getRange(),
                getSonars()[0],
                getSonars()[1],
                getSonars()[2],
                getSonars()[3]
        );
    }

}
