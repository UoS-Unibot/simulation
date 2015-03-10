/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.rs.ui.utils;

import java.io.File;

/**
 *
 * @author miles
 */
public class UIUtils {
    public static File getUserDir() {
        File f = new File(System.getProperty("user.dir") + "/user/");
        if(f.isDirectory() && f.exists())
            return f;
        else
            return new File(System.getProperty("user.dir"));
    }
    
    public static File getDir(String subdir) {
        File f = new File(System.getProperty("user.dir") + "/" + subdir);
        if(f.isDirectory() && f.exists())
            return f;
        else
            return new File(System.getProperty("user.dir"));
    }
    
    public static File getUserDir(String subdir) {
        return getDir("user/" + subdir);
    }
}
