package bit.harrl7.visscan.Trial_Records;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Liam on 23-Apr-17.
 */

public class FlashStimTrial implements Serializable, Parcelable {
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



    protected FlashStimTrial(Parcel in) {
        pos = (Point) in.readValue(Point.class.getClassLoader());
        hit = in.readByte() != 0x00;
        wasShown = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pos);
        dest.writeByte((byte) (hit ? 0x01 : 0x00));
        dest.writeByte((byte) (wasShown ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlashStimTrial> CREATOR = new Parcelable.Creator<FlashStimTrial>() {
        @Override
        public FlashStimTrial createFromParcel(Parcel in) {
            return new FlashStimTrial(in);
        }

        @Override
        public FlashStimTrial[] newArray(int size) {
            return new FlashStimTrial[size];
        }
    };
}