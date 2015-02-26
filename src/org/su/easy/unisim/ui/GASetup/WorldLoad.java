/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.ui.GASetup;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.su.easy.unisim.sim.world.json.JSONWorld;
import org.su.easy.unisim.simulation.core.SimulationWorld;
import org.su.easy.unisim.ui.UIUtils;

/**
 *
 * @author miles
 */
public class WorldLoad extends javax.swing.JPanel {

    /**
     * Creates new form WorldLoad
     */
    public WorldLoad() {
        initComponents();
    }
    
    SimulationWorld world;

    public SimulationWorld getWorld() {
        return world;
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fcController = new javax.swing.JFileChooser();
        worldViewer1 = new org.su.easy.unisim.simulation.sandpit.WorldViewer();

        fcController.setApproveButtonText("Open");
        fcController.setControlButtonsAreShown(false);
        fcController.setCurrentDirectory(UIUtils.getUserDir("/Worlds/"));
        fcController.setDialogTitle("");
        fcController.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fcControllerPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fcController, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(worldViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fcController, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(worldViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fcControllerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fcControllerPropertyChange
        try {
            if (fcController.getSelectedFile() != null) {
                SimulationWorld jw = JSONWorld.fromFile(fcController.getSelectedFile());
                worldViewer1.loadWorld(jw);
                world = jw;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_fcControllerPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fcController;
    private org.su.easy.unisim.simulation.sandpit.WorldViewer worldViewer1;
    // End of variables declaration//GEN-END:variables
}
