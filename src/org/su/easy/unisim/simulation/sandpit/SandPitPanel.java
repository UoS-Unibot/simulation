/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.simulation.sandpit;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Miles
 */
public class SandPitPanel extends JPanel {

    public SandPitPanel() {
        this.setPreferredSize(new Dimension(300, 300));
        this.setLayout(new CardLayout());
        
        this.add(sandpitCanvas);
    }
    
    SandPitCanvas sandpitCanvas = new SandPitCanvas();
    Thread sandpitThread = new Thread(sandpitCanvas);
}
