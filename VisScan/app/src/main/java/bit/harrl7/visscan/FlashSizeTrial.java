package bit.harrl7.visscan;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Liam on 23-Apr-17.
 * Adapted by AbdelRahman 26-Jul-17.
 */

public class FlashSizeTrial
{
    public Point pos;
    public boolean hit;
    private boolean wasShown;
    public int diameter;
    public String size;

    //FlashSizeTrial constructor
    public FlashSizeTrial(Point pos, boolean wasShown, int diameter, String size)
    {
        //Position of the image
        this.pos = pos;
        //Boolean to confirm image render to the screen
        this.wasShown = wasShown;
        //Diameter of the image for boundry control
        this.diameter=diameter;
        //Categorization of diameter
        this.size=size;
        hit = false;
    }

    //Writing information to the csv file
    public String ToCsv()
    {
        return pos.x + "," + pos.y + "," + hit + "," + wasShown+","+diameter+","+size;
    }
}