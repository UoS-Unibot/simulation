/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui.sandpit;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.unisim.simulation.core.SimulationWorld;

/**
 * Renders the simulation.
 *
 * @author Miles
 */
public abstract class SandPitCanvas extends Canvas {

    public SandPitCanvas() {

        camera = new SandPitCamera(Vector2D.ZERO, Vector2D.ZERO, 50);
        grid = new Grid(camera);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Vector2D prevCoord = Vector2D.NaN;

            @Override
            public void mouseDragged(MouseEvent me) {
                Vector2D newCoord = getCamera().convertScreenToWorldCoords(new Vector2D(me.getX(), me.getY()));
                if (prevCoord == Vector2D.NaN) {
                    prevCoord = newCoord;
                } else {
                    Vector2D sub = newCoord.subtract(prevCoord);
                    getCamera().move(sub);
                    draw();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                prevCoord = Vector2D.NaN;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                getCamera().changeScale(-0.5 * mwe.getUnitsToScroll());
                draw();
            }

        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);

    }


    public abstract void draw();




    public SandPitCamera getCamera() {
        return camera;
    }

    protected SimulationWorld world;
    protected final SandPitCamera camera;
    protected final Grid grid;

}
