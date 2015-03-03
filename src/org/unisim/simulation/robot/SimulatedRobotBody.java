/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.simulation.robot;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;


public class SimulatedRobotBody implements IRobotBody {
    
    private final float timeStepLength;
    private Vector2D position;
    private double heading;

    public SimulatedRobotBody(float timeStepLength) {
        this.timeStepLength = timeStepLength;
        position = Vector2D.ZERO;
    }
    
    
    
    @Override
    public void setMotors(double velocity, double angularVelocity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getRange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
    

    @Override
    public void step(double velocity, double angularVelocity) {
        double dist = velocity * timeStepLength;
        position = position.add(new Vector2D(dist * Math.cos(heading), dist * Math.sin(heading)));
        heading = (heading + angularVelocity * timeStepLength) % (2 * Math.PI);
    }
    
    
    
}
