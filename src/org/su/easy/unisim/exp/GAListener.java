/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.exp;

import org.su.easy.unisim.genesis.Stats;

/**
 *
 * @author Miles
 */
public interface GAListener {
    public void GAupdateSummary(Stats stats);
    public void GAFinished();
}
