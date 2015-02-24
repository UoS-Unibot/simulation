/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.ui.exprun;

import org.mb459.easy.premca.exp.ExpParam;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miles
 */
public class ExpParamEditor extends javax.swing.JPanel implements TableModelListener {
    public ExpParam params;
    
    @Override
    public void tableChanged(TableModelEvent e) {
        Object aValue = tblParams.getValueAt(e.getFirstRow(),1);
        String key = (String)tblParams.getValueAt(e.getFirstRow(), 0);
        if(aValue.getClass().equals(float.class))
                System.out.println("Float");
        else if(aValue.getClass().equals(int.class))
            System.out.println("Integer");
        else if(aValue.getClass().equals(boolean.class))
            System.out.println("Boolean");
        params.put(key, aValue);
    }
    
    
    public class ExpParamTableModel extends AbstractTableModel {
        
        public ExpParam params;
        
        
        
        public ExpParamTableModel(ExpParam params) {
            this.params = params;
            repaint();
        }

        @Override
        public String getColumnName(int column) {
            if(column == 1) {
                return "Value";
            } else if(column == 0)
                return "Param";
            else
                return "";
        }
        
        
        

        @Override
        public int getRowCount() {
            return params.size(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == 1)
                return params.get(params.keys.get(rowIndex));
            else if(columnIndex == 0)
                return params.keys.get(rowIndex);
            else
                return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            boolean isInt;
            Object rowVal = null;
            try {
                Integer.parseInt((String)aValue);
                isInt = true;
            } catch (NumberFormatException e) {
                isInt = false;
            }
            if(isInt)
                rowVal = Integer.parseInt((String)aValue);
            else {
                boolean isFloat;
                try {
                    Float.parseFloat((String)aValue);
                    isFloat = true;
                } catch (NumberFormatException e) {
                    isFloat = false;
                }
                if(isFloat)
                    rowVal = Float.parseFloat((String)aValue);
                else {
                    if("true".equalsIgnoreCase((String)aValue) | 
                            "false".equalsIgnoreCase((String)aValue)) {
                        rowVal = Boolean.parseBoolean((String)aValue);
                    }       
                }
            }
            params.put(this.getValueAt(rowIndex, 0), rowVal);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 0; //To change body of generated methods, choose Tools | Templates.
        }
        
        
        
    }

    public ExpParam getParams() {
//        ExpParam param = new ExpParam();
//        for(int i = 0; i < tblParams.getRowCount(); i++) {
//            Object rowVal = (tblParams.getValueAt(i, 1));
////            boolean isInt;
////            try {
////                Integer.parseInt(tblParams.getValueAt(i, 1));
////                isInt = true;
////            } catch (NumberFormatException e) {
////                isInt = false;
////            }
////            if(isInt)
////                rowVal = Integer.parseInt(tblParams.getValueAt(i, 1));
////            else {
////                boolean isFloat;
////                try {
////                    Float.parseFloat((String)tblParams.getValueAt(i, 1));
////                    isFloat = true;
////                } catch (NumberFormatException e) {
////                    isFloat = false;
////                }
////                if(isFloat)
////                    rowVal = Float.parseFloat((String)tblParams.getValueAt(i, 1));
////                else {
////                    if("true".equalsIgnoreCase((String)tblParams.getValueAt(i, 1)) | 
////                            "false".equalsIgnoreCase((String)tblParams.getValueAt(i, 1))) {
////                        rowVal = Boolean.parseBoolean((String)tblParams.getValueAt(i, 1));
////                    }       
////                }
////            }
//            param.put(tblParams.getValueAt(i, 0), rowVal);
//        }
        return ((ExpParamTableModel)tblParams.getModel()).params;
    }
    
    /**
     * Creates new form ExpParamEditor
     */
    public ExpParamEditor() {
        initComponents();
        
        ExpParamTableModel tm = new ExpParamTableModel(new ExpParam());
        tblParams.setModel(tm);
        tblParams.getColumnModel().getColumn(0).setPreferredWidth(230);
        tblParams.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblParams.getModel().addTableModelListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblParams = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton2.setText("Load...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Set to default...");

        jButton1.setText("Save...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tblParams.setAutoCreateRowSorter(true);
        tblParams.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblParams.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblParams);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveParamToFile();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadParamFromFile();
    }//GEN-LAST:event_jButton2ActionPerformed

    
    public void loadParamFromFile() {
        final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "\\params\\");
        int ret = fc.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) {
            FileInputStream fs;
            try {
                fs = new FileInputStream(fc.getSelectedFile());
                //tblParams.setModel(new ExpParamTableModel(new ExpParam(fs)));
            } catch(IOException e) {
                System.out.println("Error loading params: "); e.printStackTrace();
            }
        }
    }
        
        public void saveParamToFile() {
            final JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "\\params\\");
            int ret = fc.showSaveDialog(this);
            if(ret == JFileChooser.APPROVE_OPTION) {
                FileWriter w = null;
                try {
                    w = new FileWriter(fc.getSelectedFile());
                    //((ExpParamTableModel)tblParams.getModel()).params.saveToFile(w);
                    w.flush();
                    System.out.println(w.toString());
                } catch(IOException e) {
                    System.out.println("Error opening file...");e.printStackTrace();
                } finally {
                    if(w != null) {
                        try {
                            w.close();
                        } catch (IOException e) {
                            System.out.println("Error closing file..."); e.printStackTrace();
                        }
                    }
                }
            }
            
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblParams;
    // End of variables declaration//GEN-END:variables
}
