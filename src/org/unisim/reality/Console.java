/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.reality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 *
 * @author mb459
 */
public class Console {

    static Scanner scanner = new Scanner(System.in);
    static SerialCommunicator comm;

    public static void main(String[] args) throws SerialPortTimeoutException {

        comm = new SerialCommunicator();
        try {
            comm.openSerialPort();
            timeDataCollection();
        } catch (SerialPortException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static void doConsole() throws SerialPortTimeoutException, SerialPortException {
        try {

            String command = "";
            while (!command.equalsIgnoreCase("quit")) {
                System.out.print(">");
                command = scanner.nextLine();
                comm.sendCommand(command + "\r");
                System.out.println(comm.readLine());
            }
        } catch (SerialPortException ex) {
            System.out.println(ex.toString());
        } finally {
            comm.closeSerialPort();
            System.exit(0);
        }
    }


    static void timeDataCollection() throws SerialPortException, SerialPortTimeoutException {
        comm.sendCommand("#t3\r");
        comm.readLine();
        long totalStartTime = System.currentTimeMillis();
        
        long[] motors = new long[100];
        long[] ranges = new long[100];
        long[] sonars = new long[100];
        for (int i = 0; i < 100; i++) {
            long startTime = System.currentTimeMillis();
            comm.sendCommand("#d0\r");
            comm.readLine();
            motors[i] = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            comm.sendCommand("#q\r");
            comm.readLine();
            ranges[i] = System.currentTimeMillis() - startTime;
//            startTime = System.currentTimeMillis();
//            comm.sendCommand("#!3\r");
//            comm.readLine();
//            sonars[i] = System.currentTimeMillis() - startTime;
        }
        System.out.println("Sending 100 steps took " + String.valueOf(System.currentTimeMillis() - totalStartTime));
        System.out.println("Motors:" + Arrays.toString(motors));
        System.out.println("Ranges:" + Arrays.toString(ranges));
//        System.out.println("Sonars:" + Arrays.toString(sonars));
    }

}
