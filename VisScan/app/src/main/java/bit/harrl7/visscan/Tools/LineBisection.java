package bit.harrl7.visscan.Tools;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import bit.harrl7.visscan.IVisualTest;

import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;

/**
 * Created by harrl7 on 17/10/2017.
 */

public class LineBisection extends Fragment implements IVisualTest
{
    final int ITEM_COUNT = 5;

    RelativeLayout[] outerContainers;
    RelativeLayout[] lineContainers;
    RelativeLayout[] hitContainers;

    View[] hitMarker;

    public LineBisection() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_linebisection, container, false);

        outerContainers = new RelativeLayout[ITEM_COUNT];
        hitContainers = new RelativeLayout[ITEM_COUNT];
        hitMarker = new View[ITEM_COUNT];
        lineContainers = new RelativeLayout[ITEM_COUNT];

        // Hit marker
        hitMarker[0] = v.findViewById(R.id.hit0);
        hitMarker[1] = v.findViewById(R.id.hit1);
        hitMarker[2] = v.findViewById(R.id.hit2);
        hitMarker[3] = v.findViewById(R.id.hit3);
        hitMarker[4] = v.findViewById(R.id.hit4);

        // Hit container
        hitContainers[0] = (RelativeLayout) v.findViewById(R.id.hitContainer0);
        hitContainers[1] = (RelativeLayout) v.findViewById(R.id.hitContainer1);
        hitContainers[2] = (RelativeLayout) v.findViewById(R.id.hitContainer2);
        hitContainers[3] = (RelativeLayout) v.findViewById(R.id.hitContainer3);
        hitContainers[4] = (RelativeLayout) v.findViewById(R.id.hitContainer4);


        // Line
        lineContainers[0] = (RelativeLayout) v.findViewById(R.id.lnContainer0);
        lineContainers[1] = (RelativeLayout) v.findViewById(R.id.lnContainer1);
        lineContainers[2] = (RelativeLayout) v.findViewById(R.id.lnContainer2);
        lineContainers[3] = (RelativeLayout) v.findViewById(R.id.lnContainer3);
        lineContainers[4] = (RelativeLayout) v.findViewById(R.id.lnContainer4);


        // Outer containers
        outerContainers[0] = (RelativeLayout) v.findViewById(R.id.outerContainer0);
        outerContainers[0].setOnTouchListener(new ClickLineHandler());

        outerContainers[1] = (RelativeLayout) v.findViewById(R.id.outerContainer1);
        outerContainers[1].setOnTouchListener(new ClickLineHandler());

        outerContainers[2] = (RelativeLayout) v.findViewById(R.id.outerContainer2);
        outerContainers[2].setOnTouchListener(new ClickLineHandler());

        outerContainers[3] = (RelativeLayout) v.findViewById(R.id.outerContainer3);
        outerContainers[3].setOnTouchListener(new ClickLineHandler());

        outerContainers[4] = (RelativeLayout) v.findViewById(R.id.outerContainer4);
        outerContainers[4].setOnTouchListener(new ClickLineHandler());
        reset();

        return v;
    }


    // === User input ===
    public class ClickLineHandler implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            //if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {

                // Find the Outer Container that was clicked
                for(int i = 0; i < ITEM_COUNT; i++)
                {
                    if(v.equals(outerContainers[i]))
                    {
                        if(event.getX() > 0)
                        {
                            // Update the hit marker to the position the user clicks
                            hitContainers[i].setPadding((int) event.getX(), hitContainers[i].getPaddingRight(), hitContainers[i].getPaddingTop(), hitContainers[i].getPaddingBottom());
                            hitMarker[i].setVisibility(View.VISIBLE);
                        }

                    }
                }

            }

            return true;
        }
    }


    // === Reset ===
    // Reset the position of the lines
    private void reset()
    {
        // Display bounds
        MainActivity activity = (MainActivity) getActivity();
        Point bounds = activity.GetBounds();

        double minLen = 0.3;
        int width = bounds.x;

        double leftPad =  1;
        double rightPad = 1;

        for(int i = 0; i < ITEM_COUNT; i++)
        {
            do {
                // Random padding, must leave room for minimum line length
                leftPad = Math.random();
                rightPad = Math.random();

            } while (leftPad + rightPad > 1 - minLen);


            // Absolute values for padding
            int leftPadAbsol = (int) (leftPad * width);
            int rightPadAbsol = (int) (rightPad * width);

            // Set padding on View

            lineContainers[i].setPadding(leftPadAbsol, 0, rightPadAbsol, 0);
        }

    }

    @Override
    public void Run()
    {

    }

    @Override
    public String ToCSV()
    {
        String output = "X1, X2, Width, Xcenter, Xuser, Error\r\n";

        for(int i = 0; i < ITEM_COUNT; i++)
        {
            int X1 = lineContainers[i].getPaddingLeft();
            int X2 = lineContainers[i].getWidth() - lineContainers[i].getPaddingRight();
            int width = X2 - X1;
            int Xcenter = (X1 + X2) / 2;
            int Xuser = hitContainers[i].getLeft();
            int error = Math.abs(Xuser - Xcenter);

            output += X1 + ", " + X2 + ", " + width + ", " + Xcenter + ", " + Xuser + ", " + error + "\r\n";
        }

        return output;
    }

    @Override
    public String GetTestType()
    {
        return "Line Bisection";

    }
}
