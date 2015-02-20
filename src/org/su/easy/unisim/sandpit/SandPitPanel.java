/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.sandpit;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

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
