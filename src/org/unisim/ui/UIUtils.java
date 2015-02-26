/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.ui;

import java.io.File;

/**
 *
 * @author miles
 */
public class UIUtils {
    public static File getUserDir() {
        File f = new File(System.getProperty("user.dir") + "/user/");
        if(f.isDirectory() & f.exists())
            return f;
        else
            return new File(System.getProperty("user.dir"));
    }
    
    public static File getUserDir(String subdir) {
        File f = new File(System.getProperty("user.dir") + "/user/" + subdir);
        if(f.isDirectory() & f.exists())
            return f;
        else
            return new File(System.getProperty("user.dir"));
    }
}
