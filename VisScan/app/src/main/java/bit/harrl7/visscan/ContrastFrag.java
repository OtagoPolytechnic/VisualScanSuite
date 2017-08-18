package bit.harrl7.visscan;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContrastFrag extends Fragment implements IVisualTest {

    //DATA MEMBERS
    ListView lvConstrasts;
    ArrayList<String> wordsForListView = new ArrayList<String>();
    public static int NUM_WORDS = 10;
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
        /*
        rand = new Random();
        int top = 6;
        char data = ' ';
        String text = "";


        for (int i=0; i<=top; i++) {
            data = (char)(rand.nextInt(25)+97);
            text = data + " " + text;
        } */

        //add the words to the list and the stim objects
        for(int i = 0; i < NUM_WORDS; i++)
        {

            wordsForListView.add("H E L L O");
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
                ((TextView)view.findViewById(R.id.tvContrast)).setTextColor(Color.rgb(rgb, rgb, rgb));

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
            tv.setTextColor(Color.GREEN);

            //set the hit value to true
            stimObject.get(position).hit = true;

            //set the background color to green
            view.setBackgroundColor(Color.GREEN);

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
}
