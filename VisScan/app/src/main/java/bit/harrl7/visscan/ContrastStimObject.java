package bit.harrl7.visscan;

import android.graphics.Point;

/**
 * Created by aberhjk1 on 2/06/2017.
 */

public class ContrastStimObject
{
    public int contrastValue;
    public boolean hit;

    public ContrastStimObject(int contrastValue, boolean hit)
    {
        this.contrastValue = contrastValue;
        this.hit = hit;
    }

    public String ToCsv()
    {
        return contrastValue + "," + hit  + "\r\n";
    }
}
