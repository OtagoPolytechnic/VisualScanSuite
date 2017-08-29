package bit.harrl7.visscan.Tools;


import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bit.harrl7.visscan.Trial_Records.FlashStimTrial;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;


/**
 * A static stimulus will appear periodicaly, prompting the user for input
 */
public class FlashStimFrag extends Fragment implements IVisualTest
{
    // Display
    ImageView ivFlashStim;
    FrameLayout stimContainer;
    TextView tv;
    Point bounds;

    // Stimuli
    FlashStimTrial currentStim;
    ArrayList stimList;

    // Cycle control
    boolean isDrawCycle;
    double noShowRate;
    private int cycleDelay;
    int cycleTick;


    public FlashStimFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flash_stim, container, false);

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds =  main.GetBounds();

        // Toggle for draw/ rest cycles
        isDrawCycle = true;

        // Stim record
        stimList = new ArrayList<>();

        // Screen controls
        ivFlashStim = (ImageView) v.findViewById(R.id.ivStim);
        tv = (TextView) v.findViewById(R.id.textView);
        stimContainer = (FrameLayout) v.findViewById(R.id.stimContainer);

        // On click for full screen layout
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
        rl.setOnClickListener(new ScreenCickHandler());

        // Cycle control
        int aSecond = (int) ((MainActivity) getActivity()).GetUpdateFreg();
        isDrawCycle = true;
        noShowRate = 0.05;
        cycleDelay = aSecond * 1;
        cycleTick = cycleDelay;


        return v;
    }

    @Override
    public void Run()
    {
        cycleTick++;

        if(cycleTick > cycleDelay)
        {
            cycleTick = 0;
            NextCycle();
        }
    }


    // Screen touch event
    public class ScreenCickHandler implements OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            // Hide stim and set hit to true
            ivFlashStim.setVisibility(View.INVISIBLE);
            if(currentStim != null) { currentStim.hit = true; }
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
            // Set the width and length
            ivFlashStim.setVisibility(View.GONE);
            if(currentStim != null) { tv.setText(currentStim.ToCsv()); }
        }
    }




    // Move the stim and create a new record
    public void MoveStim()
    {
        // RNG for stim position and noShow trials
        int x = (int) (Math.random() * (bounds.x-ivFlashStim.getWidth()));
        int y = (int) (Math.random() * (bounds.y-ivFlashStim.getHeight()));
        //Standardizing screen dimensions out of 100 X and y.
        int xStandardized= ((100*x)/bounds.x);
        int yStandardized= ((100*y)/bounds.y);
        boolean showTrial = (Math.random() > noShowRate);


        // Set padding of stim container to move stim
        stimContainer.setPadding(x, y, 0, 0);

        // Create new stim as current stim
        currentStim = new FlashStimTrial(new Point(xStandardized,yStandardized), showTrial);
        stimList.add(currentStim);

        // If show trial, show stim
        if(showTrial) { ivFlashStim.setVisibility(View.VISIBLE); }

    }

    // Return test results as CSV String
    public String ToCSV()
    {
        String s = "X, Y, Hit, Shown\r\n";

        for(Object item : stimList)
        {
            FlashStimTrial stim = (FlashStimTrial) item;
            s += stim.ToCsv() +"\r\n";
        }

        return s;
    }

    public String GetTestType()
    {
        return "Flash";
    }


}




/*
public class ScreenCickHandler implements View.OnClickListener
{

    @Override
    public void onClick(View view)
    {
        ivStim.setBackground(getResources().getDrawable(R.drawable.white_ball));

        // Hit stim if flipped
        if(flipped)
        {
            // Update position
            currentStim.pos = new Point(stimContainer.getPaddingLeft(), stimContainer.getPaddingTop());
            currentStim.hit = true;
        }

    }
}
*/