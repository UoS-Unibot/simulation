/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.ui.exprun;

/**
 *
 * @author Miles
 */
public interface TextOutput {
    public void addLine(String newLine);
    public void updateProgress(int newProgress);
}
