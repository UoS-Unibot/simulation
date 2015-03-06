/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.unisim.simulation.core.SimulationWorld;
import org.unisim.simulation.geometry.Line;
import org.unisim.simulation.geometry.Shape2D;


public class SimulatedRobotBody implements IRobotBody {
    
    private final float timeStepLength;
    private Vector2D position;
    private double heading;
    private final Vector2D size;
    private final SimulationWorld world;
    private Line rangeFinderLine;
    private final Shape2D shape;

    public SimulatedRobotBody(SimulationWorld world) {
        this(world,new Vector2D(0.6,0.6),1f/60f);
    }

    
    
    public SimulatedRobotBody(SimulationWorld world,Vector2D size,float timeStepLength) {
        this.timeStepLength = timeStepLength;
        position = Vector2D.ZERO;
        this.world = world;
        this.size = size;
        shape = Shape2D.createRectangleFromCenter(position, size, heading);
        rangeFinderLine = Line.fromPolarVec(getRangeFinderBase(), heading, world.getBounds().getNorm());
    }
    
    /**
     * @return Where the base of the rangefinder is currently located.
     */
    private Vector2D getRangeFinderBase() {
        return shape.getLocalToWorldCoords(new Vector2D(size.getX() / 2,size.getY() / 2));
    }
    
    @Override
    public void setMotors(double velocity, double angularVelocity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getRange() {
        return world.findRange(rangeFinderLine);
    }

    @Override
    public double[] getSonars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        double dist = velocity * timeStepLength;
        Vector2D changeV = new Vector2D(dist * Math.cos(heading), dist * Math.sin(heading));
        shape.translate(changeV);
        position = position.add(changeV);
        rangeFinderLine.translate(changeV);
        heading = (heading + angularVelocity * timeStepLength) % (2 * Math.PI);
    }
    
    
    
}
