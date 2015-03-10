/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evors.core.io;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author miles
 */
public class DataFileTest {
    
    public DataFileTest() {
    }

    DataFile df;
    final List<String> headers = Lists.newArrayList("Header1","Header2","Header3","Header4");
    final List<Float> dataRow = Lists.newArrayList(1f,2f,3f,4f);
    
    @Before
    public void setUp() {
        df = DataFile.fromLoggable(new Loggable<Float>() {

            @Override
            public List<String> getHeaders() {
                return headers;
            }

            @Override
            public List<Float> getDataRow() {
                return dataRow;
            }
        });
    }
    
    @Test
    public void dataFileStoresHeaderFromLoggable() {
        assertEquals(headers,df.getHeaders());
    }
    
    @Test
    public void dataFileStoresDataRowOnUpdate() {
        df.update();
        assertEquals(dataRow,df.getDataRow(0));
    }
    
    @Test
    public void printToOutputStreamPrintsCSV() throws IOException {
        df.update();
        df.printCSVToOutputStream(System.out);
        System.out.flush();
    }
    
    @Test
    public void prettyPrintToPrintStreamPrettyPrintsCSV() throws IOException {
        System.out.println("");
        System.out.println("Pretty printing...");
        System.out.println("");
        df.update();
        df.prettyPrintCSVToOutputStream(System.out);
        System.out.flush();
    }
    
    
}
