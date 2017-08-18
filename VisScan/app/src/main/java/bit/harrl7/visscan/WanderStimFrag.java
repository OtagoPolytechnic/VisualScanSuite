package bit.harrl7.visscan;


import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A single stimulus wanders around the screen. The stim will periodically shift to different color, prompting the user for input
 */
public class WanderStimFrag extends Fragment implements IVisualTest
{

    ImageView ivStim;
    FrameLayout stimContainer;
    Point bounds;
    ArrayList stimList;

    private int turnDelay = 120;
    private int turnTick = turnDelay;
    private  int velMin = -4;
    private  int velRange = 8;

    private int flipDelay = 100;
    private  int unflipDelay = 50;
    private int flipTick = flipDelay;
    private boolean flipped = false;

    Point pos;
    Point vel;

    private WanderStimTrial currentStim;


    public WanderStimFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wander_stim, container, false);

        pos = new Point(0, 0);
        vel = new Point(5, 0);

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds = main.GetBounds();

        // Stim record
        stimList = new ArrayList<>();

        // Screen controls
        ivStim = (ImageView) v.findViewById(R.id.ivStim);
        stimContainer = (FrameLayout) v.findViewById(R.id.stimContainer);

        // On click for full screen layout
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
        rl.setOnClickListener(new ScreenCickHandler());

        return v;
    }

    @Override
    public void Run()
    {
        move();
        turn();
        flip();
    }

    private void turn()
    {
        turnTick++;

        if(turnTick > turnDelay)
        {
            turnTick = 0;

            // Don't let stim stop moving
            do
            {
                vel.x = (int) (velMin + (Math.random() * velRange));
                vel.y = (int) (velMin + (Math.random() * velRange));
            } while (vel.x * vel.y == 0);
        }
    }


    private void move()
    {
        // Bounce
        if(pos.x+ivStim.getWidth() > bounds.x || pos.x < 0) vel.x *= -1;
        if(pos.y+ivStim.getHeight() > bounds.y || pos.y < 0) vel.y *= -1;

        // Move
        pos.x += vel.x;
        pos.y += vel.y;

        stimContainer.setPadding(pos.x, pos.y, 0, 0);
    }

    // Change stim color
    private void flip()
    {
        flipTick++;

        if(!flipped)
        {
            // Flip the stim
            if(flipTick > flipDelay)
            {
                flipTick = 0;
                flipped = true;

                // Create new stim as current stim
                currentStim = new WanderStimTrial(new Point(stimContainer.getPaddingLeft(), stimContainer.getPaddingTop()));

                // Change image to green stim
                ivStim.setBackground(getResources().getDrawable(R.drawable.green_ball));
            }
        }
        else
        {
            // Unflip the stim
            if(flipTick > unflipDelay)
            {
                flipTick = 0;
                flipped = false;

                // Add previous stim trial to the list
                if(currentStim != null) stimList.add(currentStim);

                // Change image to white stim
                ivStim.setBackground(getResources().getDrawable(R.drawable.white_ball));
            }
        }

    }

    // Screen touch event
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


    // Return test results as CSV String
    public String ToCSV()
    {
        String s = "X, Y, Hit\r\n";

        for(Object item : stimList)
        {
            WanderStimTrial stim = (WanderStimTrial) item;
            s += stim.ToCsv() +"\r\n";
        }

        return s;
    }


    public String GetTestType()
    {
        return "Wander";
    }
}
