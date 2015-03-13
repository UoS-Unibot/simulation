/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.sim.robot;

import org.evors.core.IRobotBody;
import java.awt.Graphics2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.io.Loggable;
import org.evors.core.util.LookupFunctions;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.util.geometry.Shape2D;

public abstract class SimulatedRobotBody implements IRobotBody, Loggable<Float> {

    private final float timeStepLength;
    private Vector2D position;
    private double heading;
    private final SimulationWorld world;
    private final Shape2D shape;
    private boolean live = true;

    public SimulatedRobotBody(SimulationWorld world, Shape2D shape,
            float timeStepLength) {
        this.timeStepLength = timeStepLength;
        position = Vector2D.ZERO;
        heading = 0;
        this.world = world;
        this.shape = shape;
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
        Vector2D changeV = new Vector2D(dist * LookupFunctions.cos(heading), dist * LookupFunctions.
                sin(heading));
        shape.translate(changeV);
        position = position.add(changeV);

        //Calculate actual rotation
        double changeA = (angularVelocity * timeStepLength) % (2 * Math.PI);
        shape.rotate(changeA);
        heading += changeA;
        
        world.checkCollisions(this);
    }

    public void doCollision(Shape2D obj) {
        live = false;
    }

    public SimulationWorld getWorld() {
        return world;
    }

    public abstract void render(Graphics2D g2);
    

}
