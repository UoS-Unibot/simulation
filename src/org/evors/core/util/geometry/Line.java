/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.util.geometry;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import static java.lang.Double.NaN;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author miles
 */
public class Line extends Shape2D {

    public Vector2D p1;
    public Vector2D p2;

    public Line() {
        this(Vector2D.ZERO, Vector2D.ZERO);
    }

    public Line(Vector2D p1, Vector2D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public Collection<Line> getLines() {
        LinkedList<Line> lines = new LinkedList<>();
        lines.add(this);
        return lines; //To change body of generated methods, choose Tools | Templates.
    }

    public static Line fromCoords(double x1, double y1, double x2, double y2) {
        return new Line(new Vector2D(x1, y1), new Vector2D(x2, y2));
    }

    private static Vector2D translatePolar(Vector2D v, double angle, double length) {
        return new Vector2D(
                v.getX() + length * Math.cos(angle),
                v.getY() + length * Math.sin(angle)
        );
    }

    public static Line fromPolarVec(Vector2D p1, double angle, double length) {
        return new Line(
                p1,
                translatePolar(p1, angle, length)
        );
    }

    public static Line fromPolarCoords(double x1, double y1, double angle, double length) {
        return fromPolarVec(new Vector2D(x1, y1), angle, length);
    }

    public static Line fromCenterPoint(Vector2D center, double length, double angle) {
        return new Line(
                translatePolar(center, (angle + Math.PI) % (2 * Math.PI), length / 2),
                translatePolar(center, angle, length / 2)
        );
    }

    public static Line fromCenterPoint(double c1, double c2, double length, double angle) {
        return fromCenterPoint(new Vector2D(c1, c2), length, angle);
    }
    
    public void setFromPolar(Vector2D p1, double angle) {
        this.p1 = p1;
        this.p2 = translatePolar(p1, angle, getLength());
    }

    public LineIntersection getIntersection(Line line2) {
        return new LineIntersection(this, line2);
    }
    
    @Override
    public void rotate(double deltaAngle) {
        p2 = getRotatedPoint(p1, p2, deltaAngle);
    }

    public void rotate(Vector2D pivot, double deltaAngle) {
        p1 = getRotatedPoint(pivot, p1, deltaAngle);
        p2 = getRotatedPoint(pivot, p2, deltaAngle);
    }

    public Line2D toLine2D() {
        return new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public void render(Graphics2D g2) {
        g2.draw(toLine2D());
    }

    public double getLength() {
        return p2.subtract(p1).getNorm();
    }

    private Vector2D getRotatedPoint(Vector2D pivot, Vector2D point, double deltaAngle) {
        return new Vector2D(
                pivot.getX() + Math.cos(deltaAngle) * (point.getX() - pivot.getX())
                - Math.sin(deltaAngle) * (point.getY() - pivot.getY()),
                pivot.getY() + Math.sin(deltaAngle) * (point.getX() - pivot.getX())
                + Math.cos(deltaAngle) * (point.getY() - pivot.getY())
        );
    }

    @Override
    public void translate(Vector2D deltaMovement) {
        p1 = p1.add(deltaMovement);
        p2 = p2.add(deltaMovement);
    }

    private static double cross2D(Vector2D v1, Vector2D v2) {
        return v1.getX() * v2.getY() - v1.getY() * v2.getX();
    }

    @Override
    public String toString() {
        return "Line{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }
    
    
    
    
    public static LineIntersection getNoIntersection() {
        return LineIntersection.noIntersection();
    }
    
    

    public static class LineIntersection {

        public final boolean isIntersection;
        public final double line1DistToIntersect, line2DistToIntersect;
        public final Vector2D intersectionPoint;
        
        public static LineIntersection noIntersection() {
            return new LineIntersection();
        }

        private LineIntersection() {
            isIntersection = false;
            line1DistToIntersect = NaN;
            line2DistToIntersect = NaN;
            intersectionPoint = Vector2D.NaN;
        }

        public LineIntersection(Line line1, Line line2) {
            /**
             * Implementation of http://stackoverflow.com/a/565282
             */
            Vector2D q = line2.p1,
                    p = line1.p1,
                    s = line2.p2.subtract(line2.p1),
                    r = line1.p2.subtract(line1.p1),
                    qp = q.subtract(p);
            double rs = cross2D(r, s),
                    qpCrossR = cross2D(qp, r),
                    qpCrossS = cross2D(qp, s),
                    t = qpCrossS / rs,
                    u = qpCrossR / rs;
            if (rs == 0) {
                isIntersection = false;
                /**
                 * There are three possible cases if the cross product of r and
                 * s is 0, all of which result in Nan being returned: (1) the
                 * lines are parallel and do not intersect, (2) the lines are
                 * collinear and disjoint, with no intersection, or (3) the
                 * lines are collinear and overlapping, with no definitive
                 * intersection point.
                 */
                intersectionPoint = Vector2D.NaN;
                line1DistToIntersect = NaN;
                line2DistToIntersect = NaN;
            } else if (0 < t && t < 1
                    & 0 < u & u < 1) {
                //lines intersect, return p + tr
                intersectionPoint = p.add(t, r);
                isIntersection = true;
                line1DistToIntersect = t * r.getNorm();
                line2DistToIntersect = u * s.getNorm();
            } else {
                //non parallel and not intersecting
                intersectionPoint = Vector2D.NaN;
                isIntersection = false;
                line1DistToIntersect = NaN;
                line2DistToIntersect = NaN;
            }
        }

    }
}
