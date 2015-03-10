/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unisim.io;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author miles
 */
public final class DataFile {

    private final Collection<String> headers;
    private final Collection<Collection<Object>> data;
    private final Loggable loggable;
    public static DataFile fromLoggable(Loggable loggable) {
        return new DataFile(loggable,loggable.getHeaders());
    }

    private DataFile(Loggable loggable,Collection<String> headers) {
        this(loggable);
        addHeaders(headers);
    }

    private DataFile(Loggable loggable) {
        headers = new ArrayList<>();
        data = new ArrayList<>();
        this.loggable = loggable;
    }

    public void update() {
        addDataRow(loggable.getDataRow());
    }
    
    public void addHeaders(Collection<String> headers) {
        this.headers.addAll(headers);
    }

    public void addDataRow(List<Object> data) {
        this.data.add(data);
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
