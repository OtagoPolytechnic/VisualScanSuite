package bit.harrl7.visscan;

/**
 * Created by aberhjk1 on 5/06/2017.
 */

public class ResultsList
{
    String fileName;
    boolean chosen;

    public ResultsList(String fileName, boolean chosen)
    {
        this.fileName = fileName;
        this.chosen = chosen;
    }

    public ResultsList()
    {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }


}
