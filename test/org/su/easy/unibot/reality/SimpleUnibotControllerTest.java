/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.unibot.reality;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.su.easy.unibot.reality.SimpleUnibotController.DataPacket;

/**
 *
 * @author miles
 */
public class SimpleUnibotControllerTest {
    
    public SimpleUnibotControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void dataPacketShouldParseEncoderString() {
        String encoderStr = "# 3.0 3.0 3.0 3.0";
        double[] expected = new double[]{3.0,3.0,3.0,3.0};
        DataPacket dp = new SimpleUnibotController.DataPacket(SimpleUnibotController.PacketType.ENCODER, encoderStr);
        Assert.assertArrayEquals(expected,dp.values,0.001);
    }
    
}
