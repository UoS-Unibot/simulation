/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.sandpit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.evors.rs.genesis.RobotExperiment;
import org.evors.genesis.Genotype;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;
import org.evors.rs.sim.robot.ctrnn.io.JSONCTRNNLayout;
import org.evors.core.RunController;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.core.SimulationBuilder;

/**
 *
 * @author mb459
 */
public class SimulationViewer extends JPanel {

    private TrialViewer cv;
    private RobotExperiment exp;
    private Genotype geno;

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
                if (isRunning) {
                    cv.start();
                } else {
                    cv.stop();
                }
            }

            @Override
            public void restart() {
                cv.stop();
                loadSimulation(exp, geno);
            }

            @Override
            public void speedChanged(double newSpeed) {
                cv.setDelay(newSpeed);
            }
        });

    }

    public void loadSimulation(RobotExperiment exp, Genotype geno) {
        this.exp = exp;
        this.geno = geno;
        RunController controller = new SimulationBuilder(
                exp.getLayout().getCTRNNController(geno.getGenes()),
                new SimulatedKJunior(exp.getWorld(), 1f/60f)
        ).setWorld(exp.getWorld()).build();
        cv.loadSimulation(controller);
    }

    public void loadRandomControllerWithDefaultLayout() throws IOException {
        CTRNNLayout layout;

        try {
            layout = JSONCTRNNLayout.fromFile(new File(System.getProperty(
                    "user.dir")
                    + "/user/CTRNN Layouts/Simple5Neuron3LayerController.json")).
                    toCTRNNLayout();
        } catch (IOException ex) {
            Logger.getLogger(SandPitCanvas.class.getName()).log(Level.SEVERE,
                    null, ex);
            return;
        }

        this.geno = Genotype.withRandomGenome(layout.getGenotypeLength());
        this.exp = RobotExperiment.fromDirectory(System.getProperty(
                "user.dir") + "/demo/");
        loadSimulation(exp, geno);
    }

}
