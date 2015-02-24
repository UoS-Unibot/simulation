/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import org.su.easy.unisim.simulation.robot.SimulatedUnibot;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.util.Line;
import org.su.easy.unisim.util.Shape2D;

/**
 *
 * @author miles
 */
public class SandpitRenderer {
    private static final Stroke bstroke = new BasicStroke(0.05f);
    
    
    public static void drawWorld(Graphics2D g2,SimulationWorld world) {
        for(Shape2D obj : world.getObjects()) {
            if(obj instanceof Line) {
                Line line = (Line)obj;
                g2.setColor(Color.BLACK);
                g2.setStroke(bstroke);
                g2.draw(line.toLine2D());
            } else {
                g2.setColor(Color.red);
                g2.setStroke(bstroke);
                //g2.draw(obj.toJava2DShape());
                g2.fill(obj.toJava2DShape());
            }
        }
    }
    public static void drawRobot(Graphics2D g2,SimulatedUnibot robot) {
        g2.setColor(new Color(45,45,45));
        //g2.draw(robot.getShape().toJava2DShape());
        g2.fill(robot.getShape().toJava2DShape());
        g2.setColor(Color.RED);
        g2.setStroke(bstroke);
        g2.draw(new Line2D.Double(
                robot.getRangeFinderBase().getX(),
                robot.getRangeFinderBase().getY(),
                robot.getRangeFinder().getLastIntersectionPoint().getX(),
                robot.getRangeFinder().getLastIntersectionPoint().getY()
        ));
    }
    
    
    
    
}
