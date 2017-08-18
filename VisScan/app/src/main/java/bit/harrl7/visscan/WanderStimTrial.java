package bit.harrl7.visscan;

import android.graphics.Point;

/**
 * Created by Liam on 02-May-17.
 */

public class WanderStimTrial
{
    public Point pos;
    public boolean hit;

    public WanderStimTrial(Point pos)
    {
        this.pos = pos;
        hit = false;
    }

    public String ToCsv()
    {
        return pos.x + "," + pos.y + "," + hit;
    }
}