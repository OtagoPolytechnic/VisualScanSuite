package bit.harrl7.visscan;


import android.graphics.Point;
import android.graphics.Rect;
import android.location.LocationListener;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Random;


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
    ArrayList<ImageView> triangles;
    ArrayList<ImageView> squares;
    RelativeLayout layout;

    //STATICS
    private static int WIDTH = 90;
    private static int NUM_IMAGES = 20;
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

        //get the relative layout
        layout = (RelativeLayout)view.findViewById(R.id.layout);

        //crea a random
        rGen = new Random();

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds = main.GetBounds();

        //initialize the arraylists
        triangles = new ArrayList<ImageView>();
        smileys = new ArrayList<ImageView>();
        squares = new ArrayList<ImageView>();

        //populate the smileys and triangles.
        PopulateSmileys();
        PopulateTriangles();
        PopulateSquares();

        //return the view
        return view;

    }

    public void PopulateSmileys()
    {
        for(int i = 0; i < NUM_IMAGES; i++)
        {

            ImageView smileyIV = new ImageView(getActivity());

            //set x and y
            smileyIV.setX(rGen.nextInt(bounds.x - INSET));
            smileyIV.setY(rGen.nextInt(bounds.y - INSET));

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WIDTH + 20,HEIGHT + 20);

            smileyIV.setLayoutParams(layoutParams);

            Boolean isOverlapping = false;
            for(int j =0; j < smileys.size(); j++)
            {
                isOverlapping = checkHit(smileyIV, smileys.get(j));
                while(isOverlapping)
                {
                    smileyIV.setX(rGen.nextInt(bounds.x));
                    smileyIV.setY(rGen.nextInt(bounds.y));
                    isOverlapping = checkHit(smileyIV, smileys.get(j));
                }

            }

            //if it wasn't overlapping add to the array list of smiley faces
            smileys.add(smileyIV);

            //set the image view image to a smiley face
            smileyIV.setImageResource(R.drawable.smiley);

            //set the image view on click listener
            smileyIV.setOnClickListener(new ShapeClickHandler());

            int xStandardized= ((100*(int)smileyIV.getX())/bounds.x);
            int yStandardized= ((100*(int)smileyIV.getY())/bounds.y);
            LocationStimTrial stim = new LocationStimTrial(new Point(xStandardized, yStandardized), false);
            hitPoints.add(stim);

            //add the imageview to the layout
            layout.addView(smileyIV);

        }
    }

    public void PopulateTriangles()
    {
        for(int i = 0; i < NUM_IMAGES; i++)
        {
            //create an imageview
            ImageView triangleIV = new ImageView(getActivity());

            //set x and y
            triangleIV.setX(rGen.nextInt(bounds.x - INSET));
            triangleIV.setY(rGen.nextInt(bounds.y - INSET));

            //set the width and height of a triangle
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WIDTH,HEIGHT);
            triangleIV.setLayoutParams(layoutParams);

            //booleans to check for overlap
            Boolean isOverlapping = false;
            Boolean isOverlappingTriangles = false;

            //loop over the smileys checking if the triangle overlaps a smiley
            for(int j =0; j < smileys.size(); j++)
            {
                isOverlapping = checkHit(triangleIV, smileys.get(j));

                //keep trying new points till it doesn't overlap.
                while(isOverlapping)
                {
                    triangleIV.setX(rGen.nextInt(bounds.x));
                    triangleIV.setY(rGen.nextInt(bounds.y));
                    isOverlapping = checkHit(triangleIV, smileys.get(j));
                }
                //then loop over the other triangles to make sure it doesn't overlap with one of those
                for(int z = 0; z < triangles.size(); z++)
                {
                    //check for overlap
                    isOverlappingTriangles = checkHit(triangleIV, triangles.get(z));

                    //keep trying new points till it doesn't overlap.
                    while(isOverlappingTriangles)
                    {
                        triangleIV.setX(rGen.nextInt(bounds.x));
                        triangleIV.setY(rGen.nextInt(bounds.y));
                        isOverlappingTriangles = checkHit(triangleIV, triangles.get(j));
                    }
                }



            }

            //add the triangle to the array of triangles.
            triangles.add(triangleIV);

            //set the image resource of the triangle image view to a triangle
            triangleIV.setImageResource(R.drawable.triangle);

            triangleIV.setOnClickListener(new WrongShapeHandler());

            //add triangle to the layout
            layout.addView(triangleIV);

        }
    }

    public void PopulateSquares()
    {
        for(int i = 0; i < NUM_IMAGES; i++)
        {
            //create an imageview
            ImageView squareIV = new ImageView(getActivity());

            //set x and y
            squareIV.setX(rGen.nextInt(bounds.x - INSET));
            squareIV.setY(rGen.nextInt(bounds.y - INSET));

            //set the width and height of a triangle
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(WIDTH,HEIGHT);
            squareIV.setLayoutParams(layoutParams);

            //booleans to check for overlap
            Boolean isOverlapping = false;
            Boolean isOverlappingTriangles = false;
            Boolean isOverlappingSquares = false;

            //loop over the smileys checking if the triangle overlaps a smiley
            for(int j =0; j < smileys.size(); j++)
            {
                isOverlapping = checkHit(squareIV, smileys.get(j));

                //keep trying new points till it doesn't overlap.
                while(isOverlapping)
                {
                    squareIV.setX(rGen.nextInt(bounds.x));
                    squareIV.setY(rGen.nextInt(bounds.y));
                    isOverlapping = checkHit(squareIV, smileys.get(j));
                }
                //then loop over the other triangles to make sure it doesn't overlap with one of those
                for(int z = 0; z < triangles.size(); z++)
                {
                    //check for overlap
                    isOverlappingTriangles = checkHit(squareIV, triangles.get(z));

                    //keep trying new points till it doesn't overlap.
                    while(isOverlappingTriangles)
                    {
                        squareIV.setX(rGen.nextInt(bounds.x));
                        squareIV.setY(rGen.nextInt(bounds.y));
                        isOverlappingTriangles = checkHit(squareIV, triangles.get(j));
                    }

                    for(int s = 0; s < squares.size(); s++)
                    {
                        isOverlappingSquares = checkHit(squareIV, squares.get(s));

                        //keep trying new points till it doesn't overlap.
                        while(isOverlappingSquares)
                        {
                            squareIV.setX(rGen.nextInt(bounds.x));
                            squareIV.setY(rGen.nextInt(bounds.y));
                            isOverlappingSquares= checkHit(squareIV, squares.get(z));
                        }
                    }
                }



            }

            //add the triangle to the array of triangles.
            squares.add(squareIV);

            //set the image resource of the triangle image view to a triangle
            squareIV.setImageResource(R.drawable.square);

            squareIV.setOnClickListener(new WrongShapeHandler());

            //add triangle to the layout
            layout.addView(squareIV);

        }
    }



    private boolean checkHit(View v, View hit){

        //set the x,y,right and bottom of the view
        int viewX =  (int)v.getX();
        int viewY = (int)v.getY();
        int viewRight = (int)v.getX() + WIDTH;
        int viewBottom = (int)v.getY() + HEIGHT;

        //set the x,y,right and bottom of the other view
        int otherViewX = (int)hit.getX();
        int otherViewY = (int)hit.getY();
        int otherViewRight = (int)hit.getX() + WIDTH;
        int otherViewBottom = (int)hit.getY() + HEIGHT;

        //create the rectangles
        Rect rectView = new Rect(viewX, viewY, viewRight , viewBottom);
        Rect otherView = new Rect(otherViewX, otherViewY, otherViewRight, otherViewBottom);

        //return a bool if they intersect with each other
        return rectView.intersect(otherView);
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


        }
    }


    public class WrongShapeHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            //DO SOMETHING..
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
        for(Object item : hitPoints)
        {
            LocationStimTrial stim = (LocationStimTrial) item;
            s += stim.ToCsv() +"\r\n";
        }

        return s;
    }

    @Override
    public String GetTestType()
    {
        //
        return "Location";
    }
}
