/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.core.util.geometry.Line;
import org.evors.core.util.geometry.Shape2D;
import org.evors.rs.sim.robot.SimulatedRobotBody;

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
    public static void drawRobot(Graphics2D g2,SimulatedRobotBody robot) {
        robot.render(g2);
    }
    
    
    
    
}
