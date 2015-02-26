/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.su.easy.unisim.exp.Experiment;
import org.su.easy.unisim.exp.params.Parameters;
import org.su.easy.unisim.genesis.RobotGenotype;
import org.su.easy.unisim.genesis.RobotIndividual;
import org.su.easy.unisim.simulation.core.SimulationController;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNN;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;
import org.su.easy.unisim.simulation.robot.ctrnn.jsonIO.JSONCTRNNLayout;

/**
 *
 * @author mb459
 */
public class SimulationViewer extends JPanel {

    private TrialViewer cv;

    public SimulationViewer() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;

        cv = new TrialViewer();
        cv.setPreferredSize(new Dimension(800, 600));
        this.add(cv, gbc);
        
        gbc.gridy = 2;
        SandPitControls ctrl = new SandPitControls();
        this.add(ctrl, gbc);
        
        ctrl.addListener(new VisualiserListener() {

            @Override
            public void setRunning(boolean isRunning) {
                if(isRunning)
                    cv.start();
                else
                    cv.stop();
            }

            @Override
            public void restart() {
                cv.stop();
                loadRandomControllerWithDefaultLayout();
            }

            @Override
            public void speedChanged(double newSpeed) {
                cv.setDelay(newSpeed);
            }
        });
        
    }
    
    public void loadSimulation(Experiment exp,RobotGenotype geno) {
        SimulationController controller = new SimulationController.SimulationBuilder(new CTRNN(geno, exp.getParam())).setWorld(exp.getWorld()).build();
        cv.loadSimulation(controller);
    }
    
    public void loadRandomControllerWithDefaultLayout() {
        CTRNNLayout layout;
        RobotIndividual ind;
        try {
            layout = JSONCTRNNLayout.fromFile(new File(System.getProperty("user.dir") + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json")).toCTRNNLayout();
        } catch (IOException ex) {
            Logger.getLogger(SandPitCanvas.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Experiment exp = new Experiment();
        exp.setLayout(layout);
        exp.setParam(new Parameters());
        ind = new RobotIndividual(exp);
        SimulationController controller = new SimulationController.SimulationBuilder(new CTRNN(ind.getGenotype(), new Parameters())).setWorldSize(new Vector2D(10,10)).build();
        cv.loadSimulation(controller);
    }


}
