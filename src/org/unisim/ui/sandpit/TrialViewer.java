/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui.sandpit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.unisim.genesis.RobotIndividual;
import org.unisim.reality.RunController;
import org.unisim.simulation.robot.SimulatedRobotBody;
import org.unisim.simulation.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author miles
 */
public class TrialViewer extends SandPitCanvas implements Runnable {
    CTRNNLayout layout;
    RobotIndividual ind;
    RunController controller;
    private boolean simulationLoaded = false;
    private SimulatedRobotBody robot;
    private PathTracer path;
    private volatile long DELAY = Math.round(1000 / 60);
    private volatile boolean simulationStopped = false;

    public boolean isSimulationLoaded() {
        return simulationLoaded;
    }

    public void loadSimulation(RunController controller) {
        this.controller = controller;
        robot = (SimulatedRobotBody)this.controller.getRobot();
        world = robot.getWorld();
        path = new PathTracer(robot.getPosition());
        simulationLoaded = true;
    }

    private void step() {
        controller.step();
        path.step(robot.getPosition());
        //randomiseRobot();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread canvas = new Thread(this);
        canvas.start();
    }

    public void setDelay(double newDelay) {
        DELAY = Math.round(1000 / (60 * newDelay));
    }

    public void run() {
        if (!simulationLoaded) {
            return;
        }
        postInitialise();
        long beforeTime;
        long timeDiff;
        long sleep;
        draw();
        render();
        beforeTime = System.nanoTime();
        setVisible(true);
        while (!simulationStopped) {
            step();
            draw();
            render();
            timeDiff = System.nanoTime() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
            beforeTime = System.nanoTime();
        }
        
    }

    public void postInitialise() {
        createBufferStrategy(3);
        buffer = getBufferStrategy();
    }
    
    protected BufferStrategy buffer;
    public void stop() {
        simulationStopped = true;
    }

    public void start() {
        simulationStopped = false;
        new Thread(this).start();
    }

    @Override
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
        if (world != null) {
            SandpitRenderer.drawWorld(g2, world);
        }
        if (path != null) {
            path.draw(g2);
        }
        if (robot != null) {
            SandpitRenderer.drawRobot(g2, robot);
        }
        g2.setTransform(prevTrans);
    }

    public void render() {
        if (buffer != null) {
            buffer.show();
        }
    }
    
    @Override
    public void paint(Graphics grphcs) {
        draw();
        render();
    }
}
