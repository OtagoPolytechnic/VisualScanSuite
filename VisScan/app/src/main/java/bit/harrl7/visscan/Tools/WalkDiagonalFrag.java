package bit.harrl7.visscan.Tools;


import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bit.harrl7.visscan.Enums.EFocusPoint;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.Trial_Records.PeripherialStimObject;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalkDiagonalFrag extends Fragment implements IVisualTest
{
    //public enum EFocusPoint2 { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

    //Focus Points
    TextView focusPointTL;
    TextView focusPointTR;
    TextView focusPointBL;
    TextView focusPointBR;

    TextView introText;
    EFocusPoint currentFocus;

    //Stimulus
    ImageView ivStim;
    //Stimulus Object
    PeripherialStimObject currentStim;

    //Frame for animation
    FrameLayout stimContainer;

    //Array to hold stimulus data
    ArrayList stimList;

    Point bounds;

    //Need to keep track of stim location and start positions
    Point stimLocation;
    Point jumpDistance;
    int rightLimit;
    int bottomLimit;
    int stimCount;

    //Only start activity once focus point is chosen
    boolean testStarted;

    // Cycle control
    boolean isDrawCycle;
    double noShowRate;
    private int cycleDelay;
    int cycleTick;

    MainActivity activity;

    public WalkDiagonalFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_peripherial_flash, container, false);

        // Screen bounds for stim placement
        activity = (MainActivity) getActivity();
        bounds = activity.GetBounds();

        focusPointTL = (TextView) v.findViewById(R.id.focusPointTL);
        focusPointTR = (TextView) v.findViewById(R.id.focusPointTR);
        focusPointBL = (TextView) v.findViewById(R.id.focusPointBL);
        focusPointBR = (TextView) v.findViewById(R.id.focusPointBR);
        focusPointTL.setTag(EFocusPoint.TOP_LEFT);
        focusPointTR.setTag(EFocusPoint.TOP_RIGHT);
        focusPointBL.setTag(EFocusPoint.BOTTOM_LEFT);
        focusPointBR.setTag(EFocusPoint.BOTTOM_RIGHT);
        introText = (TextView)v.findViewById(R.id.tvIntro);

        //set onClickListeners for focus points
        focusPointTL.setOnClickListener(new onFocusPointClickListener());
        focusPointTR.setOnClickListener(new onFocusPointClickListener());
        focusPointBL.setOnClickListener(new onFocusPointClickListener());
        focusPointBR.setOnClickListener(new onFocusPointClickListener());

        testStarted = false;

        currentFocus = EFocusPoint.TOP_LEFT;

        ivStim = (ImageView) v.findViewById(R.id.ivStimulus);
        ivStim.setVisibility(View.GONE);
        stimContainer = (FrameLayout) v.findViewById(R.id.stimContainer);

        stimLocation = new Point(0, 0);
        rightLimit = bounds.x - ivStim.getWidth();
        bottomLimit = bounds.y - ivStim.getHeight();
        jumpDistance = GetJumpDistance();
        stimCount = 0;

        // Stim record
        stimList = new ArrayList<>();

        // Cycle control
        int aSecond = (int) ((MainActivity) getActivity()).GetUpdateFreg();
        isDrawCycle = true;
        noShowRate = 0.05;
        cycleDelay = aSecond * 1;
        cycleTick = cycleDelay;

        return v;
    }

    private Point GetJumpDistance()
    {
        double xdJump = rightLimit * 0.1;
        int xJump = (int)xdJump;
        double ydJump = bottomLimit * 0.1;
        int yJump = (int) ydJump;
        Point jumpDistance = new Point(xJump, yJump);
        return jumpDistance;
    }

    //Method to hide ball when the screen is tapped
    public class onScreenTapListener implements  View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            // Hide stim and set hit to true
            ivStim.setVisibility(View.INVISIBLE);
            if(currentStim != null) { currentStim.setHit(true); }
        }
    }

    //Grabs focus point clicked and turns the rest off
    public class onFocusPointClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            stimContainer.setOnClickListener(new onScreenTapListener());
            HideFocusPoints();
            //Use the view passed in to find what was clicked
            TextView selectedTV = (TextView) v.findViewById(v.getId());
            selectedTV.setVisibility(View.VISIBLE);

            //Set the focus to what the user selected
            currentFocus = (EFocusPoint)selectedTV.getTag();
            //Get starting stim location
            switch(currentFocus)
            {
                case TOP_LEFT:
                    stimLocation = new Point(0,0);
                    break;
                case TOP_RIGHT:
                    stimLocation = new Point(rightLimit, 0);
                    break;
                case BOTTOM_LEFT:
                    stimLocation = new Point(0, bottomLimit);
                    break;
                case BOTTOM_RIGHT:
                    stimLocation = new Point(rightLimit, bottomLimit);
                    break;
            }

            introText.setVisibility(View.GONE);
            StartStimulus();
        }
    }



    //Makes the ball visable and allows it to start moving
    public void StartStimulus()
    {
        ivStim.setVisibility(View.VISIBLE);
        testStarted = true;
    }

    //Hides every focus point
    public void HideFocusPoints()
    {
        focusPointTL.setVisibility(View.GONE);
        focusPointTR.setVisibility(View.GONE);
        focusPointBL.setVisibility(View.GONE);
        focusPointBR.setVisibility(View.GONE);
    }

    @Override
    public void Run()
    {
        if(testStarted)     //Check to see if test has started
        {
            cycleTick++;

            if(cycleTick > cycleDelay)
            {
                cycleTick = 0;
                NextCycle();
            }
        }
    }

    // Start the next cycle, draw or rest
    public void NextCycle()
    {
        isDrawCycle = !isDrawCycle;

        if(isDrawCycle)
        {
            MoveStim();
        }
        else
        {
            // On rest cycle: hide stim, print previous stim record
            ivStim.setVisibility(View.GONE);
        }
    }

    // Move the stim and create a new record
    public void MoveStim()
    {
        //Move stimulus only the length of the screen
        if(stimCount < 10)
        {
            int x = 0;
            int y = 0;
            if(currentFocus == EFocusPoint.TOP_LEFT)
            {
                x  = stimLocation.x + jumpDistance.x;
                y = stimLocation.y + jumpDistance.y;
            }
            else if(currentFocus == EFocusPoint.TOP_RIGHT)
            {
                x  = stimLocation.x - jumpDistance.x;
                y = stimLocation.y + jumpDistance.y;
            }
            else if(currentFocus == EFocusPoint.BOTTOM_LEFT)
            {
                x  = stimLocation.x + jumpDistance.x;
                y = stimLocation.y - jumpDistance.y;
            }
            else
            {
                x  = stimLocation.x - jumpDistance.x;
                y = stimLocation.y - jumpDistance.y;
            }

            int xStandardized= ((100*x)/bounds.x);
            int yStandardized= ((100*y)/bounds.y);

            stimLocation = new Point(x, y);

            // Set padding of stim container to move stim
            stimContainer.setPadding(x, y, 0, 0);

            // Create new stim as current stim
            currentStim = new PeripherialStimObject(new Point(xStandardized,yStandardized), true, currentFocus.toString());
            stimList.add(currentStim);

            ivStim.setVisibility(View.VISIBLE);

            //increment stimCount
            stimCount++;
        }
        else
        {
            //TODO: Pause tool and open drawer options
            activity.OpenDrawer();
        }
    }

    @Override
    public String ToCSV()
    {
        String s = "X, Y, Hit, Shown, Focus Point,\r\n";

        for(Object item : stimList)
        {
            PeripherialStimObject stim = (PeripherialStimObject) item;
            s += stim.ToCsv() +"\r\n";
        }

        return s;
    }

    @Override
    public String GetTestType()
    {
        return "Peripheral";
    }
}
