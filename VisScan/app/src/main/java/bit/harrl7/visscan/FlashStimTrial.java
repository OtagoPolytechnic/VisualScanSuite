package bit.harrl7.visscan;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Liam on 23-Apr-17.
 */

public class FlashStimTrial
{
    public Point pos;
    public boolean hit;
    private boolean wasShown;

    public FlashStimTrial(Point pos, boolean wasShown)
    {
        this.pos = pos;
        this.wasShown = wasShown;

        hit = false;
    }

    public String ToCsv()
    {
        return pos.x + "," + pos.y + "," + hit + "," + wasShown;
    }
}