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
    RelativeLayout[] outerContainers;
    RelativeLayout[] lineContainers;
    RelativeLayout[] hitContainers;


    View line;

    public LineBisection() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_linebisection, container, false);

        outerContainers = new RelativeLayout[1];
        hitContainers = new RelativeLayout[1];
        lineContainers = new RelativeLayout[1];


        hitContainers[0] = (RelativeLayout) v.findViewById(R.id.hitContainer0);

        lineContainers[0] = (RelativeLayout) v.findViewById(R.id.lnContainer0);


        outerContainers[0] = (RelativeLayout) v.findViewById(R.id.outerContainer0);
        outerContainers[0].setOnTouchListener(new ClickLineHandler());

        line = v.findViewById(R.id.hit0);

        reset();

        return v;
    }


    // === User input ===
    public class ClickLineHandler implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                reset();
                hitContainers[0].setLeft((int) event.getX());
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

        do
        {
            // Random padding, must leave room for minimum line length
            leftPad =  Math.random();
            rightPad =  Math.random();

        } while (leftPad + rightPad > 1-minLen);


        // Absolute values for padding
        int leftPadAbsol = (int) (leftPad * width);
        int rightPadAbsol = (int) (rightPad * width);

        // Set padding on View
        lineContainers[0].setPadding(leftPadAbsol, 0, rightPadAbsol, 0);

    }

    @Override
    public void Run()
    {

    }

    @Override
    public String ToCSV()
    {
        return null;
    }

    @Override
    public String GetTestType()
    {
        return null;
    }
}
