/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.sandpit;

import org.evors.rs.ui.sandpit.SimulationViewer;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
/**
 *
 * @author mb459
 */
public class VisualiserTest extends JFrame {
    
    private SimulationViewer simViewer = new SimulationViewer();

    public VisualiserTest() throws HeadlessException, IOException {
        this.setLayout(new CardLayout());
        this.add(simViewer);
        this.pack();
        simViewer.loadRandomControllerWithDefaultLayout();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new VisualiserTest().setVisible(true);
                } catch (HeadlessException ex) {
                    Logger.getLogger(VisualiserTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(VisualiserTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
}
