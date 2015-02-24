/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.su.easy.ui.exprun;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.P;
import java.util.ArrayList;

/**
 *
 * @author Miles
 */
public class ExperimentHTML {

    public ExperimentHTML() {
        document.body.appendChild(initP);
    }
    private P initP = new P();
    private ArrayList<RunDiv> runs = new ArrayList<>();
    
    private Document document = new Document(DocumentType.HTMLStrict);
    
    public void addInitText(String text) {
        initP.appendText(text);
    }
    
    public void addEnd(String text) {
        P endP = new P();
        document.body.appendChild(endP);
        endP.appendText(text);        
    }
    
    public void addRun(String text) {
        runs.add(new RunDiv(text));
    }
    
    public void addGeneration(int runNo, String text) {
        runs.get(runNo).addGeneration(text);
    }
    
    public String getHTML() {
        return document.write();
    }
    
    private class RunDiv extends Div {

        public RunDiv(String text) {
            document.body.appendChild(this);
            this.appendText("<h2 style=\"margin-left:30px;\">" + text + "</h2>");
        }
        
        public void addGeneration(String text) {
            this.appendText("<div style=\"margin-left:50px;\">" + text + "</div>");
        }
    }
}
