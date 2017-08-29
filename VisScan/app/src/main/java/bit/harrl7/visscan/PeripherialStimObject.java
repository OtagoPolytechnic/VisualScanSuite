package bit.harrl7.visscan;

import android.graphics.Point;

/**
 * Created by tuckmn1 on 29/05/2017.
 */

public class PeripherialStimObject
{
    public Point pos;
    public String focusPoint;
    public boolean hit;
    private boolean wasShown;

    public PeripherialStimObject(Point pos, boolean wasShown, String focusPoint)
    {
        this.pos = pos;
        this.wasShown = wasShown;
        this.focusPoint = focusPoint;
        hit = false;
    }

    public void setHit(boolean hit)
    {
        this.hit = hit;
    }

    public String ToCsv()
    {
        return pos.x + "," + pos.y + "," + hit + "," + wasShown +"," + focusPoint;
    }
}
