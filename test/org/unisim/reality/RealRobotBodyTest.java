/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author miles
 */
public class RealRobotBodyTest {
    
    public RealRobotBodyTest() {
    }
    private static final Logger LOG
            = Logger.getLogger(RealRobotBodyTest.class.getName());
    
    
    
    @Mocked SerialCommunicator serial;
    RealRobotBody robot;
    
    @Before
    public void setUp() throws SerialPortException, SerialPortTimeoutException {
        
        robot = new RealRobotBody();
        LOG.setLevel(Level.ALL);
        LOG.addHandler(new ConsoleHandler());
    }

    @Test
    public void initialisationSetsPacketTypeTo3() throws SerialPortException, SerialPortTimeoutException {
        new Expectations() {{
           serial.sendCommand("#t3");
        }};
        robot = new RealRobotBody();
    }
    
    @Test
    public void readDataDoesNothingIfStartupOKReceived() throws SerialPortTimeoutException {
        new Expectations() {{
           serial.readLine(); result = "Starting...\n";
           serial.readLine(); result = "Startup OK!\n";
        }};
        robot.readData();
    }
    
    @Test
    public void readDataParsesToSonarIfSonarDataReceived() throws SerialPortTimeoutException {
        new Expectations() {{
           serial.readLine(); result = "#! 600.0 300.0 30.0 3.0";
        }};
        robot.readData();
        Assert.assertArrayEquals(new double[]{600.0,300.0,30.0,3.0},robot.getSonars(),0.001);
    }
    
    @Test
    public void readDataParsesToRangeIfRangeDataReceived() throws SerialPortTimeoutException {
        new Expectations() {{
           serial.readLine(); result = "#q 300.0";
        }};
        robot.readData();
        Assert.assertEquals(300.0,robot.getRange(),0.001);
    }
    
    @Test
    public void getRangeAndgetSonarsParseBoth() throws SerialPortTimeoutException  {
        new Expectations() {{
           serial.readLine(); times = 2; returns("#q 300.0","#! 600.0 300.0 30.0 3.0");
        }};
        Assert.assertEquals(300.0,robot.getRange(),0.001);
        Assert.assertArrayEquals(new double[]{600.0,300.0,30.0,3.0},robot.getSonars(),0.001);
    }
    
        
    @Test
    public void multipleGetRangesParsesTheValues() throws SerialPortTimeoutException {
        new Expectations() {{
           serial.readLine(); times = 2; returns("#q 300.0","#q 600.0");
        }};
        Assert.assertEquals(300.0,robot.getRange(),0.001);
        Assert.assertEquals(600.0,robot.getRange(),0.001);
    }
    
    
    @Test
    public void multipleGetSonarsParsesTheValues() throws SerialPortTimeoutException {
        new Expectations() {{
           serial.readLine(); times = 2; returns("#! 600.0 300.0 30.0 3.0","#! 200.0 100.0 40.0 6.0");
        }};
        Assert.assertArrayEquals(new double[]{600.0,300.0,30.0,3.0},robot.getSonars(),0.001);
        Assert.assertArrayEquals(new double[]{200.0,100.0,40.0,6.0},robot.getSonars(),0.001);
    }
}
