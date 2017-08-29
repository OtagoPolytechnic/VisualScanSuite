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


public class PeripherialFlashFrag extends Fragment implements IVisualTest
{

    //public enum EFocusPoint { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

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

    //Only start activity once focus point is chosen
    boolean testStarted;

    // Cycle control
    boolean isDrawCycle;
    double noShowRate;
    private int cycleDelay;
    int cycleTick;

    public PeripherialFlashFrag()
    {
        //Empty Constructor Required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_peripherial_flash, container, false);

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds = main.GetBounds();

        focusPointTL = (TextView) v.findViewById(R.id.focusPointTL);
        focusPointTR = (TextView) v.findViewById(R.id.focusPointTR);
        focusPointBL = (TextView) v.findViewById(R.id.focusPointBL);
        focusPointBR = (TextView) v.findViewById(R.id.focusPointBR);
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



        // Stim record
        stimList = new ArrayList<>();

        // Cycle control
        int aSecond = (int) ((MainActivity) getActivity()).GetUpdateFreg();
        isDrawCycle = true;
        noShowRate = 0.05;
        cycleDelay = aSecond * 2;
        cycleTick = cycleDelay;

        return v;
    }

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

    public class onFocusPointClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            stimContainer.setOnClickListener(new onScreenTapListener());
            HideFocusPoints();
            TextView selectedTV = (TextView) v.findViewById(v.getId());
            selectedTV.setVisibility(View.VISIBLE);

            introText.setVisibility(View.GONE);
            StartStimulus();
        }
    }

    public void StartStimulus()
    {
        ivStim.setVisibility(View.VISIBLE);
        testStarted = true;
    }

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
            //if(currentStim != null) { tv.setText(currentStim.ToCsv()); }
        }
    }

    // Move the stim and create a new record
    public void MoveStim()
    {
        // RNG for stim position and noShow trials
        int x = (int) (Math.random() * (bounds.x-ivStim.getWidth()));
        int y = (int) (Math.random() * (bounds.y-ivStim.getHeight()));
        //Standardizing screen dimensions out of 100 X and y.
        int xStandardized= ((100*x)/bounds.x);
        int yStandardized= ((100*y)/bounds.y);

        boolean showTrial = (Math.random() > noShowRate);


        // Set padding of stim container to move stim
        stimContainer.setPadding(x, y, 0, 0);

        // Create new stim as current stim
        currentStim = new PeripherialStimObject(new Point(xStandardized,yStandardized), showTrial, currentFocus.toString());
        stimList.add(currentStim);

        // If show trial, show stim
        if(showTrial) { ivStim.setVisibility(View.VISIBLE); }

    }

    @Override
    public String ToCSV()
    {
        String s = "Focus Point, X, Y, Hit, Shown\r\n";

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
        return "Peripherial";
    }
}
