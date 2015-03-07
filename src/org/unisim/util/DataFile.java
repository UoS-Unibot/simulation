/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.util;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author miles
 */
public final class DataFile {

    private final Collection<String> headers;
    private final Collection<Collection<Object>> data;

    public DataFile(String...headers) {
        this();
        addHeaders(headers);
    }
    
    

    public DataFile() {
        headers = new ArrayList<>();
        data = new ArrayList<>();
    }

    public void addHeaders(String... headers) {
        this.headers.addAll(Arrays.asList(headers));
    }

    public void addDataRow(Object... data) {
        this.data.add(Arrays.asList(data));
    }

    public void saveToCSV(File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            file.mkdirs();
            file.createNewFile();
        }
        PrintWriter pw = new PrintWriter(file);
        Joiner headerJoiner = Joiner.on(",").useForNull("");
        Joiner dataJoiner = Joiner.on(",").useForNull("0");
        pw.println(headerJoiner.join(headers));
        for(Collection<Object> dataLine : data) {
            pw.println(dataJoiner.join(dataLine));
        }
        pw.close();
    }

}
