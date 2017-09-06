package bit.harrl7.visscan.Tools;


import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * Signal a direction using an image
 * The user inputs a direction that matches the direction of the image
 *
 * Each round the img gets smaller
 * A fixed number of trials are performed each round
 */
public class DirectionalFragment extends Fragment  implements IVisualTest
{
    ImageView stim;

    // Trail logic control
    final int trialsPerRound = 5;
    int trialCount;

    // Image scale for each round
    final float[] scaleArray = { 1.5f, 1.0f, 0.8f, 0.5f, 0.2f };

    int[] hitsPerRound;
    int round;
    final int roundsPerTest = scaleArray.length;

    public DirectionalFragment() { } // Required empty public constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_directional, container, false);

        // Stimulus
        stim = (ImageView) v.findViewById(R.id.ivDirStim);

        // Control
        reset();

        // Buttons
        ImageButton btnUp       = (ImageButton) v.findViewById(R.id.btnUp);
        ImageButton btnDown     = (ImageButton) v.findViewById(R.id.btnDown);
        ImageButton btnLeft     = (ImageButton) v.findViewById(R.id.btnLeft);
        ImageButton btnRight    = (ImageButton) v.findViewById(R.id.btnRight);

        btnUp.setOnClickListener(new ClickHandler());
        btnDown.setOnClickListener(new ClickHandler());
        btnLeft.setOnClickListener(new ClickHandler());
        btnRight.setOnClickListener(new ClickHandler());
        
        return v;
    }


    // Button click
    public class ClickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            evaluateTrial(v.getId());
            runNextTrial();
        }
    }

    // Check user input against trial direction
    private void evaluateTrial(int btnId)
    {
        boolean correct = false;


        switch (btnId)
        {
            case R.id.btnUp:
                correct = stim.getRotation() == 90*0;
                break;
            case R.id.btnRight:
                correct = stim.getRotation() == 90*1;
                break;
            case R.id.btnDown:
                correct = stim.getRotation() == 90*2;
                break;
            case R.id.btnLeft:
                correct = stim.getRotation() == 90*3;
                break;
        }

        // Increment score
       if(correct) hitsPerRound[round]++;
    }

    //Rescale stimulus
    private void runNextTrial()
    {
        // Rotate stimulus
        int rotAngle = 90 * (int)Math.ceil(Math.random()*3);
        stim.setRotation((stim.getRotation()+rotAngle)%360);
        trialCount--;

        // Next round, rescale
        if(trialCount <= 0)
        {
            round++;
            trialCount = trialsPerRound;
        }

        // Rescale stim for next round
        if(round < roundsPerTest)
        {
            stim.setScaleX(scaleArray[round]);
            stim.setScaleY(scaleArray[round]);


        }
        else    // After final round, end test
        {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.OpenDrawer();
        }
    }

    // Initialise the stimulus
    private void reset()
    {
        stim.setScaleX(scaleArray[0]);
        stim.setScaleY(scaleArray[0]);

        trialCount = trialsPerRound;

        round = 0;
        hitsPerRound = new int[roundsPerTest];
    }





    @Override
    public void Run() { } // Empty run method as tool contains no timed logic


    @Override
    public String ToCSV()
    {
        String csv = "Scale, hit, total \r\n";

        for (int i=0; i<roundsPerTest; i++)
        {
            csv += scaleArray[i] + ", " + hitsPerRound[i] + ", " + trialsPerRound + "\r\n";
        }

        reset();
        return csv;
    }

    @Override
    public String GetTestType()
    {
        return "Directional";
    }
}
