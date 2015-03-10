/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui;

import javax.swing.table.AbstractTableModel;
import org.unisim.io.pop.JSONPopulation;

/**
 *
 * @author miles
 */
public class PopulationTM extends AbstractTableModel {

    public PopulationTM(JSONPopulation pop) {
        this.pop = pop;
    }

    public PopulationTM() {
    }

    public JSONPopulation getPop() {
        return pop;
    }

    public void setPop(JSONPopulation pop) {
        this.pop = pop;
    }

    JSONPopulation pop;

    @Override
    public int getRowCount() {
        if (pop == null) {
            return 0;
        } else {
            return pop.getIndividuals().length;
        }
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return row;
        } else if (col == 1) {
            return pop.getIndividuals()[row].getFitness();
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
