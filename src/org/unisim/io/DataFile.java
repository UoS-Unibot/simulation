package org.unisim.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Not fully implemented; intended as storage for experimental etc. logs
 *
 * @author Miles Bryant (mb459 at sussex.ac.uk)
 */
public final class DataFile {

    public DataFile() {
    }

    public DataFile(String[] categoryTitles) {
        for (String title : categoryTitles) {
            addCategory(title);
        }
    }

    private final HashMap<String, ArrayList<Double>> data = new HashMap<>();

    public void addCategory(String categoryName) {
        if (data.containsKey(categoryName)) {
            throw new IllegalStateException("Data already contains category");
        }
        data.put(categoryName, new ArrayList<Double>());
    }

    public void removeCategory(String categoryName) {
        data.remove(categoryName);
    }

    public void addData(String categoryName, double dataPoint) {
        data.get(categoryName).add(dataPoint);
    }

}
