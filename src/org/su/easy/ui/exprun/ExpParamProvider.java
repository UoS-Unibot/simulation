/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.ui.exprun;

import org.su.easy.unisim.exp.ExpParam;
import org.su.easy.unisim.simulation.robot.ctrnn.CTRNNLayout;

/**
 *
 * @author Miles
 */
public interface ExpParamProvider {
    public ExpParam getParams();
    public CTRNNLayout getCTRNNLayout();
}
