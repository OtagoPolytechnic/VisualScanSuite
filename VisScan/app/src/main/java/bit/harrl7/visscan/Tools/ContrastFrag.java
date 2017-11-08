package bit.harrl7.visscan.Tools;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.Trial_Records.ContrastStimObject;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.R;
import bit.harrl7.visscan.Trial_Records.LocationStimTrial;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContrastFrag extends Fragment implements IVisualTest {

/*    //DATA MEMBERS
    ListView lvConstrasts;
    ArrayList<String> wordsForListView = new ArrayList<String>();
    public static int NUM_WORDS = 14;
    public static int RGB_MULTIPLIER = 24;
    Random rand;
    ArrayList<ContrastStimObject> stimObject;



    public ContrastFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_constrast, container, false);

        //Array of stimulus objects.
        stimObject = new ArrayList<ContrastStimObject>();

        rand = new Random();
        int top = 6;
        char data = ' ';
        String text = "";




        //add the words to the list and the stim objects
        for(int i = 0; i < NUM_WORDS; i++)
        {
            text = "";
            for (int j=0; j<=top; j++) {
                data = (char)(rand.nextInt(25)+97);
                text = data + " " + text;

            }
            String outputText= text.toUpperCase();

            wordsForListView.add(outputText);
            stimObject.add(new ContrastStimObject(i * RGB_MULTIPLIER, false));
        }

        //create list view
        lvConstrasts = (ListView)v.findViewById(R.id.lvConstrasts);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.listviewconstrast, wordsForListView){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);

                //set the rgb value of the textview to multiples of 24,  eg. 0,0,0 - 24,24,24 etc.
                int rgb = position * RGB_MULTIPLIER;
                if(rgb < 255)
                {
                    ((TextView)view.findViewById(R.id.tvContrast)).setTextColor(Color.rgb(rgb, rgb, rgb));
                }
                else
                {
                    rgb = 255;
                    ((TextView)view.findViewById(R.id.tvContrast)).setTextColor(Color.rgb(rgb, rgb, rgb));
                }


                return view;
            };
        };

        //set the adapter for the listview
        lvConstrasts.setAdapter(adapter);

        //set on click listener for listview
        lvConstrasts.setOnItemClickListener(new ListViewListener());
        return v;
    }


    public class ListViewListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //get the text view of the item clicked.
            TextView tv = (TextView)view.findViewById(R.id.tvContrast);

            //set the text to invisible
            tv.setTextColor(Color.TRANSPARENT);

            //set the hit value to true
            stimObject.get(position).hit = true;

            //set the background color to green
            view.setBackgroundColor(Color.LTGRAY);

        }
    }

    @Override
    public void Run() {

    }

    @Override
    public String ToCSV() {
        String s = "Contrast, Hit\r\n";

        //OUTPUT TO CSV
        for(Object item : stimObject)
        {
            ContrastStimObject stim = (ContrastStimObject) item;
            s += stim.ToCsv();
        }

        return s;
    }

    @Override
    public String GetTestType() {
        return "Contrast";
    }
}*/

    //CLASS DATA MEMBERS
    Point bounds;
    ArrayList<ContrastStimObject> hitPoints;
    Random rGen;
    Point pos;
    ArrayList<TextView> contrastWords;
    RelativeLayout layout;
    Vibrator vibe;
    public static List<Integer> rgbValues = new ArrayList<Integer>();

    //STATICS

    private static int NUM_IMAGES = 10;
    public int letterWidth;
    public int letterHeight;

    public ContrastFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        pos = new Point(0, 0);

        for (int i = 1; i < 21; i++) {
            rgbValues.add(i * 12);
        }


        //list of points where the user has clicked the smiley
        hitPoints = new ArrayList<ContrastStimObject>();
        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //get the relative layout
        layout = (RelativeLayout) view.findViewById(R.id.layout);

        //crea a random
        rGen = new Random();

        // Screen bounds for stim placement
        MainActivity main = (MainActivity) getActivity();
        bounds = main.GetBounds();
        letterWidth = bounds.x / NUM_IMAGES;
        letterHeight = bounds.y / NUM_IMAGES;

        contrastWords = new ArrayList<TextView>();

        DrawLetters();

        //return the view
        return view;

    }

    public void DrawLetters()
    {


        for (int rows = 0; rows < NUM_IMAGES; rows++) {
            for (int cols = 0; cols < NUM_IMAGES; cols++) {
                TextView contrastText = new TextView(getActivity());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(letterWidth, letterHeight);

                int rgb = rgbValues.get(rGen.nextInt(rgbValues.size()));
                char data = (char)(rGen.nextInt(25)+97);
                contrastText.setLayoutParams(layoutParams);

                contrastText.setTextSize(30f);

                contrastText.setX(rows * (bounds.x / NUM_IMAGES));
                contrastText.setY(cols * (bounds.y / NUM_IMAGES));

                //set text and colour
                contrastText.setText(String.valueOf(data).toUpperCase());
                contrastText.setTextColor(Color.rgb(rgb, rgb, rgb));
                contrastText.setGravity(Gravity.CENTER);
                contrastWords.add(contrastText);

                //add the textview to the layout
                layout.addView(contrastText);

                //set the onclick listener
                contrastText.setOnClickListener(new ContrastFrag.ShapeClickHandler());

                int xStandardized= ((100*(int)contrastText.getX())/bounds.x);
                int yStandardized= ((100*(int)contrastText.getY())/bounds.y);
                ContrastStimObject stim = new ContrastStimObject(new Point(xStandardized, yStandardized), false,rgb);
                hitPoints.add(stim);

            }


        }
    }


    //Click handler for the imageviews that are clickable.
    public class ShapeClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //get the imageview that was clicked.
            TextView textHit = (TextView) v;

            //change the smiley face to a smiley face with a cross through it
            textHit.setText("✓");
            textHit.setTextColor(Color.WHITE);

            //disable the clicked image
            textHit.setEnabled(false);

            int xStandardized = ((100 * (int) textHit.getX()) / bounds.x);
            int yStandardized = ((100 * (int) textHit.getY()) / bounds.y);

            //add the smiley face that was hit to an arraylist of points
            for (ContrastStimObject stim : hitPoints) {
                //if its the stim thats hit, set its boolean to true

                if ((stim.pos.x == xStandardized) && (stim.pos.y == yStandardized)) {
                    stim.hit = true;
                }
            }

        }
    }


    @Override
    public void Run() {

    }

    @Override
    public String ToCSV() {
        String s = "Contrast, Hit\r\n";

        //OUTPUT TO CSV
        for(Object item : hitPoints)
        {
            ContrastStimObject stim = (ContrastStimObject) item;
            s += stim.ToCsv();
        }

        return s;
    }

    @Override
    public String GetTestType() {
        //
        return "Contrast";
    }
}
