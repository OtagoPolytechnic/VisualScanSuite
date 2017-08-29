package bit.harrl7.visscan;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aberhjk1 on 28/08/2017.
 */

public class PlottableObject implements Parcelable
{
    public Point pos;
    public boolean hit;

    public PlottableObject(Point pos, boolean hit)
    {
        this.pos = pos;
        this.hit = hit;
    }

    protected PlottableObject(Parcel in) {
        pos = (Point) in.readValue(Point.class.getClassLoader());
        hit = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pos);
        dest.writeByte((byte) (hit ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<PlottableObject> CREATOR = new Creator<PlottableObject>() {
        @Override
        public PlottableObject createFromParcel(Parcel in) {
            return new PlottableObject(in);
        }

        @Override
        public PlottableObject[] newArray(int size) {
            return new PlottableObject[size];
        }
    };
}
