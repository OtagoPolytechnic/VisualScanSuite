package bit.harrl7.visscan.Tools;


import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Random;

import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Trial_Records.LocationStimTrial;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFrag extends Fragment implements IVisualTest {

    //CLASS DATA MEMBERS
    Point bounds;
    ArrayList<LocationStimTrial> hitPoints;
    Random rGen;
    Point pos;
    ArrayList<ImageView> smileys;
    RelativeLayout layout;
    Vibrator vibe;

    //STATICS
    private static int WIDTH = 90;
    private static int NUM_IMAGES = 12;
    private static int HEIGHT =90;
    private static int INSET = 120;

    public LocationFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        pos = new Point(0, 0);

        //list of points where the user has clicked the smiley
        hitPoints = new ArrayList<LocationStimTrial>();
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //get the relative layout
        layout = (RelativeLayout)view.findViewById(R.id.layout);

        //crea a random
        rGen = new Random();

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds = main.GetBounds();

        smileys = new ArrayList<ImageView>();

        DrawSmileys();

        //return the view
        return view;

    }

    public void DrawSmileys()
    {
        for(int rows = 0; rows < NUM_IMAGES; rows++)
        {
            for(int cols = 0; cols < 10; cols++)
            {
                ImageView smileyIV = new ImageView(getActivity());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WIDTH + 20,HEIGHT + 20);

                smileyIV.setLayoutParams(layoutParams);

                smileyIV.setX(rows * (bounds.x / NUM_IMAGES));
                smileyIV.setY(cols * (bounds.y /NUM_IMAGES + 25));

                smileys.add(smileyIV);

                switch(rGen.nextInt(3))
                {
                    case 0:
                        smileyIV.setImageResource(R.drawable.smiley);

                        //set the image view on click listener
                        smileyIV.setOnClickListener(new ShapeClickHandler());

                        int xStandardized= ((100*(int)smileyIV.getX())/bounds.x);
                        int yStandardized= ((100*(int)smileyIV.getY())/bounds.y);
                        LocationStimTrial stim = new LocationStimTrial(new Point(xStandardized, yStandardized), false);
                        hitPoints.add(stim);
                        break;
                    case 1:
                        smileyIV.setImageResource(R.drawable.triangle);
                        smileyIV.setOnClickListener(new WrongShapeHandler());
                        break;
                    case 2:
                        smileyIV.setImageResource(R.drawable.square);
                        smileyIV.setOnClickListener(new WrongShapeHandler());
                        break;

                }

                layout.addView(smileyIV);
            }


        }
    }


    //Click handler for the imageviews that are clickable.
    public class ShapeClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            //get the imageview that was clicked.
            ImageView imageHit = (ImageView)v;

            //change the smiley face to a smiley face with a cross through it
            imageHit.setImageResource(R.drawable.smileyhit);

            //disable the clicked image
            imageHit.setEnabled(false);

            int xStandardized= ((100*(int)imageHit.getX())/bounds.x);
            int yStandardized= ((100*(int)imageHit.getY())/bounds.y);

            //add the smiley face that was hit to an arraylist of points
            for(LocationStimTrial stim : hitPoints)
            {
                //if its the stim thats hit, set its boolean to true

                if((stim.pos.x == xStandardized )&&(stim.pos.y == yStandardized))
                {
                    stim.hit = true;
                }
            }

            if(vibe.hasVibrator())
            {
                vibe.vibrate(200);
            }


        }
    }


    public class WrongShapeHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

        }
    }


    @Override
    public void Run() {

    }

    @Override
    public String ToCSV()
    {
        String s = "X, Y, Hit\r\n";

        //NEEDS TO CHANGE TO ALL HIT AND MISSED.
        for(LocationStimTrial item : hitPoints)
        {

            s += item.ToCsv() +"\r\n";
        }

        return s;
    }

    @Override
    public String GetTestType()
    {
        //
        return "Shape Cancellation";
    }
}
