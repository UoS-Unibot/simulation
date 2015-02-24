/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.genesis.RobotIndividual;
import org.su.easy.unisim.simulation.robot.IRobot;
import org.su.easy.unisim.simulation.robot.SimulatedUnibot;
import org.su.easy.unisim.simulation.core.SimulationController;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;

/**
 * Renders the simulation.
 *
 * @author Miles
 */
public class SandPitCanvas extends Canvas implements Runnable {

    CTRNNLayout layout;

    RobotIndividual ind;

    SimulationController controller;
    private boolean simulationLoaded = false;

    public boolean isSimulationLoaded() {
        return simulationLoaded;
    }

    public void loadSimulation(SimulationController controller) {
        this.controller = controller;
        robot = this.controller.getRobot();
        world = this.controller.getWorld();
        path = new PathTracer(robot.getPosition());
        simulationLoaded = true;
    }
    

    public SandPitCanvas() {

//        try {
//            layout = JSONCTRNNLayout.fromFile(new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json")).toCTRNNLayout();
//        } catch (IOException ex) {
//            Logger.getLogger(SandPitCanvas.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        ind = new RobotIndividual(layout, new ExpParam());
//        controller = new SimulationController.SimulationBuilder(new CTRNN(ind.getGenotype(), new ExpParam())).build();
        setPreferredSize(new Dimension(800, 600));

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
                    render();
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
                render();
            }

        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);

    }

    public void initialise() {
        createBufferStrategy(3);
        buffer = this.getBufferStrategy();
        draw();
    }

    private BufferStrategy buffer;

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
            SandpitRenderer.drawRobot(g2, (SimulatedUnibot) robot);
        }

        g2.setTransform(prevTrans);
    }

    public void render() {
        buffer.show();

    }

    @Override
    public void paint(Graphics grphcs) {
        draw();
        render();
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
        //randomiseRobot();
    }


    @Override
    public void addNotify() {
        super.addNotify();
        Thread canvas = new Thread(this);
        canvas.start();
    }
    
    public void setDelay(double newDelay) {
        DELAY = Math.round(1000/(60 * newDelay));
    }

    private volatile long DELAY = Math.round(1000/60);
    private volatile boolean simulationStopped = false;

    @Override
    public void run() {
        if (!simulationLoaded) {
            return;
        }

        
        
        long beforeTime, timeDiff, sleep;

        draw();
        render();
        beforeTime = System.currentTimeMillis();
        setVisible(true);

        while (!simulationStopped) {
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
    
    public void stop() {
        simulationStopped = true;
    }
    
    public void start() {
        simulationStopped = false;
        new Thread(this).start();
    }

}
