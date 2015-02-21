/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.util;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.util.Line.LineIntersection;

/**
 *
 * @author miles
 */
public class Shape2D {

    private final LinkedList<Line> lines;
    private Vector2D center;
    private double rotation;

    public Shape2D() {
        this(Vector2D.ZERO);
    }

    public Shape2D(Vector2D center) {
        lines = new LinkedList<>();
        this.center = center;
    }
    
    public static Shape2D createRectangleFromCenter(Vector2D center, Vector2D size, double rotation) {
        Shape2D shape = new Shape2D();
        double halfW = size.getX() / 2,
                halfH = size.getY() / 2,
                x1 = center.getX() - halfW,
                y1 = center.getY() + halfH,
                x2 = center.getX() + halfW,
                y2 = center.getY() - halfH;
        shape.addLine(Line.fromCoords(x1, y1, x1, y2));
        shape.addLine(Line.fromCoords(x1, y2, x2, y2));
        shape.addLine(Line.fromCoords(x2, y2, x2, y1));
        shape.addLine(Line.fromCoords(x2, y1, x1, y1));
        shape.rotate(rotation);
        return shape;
    }
    
    public Shape toJava2DShape() {
        Path2D s = new Path2D.Double();
        for(Line line : lines) {
            s.append(line.toLine2D(), false);
        }
        s.closePath();
        return s;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void addLine(double x1, double y1,double x2, double y2) {
        addLine(Line.fromCoords(x1, y1, x2, y2));
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void rotate(double deltaAngle) {
        rotation = (rotation + deltaAngle) % (2 * Math.PI);
        for (Line line : lines) {
            line.rotate(center, deltaAngle);
        }
    }

    public void move(double distance, double deltaAngle) {
        translate(new Vector2D(distance * Math.cos(deltaAngle),distance * Math.sin(deltaAngle)));
    }
    
    public void translate(Vector2D deltaMovement) {
        for(Line l : lines)
            l.translate(deltaMovement);
    }
    
    public Collection<Line> getLines() {
        return lines;
    }
    
    public Collection<Vector2D> getIntersectionPoints(Shape2D shape) {
        LinkedList<Vector2D> ips = new LinkedList<>();
        for(Line l1 : lines) {
            for(Line l2 : shape.getLines()) {
                LineIntersection result = l1.getIntersection(l2);
                if(result.isIntersection)
                    ips.add(result.intersectionPoint);
            }
        }
        return ips;
    }
    
    public double getSmallestLineIntersectionDist(Line line) {
        double lowestDist = Double.NaN;
        for(Line shapeLine : getLines()) {
            LineIntersection li = line.getIntersection(shapeLine);
            if(li.isIntersection) {
                if(lowestDist == Double.NaN | li.line1DistToIntersect < lowestDist) {
                    lowestDist = li.line1DistToIntersect;
                }
            }
        }
        return lowestDist;
    }
    
    public void fill(Graphics2D g2) {
        Path2D path = new Path2D.Double();
        for(Line l : lines) {
            path.append(l.toLine2D(),false);
        }
        g2.fill(path);
    }
    
    public boolean intersectsWith(Shape2D shape) {
        for(Line l1 : lines) {
            for(Line l2 : shape.getLines()) {
                if(l1.getIntersection(l2).isIntersection)
                    return true;
            }
        }
        return false;
    }

    public Vector2D getCenter() {
        return center;
    }

    public void setCenter(Vector2D center) {
        this.center = center;
    }

    public double getRotation() {
        return rotation;
    }

    
    

}
