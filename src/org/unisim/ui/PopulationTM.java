/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui;

import org.unisim.io.CSVPopulation;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author miles
 */
public class PopulationTM extends AbstractTableModel {

    public PopulationTM(CSVPopulation pop) {
        this.pop = pop;
    }

    public PopulationTM() {
    }

    public CSVPopulation getPop() {
        return pop;
    }

    public void setPop(CSVPopulation pop) {
        this.pop = pop;
    }

    CSVPopulation pop;

    @Override
    public int getRowCount() {
        if (pop == null) {
            return 0;
        } else {
            return pop.getSize();
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return pop.getIDAt(row);
        } else if (col == 1) {
            return pop.getFitnessAt(row);
        }
        return null;
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "ID";
        } else {
            return "Fitness";
        }
    }

}
