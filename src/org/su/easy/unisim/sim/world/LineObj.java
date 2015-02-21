package org.su.easy.unisim.sim.world;

import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.util.Line;
import org.su.easy.unisim.util.Line.LineIntersection;

/**
 * Represents a line object in the world. For the purposes of the simulation it
 * has zero width. Currently used to represent world bounds.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class LineObj implements WorldObj {

    private float rotation;
    private Vector2D position;
    private Line line = new Line();
    
    public LineObj(Line line) {
        this.line = line;
    }


    /**
     * @return Gets the Java2D geometry Line2D object. Currently used for
     * rendering.
     */
    public Line getLine() {
        return line;
    }

    /**
     * @return the world coordinates of the center point of this line.
     */
    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Collection<Double> getLineIntersectionDists(Line line2) {
        final LinkedList<Double> arr = new LinkedList<>();
        LineIntersection li = line.getIntersection(line2);
        if(li.isIntersection)
            arr.add(li.line1DistToIntersect);
        return arr;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public boolean intersectsWith(WorldObj obj) {
        for(Line l : obj.getLines())
            if(this.getLine().getIntersection(line).isIntersection)
                return true;
        return false;        
    }

    @Override
    public Collection<Line> getLines() {
        final LinkedList<Line> lines = new LinkedList<>();
        lines.add(getLine());
        return lines;
    }

}
