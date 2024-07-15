package Persistence;

import java.io.IOException;

/**
 * Interface for managing persistence operations, such as reading from and writing to file.
 */
public interface PersistenceManager {

    /**
     * Opens a file for reading.
     *
     * @param datenquelle The file to be opened for reading.
     * @throws IOException If an I/O error occurs while opening the data source.
     */
    public void openForReading(String datenquelle) throws IOException;

    /**
     * Opens a file for writing.
     *
     * @param datenquelle The file to be opened for writing.
     * @throws IOException If an I/O error occurs while opening the data source.
     */
    public void openForWriting(String datenquelle) throws IOException;

    /**
     * Closes the currently open file.
     *
     * @return True if the data source was successfully closed, false otherwise.
     */
    public boolean close();
}
