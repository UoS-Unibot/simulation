/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.unibot.sim;

import com.google.common.collect.Lists;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.util.geometry.Line;
import org.evors.core.util.geometry.Rectangle;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.SimulatedRobotBody;

/**
 *
 * @author miles
 */
public class SimulatedUnibot extends SimulatedRobotBody {
    
    private static final float RANGEFINDER_NOISE_STDEV = 0.05f;
    private static final Stroke bstroke = new BasicStroke(0.05f);

    private final Vector2D size;
    private final float rangeFinderMaxLength;
    private final Rectangle rect;
    private Line rangeFinderLine;
    private float range;

    public SimulatedUnibot(SimulationWorld world, Vector2D size,
            float timestepLength) {
        this(Rectangle.createFromCenter(Vector2D.ZERO, size, 0), world,
                timestepLength);
    }

    public SimulatedUnibot(Rectangle rect, SimulationWorld world,
            float timeStepLength) {
        super(world, rect, timeStepLength);
        this.size = rect.getSize();
        this.rect = rect;
        rangeFinderMaxLength = (float) (world.getBounds().getNorm());
        rangeFinderLine = getNewRangeFinder();
    }

    /**
     * @return Where the base of the rangefinder is currently located.
     */
    private Vector2D getRangeFinderBase() {
        return rect.getLocalToWorldCoords(new Vector2D(size.getX() / 2, size.
                getY() / 2));
    }

    private Line getNewRangeFinder() {
        return Line.fromPolarVec(getRangeFinderBase(), getHeading(),
                rangeFinderMaxLength
        );
    }

    public Line getRangeFinderLine() {
        return rangeFinderLine;
    }

    public Line getShortenedRangeFinderLine() {
        fromPolarVec
                = Line.fromPolarVec(getRangeFinderBase(), getHeading(), range);
        return fromPolarVec;
    }
    private Line fromPolarVec;

    @Override
    public void step(double velocity, double angularVelocity) {
        super.step(velocity, angularVelocity);
        rangeFinderLine = getNewRangeFinder();
        range = (float) (getWorld().traceRay(rangeFinderLine)
                + ThreadLocalRandom.current().nextGaussian() * RANGEFINDER_NOISE_STDEV);
    }

    @Override
    public float[] getInput() {
        return new float[]{range};
    }

    @Override
    public List getHeaders() {
        return Lists.newArrayList("RobotX", "RobotY", "Heading", "Rangefinder");
    }

    @Override
    public List<Float> getDataRow() {
        float[] input = getInput();
        return Lists.newArrayList(
                (float) getPosition().getX(),
                (float) getPosition().getY(),
                (float) getHeading(),
                input[0]
        );
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(45,45,45));
        g2.fill(getShape().toJava2DShape());
        g2.setStroke(bstroke);
        g2.setColor(Color.RED);
        g2.draw(getShortenedRangeFinderLine().toLine2D());
    }

    
    
}
