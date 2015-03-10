/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.sandpit;

import org.evors.rs.ui.sandpit.SandpitRenderer;
import org.evors.rs.ui.sandpit.SandPitCanvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.rs.sim.core.SimulationWorld;

/**
 *
 * @author miles
 */
public class WorldViewer extends SandPitCanvas {
    
    public void loadWorld(SimulationWorld world) {
        this.world = world;
    }

    @Override
    public void draw() {
        Graphics2D g2 = (Graphics2D) getGraphics();
        camera.setWindowSize(new Vector2D(this.getWidth(), this.getHeight()));
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());
        AffineTransform prevTrans = g2.getTransform();
        g2.setTransform(camera.getTransform());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grid.draw(g2);
        if (world != null) {
            SandpitRenderer.drawWorld(g2, world);
        }
        g2.setTransform(prevTrans);
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        draw();
    }
    
    
    
}
