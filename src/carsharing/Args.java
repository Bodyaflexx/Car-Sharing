package carsharing;
import com.beust.jcommander.Parameter;


public class Args {
    @Parameter(names = "-databaseFileName")
    private String databaseFileName;

    public String getDatabaseFileName() {
        return databaseFileName;
    }
}
