/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.kjunior;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.geometry.Circle;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class SimulatedKJunior extends SimulatedRobotBody {

    private static final Stroke bstroke = new BasicStroke(0.05f);
    public SimulatedKJunior(SimulationWorld world,
            float timeStepLength) {
        super(world, Circle.getFromCenter(Vector2D.ZERO, 0.065), timeStepLength);
    }
    
    

    @Override
    public void step(double velocity, double angularVelocity) {
        super.step(velocity, angularVelocity); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    @Override
    public float[] getInput() {
        return new float[]{0};
    }

    @Override
    public List<String> getHeaders() {
        return new ArrayList<>(0);
    }

    @Override
    public List<Float> getDataRow() {
        return new ArrayList<>(0);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(45,45,45));
        //g2.draw(robot.getShape().toJava2DShape());
        g2.fill(getShape().toJava2DShape());
        g2.setStroke(bstroke);
//        g2.setColor(Color.GREEN);
//        g2.draw(robot.getRangeFinderLine().toLine2D());
//        g2.setColor(Color.RED);
//        g2.draw(getShortenedRangeFinderLine().toLine2D());
    }
    
}
