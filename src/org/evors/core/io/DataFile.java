package org.evors.core.io;

import com.google.common.base.Joiner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The DataFile stores data from a Loggable and can print it to an output stream
 * or save it to a CSV file. Pretty printing to an ASCII table for debugging
 * etc. is also supported.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 */
public final class DataFile {

    private final Collection<String> headers;
    private final Collection<Collection<Object>> data;
    private final Loggable loggable;

    /**
     * Create a new DataFile with the specified Loggable.
     *
     * @param loggable Loggable interface providing headers and data.
     * @return a new DataFile with headers set and no data added.
     */
    public static DataFile fromLoggable(Loggable loggable) {
        return new DataFile(loggable, loggable.getHeaders());
    }

    private DataFile(Loggable loggable, Collection<String> headers) {
        this(loggable);
        addHeaders(headers);
    }

    private DataFile(Loggable loggable) {
        headers = new ArrayList<>();
        data = new ArrayList<>();
        this.loggable = loggable;
    }

    /**
     * Adds a row of data by querying the Loggable for a data row.
     */
    public void update() {
        addDataRow(loggable.getDataRow());
    }

    /**
     * Adds headers to the end of the header list.
     *
     * @param headers String collection of headers to add.
     */
    public void addHeaders(Collection<String> headers) {
        this.headers.addAll(headers);
    }

    /**
     * Adds a row of data.
     *
     * @param data List of data to add. Must have the same length as the headers
     * or an IllegalArgumentException is thrown.
     */
    public void addDataRow(List<Object> data) {
        if (data.size() != headers.size()) {
            throw new IllegalArgumentException(String.format(
                    "Data provided must be same length as header row. Length "
                    + "of data provided: %d. Length of header row: %d.",
                    data.size(), headers.size()));
        }
        this.data.add(data);
    }

    /**
     * Saves this DataFile to a standard CSV file that can be read by any
     * capable CSV reading program. Lines end with \n and data is separated by
     * commas.
     *
     * @param file File to save CSV to. If it doesn't already exist, directories
     * and a new file will be created.
     * @throws FileNotFoundException if there was a problem with creating the
     * file.
     * @throws IOException if another error occurred with writing to the file.
     */
    public void saveToCSV(File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            file.mkdirs();
            file.createNewFile();
        }
        try (PrintWriter pw = new PrintWriter(file)) {
            Joiner headerJoiner = Joiner.on(",").useForNull("");
            Joiner dataJoiner = Joiner.on(",").useForNull("0");
            pw.println(headerJoiner.join(headers));
            for (Collection<Object> dataLine : data) {
                pw.println(dataJoiner.join(dataLine));
            }
        }
    }

    /**
     * Gets a copy of the headers.
     *
     * @return String collection of headers.
     */
    public Collection<String> getHeaders() {
        return new ArrayList<>(headers);
    }

    /**
     * Returns the number of data rows in the DataFile.
     *
     * @return number of data rows.
     */
    public int getNumberOfDataRows() {
        return data.size();
    }

    /**
     * Returns a copy of the data row at the specified index.
     *
     * @param index Index of data row. Must be below the value of
     * getNumberOfDataRows().
     * @return Object collection of the data row.
     */
    public Collection<Object> getDataRow(int index) {
        return new ArrayList<>(data).get(index);
    }

    /**
     * Prints the CSV to an OutputStream in standard CSV format that can be read
     * by any capable CSV reading program. Lines end with \n and data is
     * separated by commas.
     *
     * @param out OutputStream to write to. This method only writes to the
     * OutputStream, and is not responsible for opening and closing it.
     * @throws IOException if an error occurs writing to the OutputStream.
     */
    void printCSVToOutputStream(OutputStream out) throws IOException {
        Joiner headerJoiner = Joiner.on(",").useForNull("");
        Joiner dataJoiner = Joiner.on(",").useForNull("0");
        out.write((headerJoiner.join(headers) + "\n").getBytes());
        for (Collection<Object> dataLine : data) {
            out.write((dataJoiner.join(dataLine) + "\n").getBytes());
        }
    }

    /**
     * Pretty prints the DataFile in an ASCII table format with fixed column
     * widths. The implementation of this is liable to change, so <b>don't</b>
     * use this for parsing; use printCSVToOutputStream() instead.
     *
     * @param out PrintStream to print the DataFile to. Use System.out to print
     * to the console, or a custom PrintStream can be used. This method calls
     * .print() methods, so it actively modifies the PrintStream so flush()
     * doesn't need to be called.
     * @throws IOException if there is an error printing to the PrintStream.
     */
    public void prettyPrintCSVToOutputStream(PrintStream out) throws IOException {

        String cellFormatStr = "| %-15s ";
        char[] line = new char[headers.size() * 18 + 1];
        Arrays.fill(line, '-');
        String dividerLine = String.valueOf(line) + "\n";

        StringBuilder headerSB = new StringBuilder(dividerLine);
        for (String header : headers) {
            headerSB.append(String.format(cellFormatStr, header));
        }
        headerSB.append("|\n").append(dividerLine);
        String headerStr = headerSB.toString();
        out.print(headerStr);
        for (Collection<Object> dataLine : data) {
            for (Object dataCell : dataLine) {
                String d = String.valueOf(dataCell);
                out.printf(cellFormatStr, d);
            }
            out.println("|");
        }
        out.print(dividerLine);
    }

}
