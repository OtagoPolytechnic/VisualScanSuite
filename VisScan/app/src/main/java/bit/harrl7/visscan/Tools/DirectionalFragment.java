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

import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionalFragment extends Fragment  implements IVisualTest
{
    ImageView stim;

    // Trail logic control
    final int trialsPerCycle = 5;
    int trialCount;
    float scale;

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
        trialCount = trialsPerCycle;
        scale = 1;

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

            evaluateTrail(v.getId());
            rescaleStimulus();
        }
    }

    private void evaluateTrail(int btnId)
    {
        boolean correct = false;

        float x = stim.getRotation();

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

       // Toast.makeText(getActivity(), ""+correct, Toast.LENGTH_SHORT).show();
    }

    //Rescale stimulus
    private void rescaleStimulus()
    {
        // Rotate stimulus
        int rotAngle = 90 * (int)Math.ceil(Math.random()*3);
        stim.setRotation((stim.getRotation()+rotAngle)%360);
        trialCount--;

        // Rescale after full set of trials
        if(trialCount <= 0)
        {
            scale -= 0.1;

            stim.setScaleX(scale);
            stim.setScaleY(scale);

            trialCount = trialsPerCycle;
        }

        // End
        if(scale < 0.2)
        {
            reset();

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.OpenDrawer();
        }
    }

    // Initialise the stimulus
    private void reset()
    {
        scale = 1;
        stim.setScaleX(scale);
        stim.setScaleY(scale);

        trialCount = trialsPerCycle;
    }








    @Override
    public void Run()
    {

    }

    @Override
    public String ToCSV()
    {
        return "X, Y, Z";
    }

    @Override
    public String GetTestType()
    {
        return "Directional";
    }
}
