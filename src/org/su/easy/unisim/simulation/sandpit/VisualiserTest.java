/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.CardLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
/**
 *
 * @author mb459
 */
public class VisualiserTest extends JFrame {
    
    private SimulationViewer simViewer = new SimulationViewer();

    public VisualiserTest() throws HeadlessException {
        this.setLayout(new CardLayout());
        this.add(simViewer);
        this.pack();
        simViewer.loadRandomControllerWithDefaultLayout();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VisualiserTest().setVisible(true);
            }
        });
    }
    
}
