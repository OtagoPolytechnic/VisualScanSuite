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
import java.util.Random;

import bit.harrl7.visscan.Trial_Records.FlashSizeTrial;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;


/**
 * A static stimulus will appear periodicaly, prompting the user for input
 */
public class FlashSizeFrag extends Fragment implements IVisualTest
{
    // Display
    ImageView ivFlashImage;
    FrameLayout stimContainer;
    TextView tv;
    Point bounds;
    int newSize;
    String size;

    // Stimuli
    FlashSizeTrial currentStim;
    ArrayList stimList;

    // Cycle control
    boolean isDrawCycle;
    double noShowRate;
    private int cycleDelay;
    int cycleTick;


    public FlashSizeFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_flash_size, container, false);

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds =  main.GetBounds();

        // Toggle for draw/ rest cycles
        isDrawCycle = true;

        // Stim record
        stimList = new ArrayList<>();

        // Screen controls
        ivFlashImage = (ImageView) v.findViewById(R.id.ivFlashSize);
        tv = (TextView) v.findViewById(R.id.textView1);
        stimContainer = (FrameLayout) v.findViewById(R.id.stimContainer1);

        // On click for full screen layout
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl2);
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
            ivFlashImage.setVisibility(View.INVISIBLE);
            if(currentStim != null)
            {
                currentStim.hit = true;
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
            // Set the width and length
            ivFlashImage.setVisibility(View.GONE);
            //Randomly generate image sizes between 196 and 296(inclusive)
            Random r= new Random();
             newSize =r.nextInt(296)+85;
            ivFlashImage.getLayoutParams().height = newSize;
            ivFlashImage.getLayoutParams().width = newSize;
            Categorise(newSize);
            if(currentStim != null) { tv.setText(currentStim.ToCsv()); }
        }
    }

    //Method thatreturns a size category
    public String Categorise(int newSize) {

        if (newSize < 149 && newSize >= 85) {
            size = "Small";
        }
        if (newSize < 297 && newSize >= 150) {
            size = "Medium";
        }
        if (newSize >= 298) {
            size = "Large";
        }

        return size;
    }






    // Move the stim and create a new record
    public void MoveStim()
    {
        // RNG for stim position and noShow trials
        int x = (int) (Math.random() * (bounds.x- ivFlashImage.getWidth()));
        int y = (int) (Math.random() * (bounds.y- ivFlashImage.getHeight()));
        //Standardizing screen dimensions out of 100 X and y.
        int xStandardized= ((100*x)/bounds.x);
        int yStandardized= ((100*y)/bounds.y);

        boolean showTrial = (Math.random() > noShowRate);


        // Set padding of stim container that is plus the radius of the circles.
        stimContainer.setPadding(x-newSize, y-newSize, 0, 0);

        // Create new stim as current stim
        currentStim = new FlashSizeTrial(new Point(xStandardized,yStandardized), showTrial, newSize,size );
        stimList.add(currentStim);

        // If show trial, show stim
        if(showTrial) { ivFlashImage.setVisibility(View.VISIBLE); }

    }

    // Return test results as CSV String
    public String ToCSV()
    {
        String s = "X, Y, Hit, Shown, Diameter,Size\r\n";

        for(Object item : stimList)
        {
            FlashSizeTrial stim = (FlashSizeTrial) item;
            s += stim.ToCsv() +"\r\n";
        }

        return s;
    }

    public String GetTestType()
    {
        return "FlashSize";
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