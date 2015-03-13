/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.sandpit;

import com.google.common.base.Splitter;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.evors.core.RunController;
import org.evors.rs.sim.robot.SimulatedRobotBody;
import org.evors.rs.sim.robot.ctrnn.CTRNN;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author miles
 */
public class TrialViewer extends SandPitCanvas implements Runnable {

    CTRNNLayout layout;
    RunController controller;
    private boolean simulationLoaded = false;
    private SimulatedRobotBody robot;
    private PathTracer path;
    private volatile long DELAY = Math.round(1000 / 60);
    private volatile boolean simulationStopped = false;
    private float time = 0;

    public boolean isSimulationLoaded() {
        return simulationLoaded;
    }

    public void loadSimulation(RunController controller) {
        this.controller = controller;
        robot = (SimulatedRobotBody) this.controller.getRobot();
        world = robot.getWorld();
        path = new PathTracer(robot.getPosition());
        simulationLoaded = true;
    }

    private void step() {
        controller.step();
        path.step(robot.getPosition());
        time += controller.getTimeStep();
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

    @Override
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
        while (!simulationStopped) {
            if (controller.isLive()) {
                step();
            }
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
                simulationStopped = true;
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

    public void drawText(Graphics2D g2, String text) {
        FontMetrics fm = g2.getFontMetrics();
        List<String> strings = Splitter.on("\n").splitToList(text);
        float x1 = 40, y = 40;
        int dx = 0;

        for (String s : strings) {
            int width = fm.stringWidth(s);
            if (width > dx) {
                dx = width;
            }
        }
        g2.setColor(new Color(0.6f, 0.6f, 0.6f, 0.6f));
        g2.fillRect((int) x1, (int) y - fm.getHeight(), dx + 5, fm.getHeight()
                * strings.size() + 5);
        g2.setColor(Color.red);
        for (String s : strings) {
            g2.drawString(s, 40, y);
            y += fm.getHeight();
        }

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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
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
        drawText(
                g2,
                String.format(
                        "Time: %.2f\nRobot position: {%.2f,%.2f}\nRobot heading: %.2f\nInputs:%s\nNeurons:%s",
                        time,
                        robot.getPosition().getX(),
                        robot.getPosition().getY(),
                        robot.getHeading(),
                        Arrays.toString(robot.getInput()),
                        Arrays.toString(((CTRNN) controller.getController()).
                                getNeurons())
                )
        );
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
