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
import org.evors.core.util.LookupFunctions;
import org.evors.core.util.geometry.Circle;
import org.evors.core.util.geometry.Line;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class SimulatedKJunior extends SimulatedRobotBody {

    private static final Stroke bstroke = new BasicStroke(0.01f);

    private final float maxIRLength = 0.5f;
    private final int NUM_IRs = 6;
    private final float radius;
    private final float[] irAngles;

    public SimulatedKJunior(SimulationWorld world,
            float timeStepLength) {
        super(world, Circle.getFromCenter(Vector2D.ZERO, 0.065), timeStepLength);
        //maxIRLength = (float) world.getBounds().getNorm();
        radius = 0.065f;
        irAngles = new float[NUM_IRs];
        for (int i = 0; i < NUM_IRs; i++) {
            irAngles[i] = (float) (i * (2 * Math.PI / NUM_IRs));
        }
    }

    @Override
    public void step(double velocity, double angularVelocity) {
        super.step(velocity, angularVelocity);
    }

    public Vector2D getIRBase(float angle) {
        return getShape().getCenter().add(new Vector2D(LookupFunctions.
                cos(angle), LookupFunctions.sin(angle)).scalarMultiply(radius));
    }

    private Line getIRLine(float angle) {
        return Line.fromPolarVec(getIRBase(angle), angle, maxIRLength);
    }

    public float getIRReading(float angle) {
        return getWorld().traceRay(getIRLine(angle));
    }

    @Override
    public float[] getInput() {
        float[] input = new float[NUM_IRs];
        for (int i = 0; i < NUM_IRs; i++) {
            input[i] = getIRReading((float) (irAngles[i] + getHeading()));
        }
        return input;
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
        g2.setStroke(bstroke);
        g2.setColor(Color.RED);
        float[] input = getInput();
        for (int i = 0; i < NUM_IRs; i++) {
            Line IR = Line.fromPolarVec(getIRBase(irAngles[i]), irAngles[i],
                    input[i]);
            g2.draw(IR.toLine2D());
        }
        g2.setColor(new Color(45, 45, 45));
        g2.fill(getShape().toJava2DShape());
    }

}
