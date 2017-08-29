package bit.harrl7.visscan.Tools;


import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;
import bit.harrl7.visscan.Trial_Records.WanderStimTrial;


/**
 * A single stimulus wanders around the screen. The stim will periodically shift to different color, prompting the user for input
 */
public class ZigZagFrag extends Fragment implements IVisualTest
{
    MainActivity activity;
    ImageView ivStim;
    FrameLayout stimContainer;
    Point bounds;

    Boolean testRunning;

    // Pathing
    Point[] wayPoints;
    int wayPointIndex;

    private int turnDelay;
    private int turnTick;

    Point pos;
    Point vel;

    // Recording pauses
    List<Point> pauseList;
    boolean paused;

    private WanderStimTrial currentStim;


    public ZigZagFrag()
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
        activity = (MainActivity) getActivity();
        bounds = activity.GetBounds();

        // pause record
        pauseList = new ArrayList<>();
        paused = true;

        // Screen controls
        ivStim = (ImageView) v.findViewById(R.id.ivStim);
        stimContainer = (FrameLayout) v.findViewById(R.id.stimContainer);

        // On click for full screen layout
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl);
        rl.setOnClickListener(new ScreenCickHandler());

        // Turn every second
        turnDelay = (int) (activity.GetUpdateFreg() * 1);
        turnTick = turnDelay;

        setWayPoints();

        return v;
    }

    @Override
    public void Run()
    {
        if(testRunning && !paused)
        {
            move();
            turn();
        }
    }

    private void turn()
    {
        turnTick++;

        if(turnTick > turnDelay)
        {
            turnTick = 0;

            wayPointIndex++;

            // Set next waypoint
            if(wayPointIndex < wayPoints.length)
            {
                vel.x = (int) ((wayPoints[wayPointIndex].x - wayPoints[wayPointIndex-1].x)/turnDelay);
                vel.y = (int) (wayPoints[wayPointIndex].y - wayPoints[wayPointIndex-1].y)/turnDelay;
            }
            else // Finish test
            {
                finishTest();
            }



        }
    }


    private void move()
    {
        // Bounce
       //if(pos.x+ivStim.getWidth() > bounds.x || pos.x < 0) vel.x *= -1;
        //if(pos.y+ivStim.getHeight() > bounds.y || pos.y < 0) vel.y *= -1;

        // Move
        pos.x += vel.x;
        pos.y += vel.y;

        stimContainer.setPadding(pos.x, pos.y, 0, 0);
    }


    // Screen touch event
    public class ScreenCickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            // Record pause
            if(!paused) pauseList.add(new Point(pos.x, pos.y));

            // Toggle pause
            paused = !paused;
        }
    }


    // Set the waypoints of the ZigZag path
    private void setWayPoints()
    {
        // Pathing variables
        int x1 = (int) (bounds.x * 0.1);
        int x2 = (int) (bounds.x * 0.45);
        int x3 = (int) (bounds.x * 0.55);
        int x4 = (int) (bounds.x * 0.9);

        int yBase = (int) (bounds.y * 0.05);
        int yMargin = (int) (bounds.y * 0.1);

        // Set waypoints, array length must divide evenly into 4
        wayPointIndex = 0;
        wayPoints = new Point[36];

        // First half of the waypoints are in the left column
        for(int i = 0; i < wayPoints.length/4; i++)
        {
            wayPoints[2*i] = new Point(x1, yBase+yMargin*i);
            wayPoints[2*i+1] = new Point(x2, yBase+yMargin*i);
        }

        // Last half of the waypoints are in the right column
        for(int i = 0; i < wayPoints.length/4; i++)
        {
            wayPoints[2*i+(wayPoints.length/2)] = new Point(x3, yBase+yMargin*i);
            wayPoints[2*i+(wayPoints.length/2)+1] = new Point(x4, yBase+yMargin*i);
        }

        // move to start
        stimContainer.setPadding(x1, yBase, 0, 0);
        testRunning = true;
    }


    private void finishTest()
    {
        // Do finsh test stuff
        testRunning = false;
        activity.OpenDrawer();
    }




    // Return test results as CSV String
    public String ToCSV()
    {
        String s = "X, Y \r\n";

        for(Point item : pauseList)
        {
            s += getStandardizedPointString(item);
        }

        // Clean record
        pauseList.clear();

        return s;
    }

    // Converts a point to 100 based and CSV formated
    private String getStandardizedPointString(Point point)
    {
        double x = ((100*point.x)/bounds.x);
        double y = ((100*point.y)/bounds.y);

        String pointString =  x + ", " + y +"\r\n";
        return pointString;
    }


    public String GetTestType()
    {
        return "ZigZag";
    }
}
