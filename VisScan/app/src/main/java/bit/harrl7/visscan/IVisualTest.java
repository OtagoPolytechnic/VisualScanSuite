package bit.harrl7.visscan;

/**
 * Created by Liam on 27-Apr-17.
 */

public interface IVisualTest
{
    // Main logic loop
    public void Run();

    // Output test results as CSV string
    public String ToCSV();

    // Return test type
    public  String GetTestType();
}
