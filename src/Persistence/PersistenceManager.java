package Persistence;
import java.io.IOException;

public interface PersistenceManager {
    public void openForReading(String datenquelle) throws IOException;

    public void openForWriting(String datenquelle) throws IOException;

    public boolean close();




}
