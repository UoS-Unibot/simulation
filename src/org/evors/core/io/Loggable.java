package org.evors.core.io;

import java.util.List;

/**
 * The Loggable interface indicates that a class can log data. It is used in
 * tandem with a DataFile and provides the headers and data.
 *
 * @author Miles Bryant <mb459 at sussex.ac.uk>
 * @param <T> type of data provided by this Loggable.
 */
public interface Loggable<T> {

    /**
     * Returns a list of headers corresponding with the data provided.
     *
     * @return A String List containing the headers.
     */
    public List<String> getHeaders();

    /**
     * Returns a data row, typically pertaining to all the loggable data in a
     * class in a particular timestep. Is called by the associated
     * DataFile.update() method, which is typcially called every timestep.
     *
     * @return a List of data.
     */
    public List<T> getDataRow();
}
