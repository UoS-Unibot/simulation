/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unisim.robot.ctrnn;

import org.su.easy.unisim.robot.IRobotController;
import org.su.easy.unisim.robot.RobotInput;


public class CTRNNRobotController implements IRobotController {

    @Override
    public void step(RobotInput input) {
        
    }

    @Override
    public float getVelocity() {
        return 0.7f;
    }

    @Override
    public float getAngularVelocity() {
        return 0.2f;
    }
    
}
