/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.sandpit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import org.su.easy.unisim.robot.SimulatedUnibot;
import org.su.easy.unisim.sim.world.SimulationWorld;
import org.su.easy.unisim.util.Line;
import org.su.easy.unisim.util.Shape2D;

/**
 *
 * @author miles
 */
public class SandpitRenderer {
    private static final Stroke bstroke = new BasicStroke(0.1f);
    
    
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
                g2.fill(obj.toJava2DShape());
            }
        }
    }
    public static void drawRobot(Graphics2D g2,SimulatedUnibot robot) {
        g2.setColor(Color.GREEN);
        g2.fill(robot.getShape().toJava2DShape());
    }
    
    
    
    
}
