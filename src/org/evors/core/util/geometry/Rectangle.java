package org.evors.core.util.geometry;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import javax.naming.OperationNotSupportedException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The Rectangle class represents a special case of Polygon with only 4 lines.
 * Lines cannot be added or removed, and the rectangle cannot be resized (it is
 * presumed that rectangular objects will not need to change size mid
 * simulation!).
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public class Rectangle extends Polygon {
    
    private final Vector2D size;

    public Rectangle(Vector2D size) {
        this.size = size;
    }
    
    public static Rectangle createFromCenter(Vector2D center,
            Vector2D size, double rotation) {
        Rectangle rect = new Rectangle(size);
        double halfW = size.getX() / 2,
                halfH = size.getY() / 2,
                x1 = center.getX() - halfW,
                y1 = center.getY() + halfH,
                x2 = center.getX() + halfW,
                y2 = center.getY() - halfH;
        rect.addLinePrivate(Line.fromCoords(x1, y1, x1, y2));
        rect.addLinePrivate(Line.fromCoords(x1, y2, x2, y2));
        rect.addLinePrivate(Line.fromCoords(x2, y2, x2, y1));
        rect.addLinePrivate(Line.fromCoords(x2, y1, x1, y1));
        rect.rotate(rotation);
        return rect;
    }

    @Override
    public void addLine(Line line) {
        throw new UnsupportedOperationException("Cannot add line to a rectangle. Construct a new rectangle instead or use the Polygon class.");
    }

    @Override
    public Shape toJava2DShape() {
        Rectangle2D rect = new Rectangle2D.Double();
        rect.setFrame(getCenter().getX() - size.getX()/2, getCenter().getY() - size.getY()/2, size.getX(), size.getY());
        return rect;
    }    
    
    private void addLinePrivate(Line line) {
        //For the static constructors
        super.addLine(line);
    }
}
