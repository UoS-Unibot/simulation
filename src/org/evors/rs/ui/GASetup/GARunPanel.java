/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.GASetup;

import java.awt.Container;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import org.evors.genesis.GAParameters;
import org.evors.rs.genesis.RobotExperiment;
import org.evors.rs.genesis.RobotGARunner;
import org.evors.rs.genesis.RobotPhenotype;
import org.evors.genesis.JSONPopulation;
import org.evors.rs.kjunior.SimulatedKJunior;
import org.evors.rs.sim.core.SimulationWorld;
import org.evors.rs.sim.robot.ctrnn.CTRNNLayout;
import org.evors.rs.ui.utils.TextOutput;
import org.evors.rs.ui.frames.PopulationViewer;

/**
 *
 * @author Miles
 */
public class GARunPanel extends javax.swing.JPanel implements TextOutput {

    /**
     * Creates new form RunExpPane
     */
    public GARunPanel() {
        initComponents();
        lblDir.setText(generateUniqueFilename());
        lblOutputSaved.setText("");
    }

    private RobotExperiment exp;
    private RobotGARunner gaRunner;

    public void setUpExperiment(GAParameters params, SimulationWorld world,
            CTRNNLayout layout) {
        exp = new RobotExperiment(layout, new SimulatedKJunior(world,
                1f/60f), world);
        btnStart.setEnabled(true);
        parameters = params;
    }

    private GAParameters parameters;
    
    private String generateUniqueFilename() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("user.dir"));
        sb.append("/user/experiments/");
        sb.append(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        sb.append("/");
        sb.append(new SimpleDateFormat("HH-mm-ss").format(new Date()));
        sb.append("/");
        return sb.toString();
    }

    Thread gaThread;

    private void startPressed() {
        if (btnStart.isSelected()) {
            gaRunner = new RobotGARunner(
                    new RobotPhenotype(exp), true,parameters);
            addLine(gaRunner.getProgressReportHeader());
            gaRunner.setListener(new RobotGARunner.GAListener() {
                @Override
                public void doUpdate() {
                    prgProgress.setValue(Math.round(gaRunner.getProgress()));
                    addLine(gaRunner.getProgressReportLine());
                }

                @Override
                public void finished() {
                    PopulationViewer popView = new PopulationViewer();
                    popView.loadJSONPopulation(exp, JSONPopulation.
                            fromPopulation(gaRunner.getPopulation()));
                    popView.setVisible(true);
                    Container parent = getParent();
                    while(parent.getClass() != JDesktopPane.class) {
                        parent = parent.getParent();
                    }
                    parent.add(popView);
                    try {
                        popView.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        Logger.getLogger(GARunPanel.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
            });
            gaThread = new Thread(gaRunner);
            gaThread.start();
            btnStart.setText("Pause");
            btnReset.setEnabled(true);
        } else {
            btnStart.setText("Start");
            gaRunner.setRunning(false);
            btnReset.setEnabled(false);
        }
    }

    private boolean outputWritten = false;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chkSaveReports = new javax.swing.JCheckBox();
        lblDir = new javax.swing.JTextField();
        btnChooseDir = new javax.swing.JButton();
        prgProgress = new javax.swing.JProgressBar();
        btnStart = new javax.swing.JToggleButton();
        btnReset = new javax.swing.JButton();
        spnNRuns = new javax.swing.JSpinner();
        spnNThreads = new javax.swing.JSpinner();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOut = new javax.swing.JTextPane();
        btnSaveOut = new javax.swing.JButton();
        lblOutputSaved = new javax.swing.JLabel();

        jLabel1.setText("Number of times to run");

        jLabel2.setText("Maximum number of threads");

        chkSaveReports.setText("Save reports and logs to folder");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, btnSaveOut, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), chkSaveReports, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        chkSaveReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSaveReportsActionPerformed(evt);
            }
        });

        btnChooseDir.setText("Choose");
        btnChooseDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseDirActionPerformed(evt);
            }
        });

        prgProgress.setMaximum(1000);

        btnStart.setText("Start");
        btnStart.setEnabled(false);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.setEnabled(false);
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        spnNRuns.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        spnNThreads.setModel(new javax.swing.SpinnerNumberModel(1, 1, 255, 1));

        txtOut.setEditable(false);
        txtOut.setFont(new java.awt.Font("Courier New", 0, 11)); // NOI18N
        jScrollPane1.setViewportView(txtOut);

        btnSaveOut.setText("Save Output");
        btnSaveOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveOutActionPerformed(evt);
            }
        });

        lblOutputSaved.setText("jLabel3");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(chkSaveReports)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseDir, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnNRuns)
                            .addComponent(spnNThreads))
                        .addGap(314, 314, 314))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(btnStart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSaveOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(lblOutputSaved, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane1)
                    .addComponent(prgProgress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSaveReports)
                    .addComponent(lblDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseDir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(spnNRuns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spnNThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStart)
                    .addComponent(btnReset)
                    .addComponent(btnSaveOut)
                    .addComponent(lblOutputSaved))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prgProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseDirActionPerformed
        final JFileChooser fc = new JFileChooser(new File(lblDir.getText()));
        int returnVal = fc.showOpenDialog(this);
    }//GEN-LAST:event_btnChooseDirActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        startPressed();
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        btnStart.doClick();
        gaRunner = null;
    }//GEN-LAST:event_btnResetActionPerformed

    private void chkSaveReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSaveReportsActionPerformed

    }//GEN-LAST:event_chkSaveReportsActionPerformed

    private void btnSaveOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveOutActionPerformed
        if (outputWritten) {
            try {
                new File(lblDir.getText()).mkdirs();
                File file = new File(lblDir.getText() + "explog.txt");
                PrintStream out = new PrintStream(file);
                out.print(txtOut.getText());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GARunPanel.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }//GEN-LAST:event_btnSaveOutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChooseDir;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSaveOut;
    private javax.swing.JToggleButton btnStart;
    private javax.swing.JCheckBox chkSaveReports;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblDir;
    private javax.swing.JLabel lblOutputSaved;
    private javax.swing.JProgressBar prgProgress;
    private javax.swing.JSpinner spnNRuns;
    private javax.swing.JSpinner spnNThreads;
    private javax.swing.JTextPane txtOut;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addLine(String newLine) {
        txtOut.setText(newLine.concat(txtOut.getText()));
    }

    @Override
    public void updateProgress(int newProgress) {
        prgProgress.setValue(newProgress);
    }
}
