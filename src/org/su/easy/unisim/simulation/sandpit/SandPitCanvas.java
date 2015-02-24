/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.mb459.easy.premca.exp.ExpParam;
import org.mb459.easy.premca.genesis.RobotIndividual;
import org.su.easy.unisim.simulation.robot.IRobot;
import org.su.easy.unisim.simulation.robot.SimulatedUnibot;
import org.su.easy.unisim.simulation.core.SimulationController;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNN;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.su.easy.unisim.simulation.robot.ctrnn.jsonIO.JSONCTRNNLayout;
import org.su.easy.unisim.util.Shape2D;

/**
 * Renders the simulation.
 *
 * @author Miles
 */
public class SandPitCanvas extends Canvas implements Runnable {

    CTRNNLayout layout;

    RobotIndividual ind;

    SimulationController controller;

    public SandPitCanvas() {

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

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("restarting");
                    restart = true;
                    start();
                }

            }

            @Override
            public void keyReleased(KeyEvent ke) {

            }
        });
        
        start();
    }

    private BufferStrategy buffer;

    public void start() {
        try {
            layout = JSONCTRNNLayout.fromFile(new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json")).toCTRNNLayout();
        } catch (IOException ex) {
            Logger.getLogger(SandPitCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        ind = new RobotIndividual(layout, new ExpParam());
        controller = new SimulationController.SimulationBuilder(new CTRNN(ind.getGenotype(), new ExpParam()))
                .setWorldSize(new Vector2D(10,10))
                .addWorldObject(Shape2D.createRectangleFromCenter(new Vector2D(3, 3), new Vector2D(2,2), 0))
                .build();

        robot = controller.getRobot();
        world = controller.getWorld();

        path = new PathTracer(robot.getPosition());
        restart = false;
    }

    public void draw() {
        if (buffer == null) {
            return;
        }
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
        SandpitRenderer.drawRobot(g2, (SimulatedUnibot) robot);

        g2.setTransform(prevTrans);
    }

    public void render() {
        buffer.show();

    }

    public SandPitCamera getCamera() {
        return camera;
    }

    private IRobot robot;
    private PathTracer path;
    private SimulationWorld world;
    private final SandPitCamera camera = new SandPitCamera(Vector2D.ZERO, Vector2D.ZERO, 50);
    private final Grid grid = new Grid(camera);

    private void step() {
        controller.step();
        path.step(robot.getPosition());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread canvas = new Thread(this);
        canvas.start();
    }

    private final long DELAY = (long) (10);
    private boolean restart = false;

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
            if(restart)
                start();
            
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
