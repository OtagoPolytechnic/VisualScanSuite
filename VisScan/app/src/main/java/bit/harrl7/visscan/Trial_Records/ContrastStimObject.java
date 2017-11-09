package bit.harrl7.visscan.Trial_Records;

import android.graphics.Point;

/**
 * Created by aberhjk1 on 2/06/2017.
 */

public class ContrastStimObject
{
    public int contrastValue;
    public boolean hit;
    public Point pos;

    public ContrastStimObject(Point pos, boolean hit, int contrastValue)
    {
        this.contrastValue = contrastValue;
        this.hit = hit;
        this.pos = pos;
    }

    public String ToCsv()
    {
        return contrastValue + "," + hit  + "\r\n";
    }
}
