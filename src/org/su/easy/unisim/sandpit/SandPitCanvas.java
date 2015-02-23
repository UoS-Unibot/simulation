/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.sandpit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.robot.ctrnn.CTRNNRobotController;
import org.su.easy.unisim.robot.IRobot;
import org.su.easy.unisim.robot.SimulatedUnibot;
import org.su.easy.unisim.sim.SimulationController;
import org.su.easy.unisim.sim.world.SimulationWorld;

/**
 * Renders the simulation.
 * @author Miles
 */
public class SandPitCanvas extends Canvas implements Runnable {
    
    SimulationController controller = new SimulationController.SimulationBuilder(new CTRNNRobotController()).build();
    
    public SandPitCanvas() {
        robot = controller.getRobot();
        world = controller.getWorld();
        
        path = new PathTracer(robot.getPosition());
        robot.setVelocity(1);
        
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Vector2D prevCoord = Vector2D.NaN;
            @Override
            public void mouseDragged(MouseEvent me) {
                Vector2D newCoord = getCamera().convertScreenToWorldCoords(new Vector2D(me.getX(),me.getY()));
                if(prevCoord == Vector2D.NaN) {
                    prevCoord = newCoord;
                } else {
                    Vector2D sub = newCoord.subtract(prevCoord);
                getCamera().move(sub);
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                prevCoord = Vector2D.NaN;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) {
                getCamera().changeScale(-0.5 * mwe.getUnitsToScroll());
            }
            
            


        };
        
        
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }

    private BufferStrategy buffer;

    public void draw() {
        if(buffer == null)
            return;
        Graphics2D g2 = (Graphics2D) buffer.getDrawGraphics();
        camera.setWindowSize(new Vector2D(this.getWidth(), this.getHeight()));

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        AffineTransform prevTrans = g2.getTransform();
        g2.setTransform(camera.getTransform());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        grid.draw(g2);
        SandpitRenderer.drawWorld(g2, world);
        path.draw(g2);
        SandpitRenderer.drawRobot(g2, (SimulatedUnibot)robot);
        
        g2.setTransform(prevTrans);
    }

    public void render() {
        buffer.show();

    }
    
    public SandPitCamera getCamera() {
        return camera;
    }

    private final IRobot robot;
    private final PathTracer path;
    private final SimulationWorld world;
    private final SandPitCamera camera = new SandPitCamera(Vector2D.ZERO, Vector2D.ZERO, 50);
    private final Grid grid = new Grid(camera);


    private void step() {
        controller.step();
        path.step(robot.getPosition());
        //randomiseRobot();
    }
    
    private void randomiseRobot() {
        if(Math.random() < 0.16)
            robot.setAngularVelocity((float) (robot.getAngularVelocity() + ((Math.random()- 0.5) * 0.5 )));
        robot.setVelocity((float) (robot.getVelocity() + Math.random() *0.001));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread canvas = new Thread(this);
        canvas.start();
    }

    private final long DELAY = (long) (10);

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        createBufferStrategy(3);
        buffer = this.getBufferStrategy();
        draw();
        render();
        beforeTime = System.currentTimeMillis();
        setVisible(true);

        while (true) {
            step();
            draw();
            render();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }

            beforeTime = System.currentTimeMillis();
        }
    }

}
