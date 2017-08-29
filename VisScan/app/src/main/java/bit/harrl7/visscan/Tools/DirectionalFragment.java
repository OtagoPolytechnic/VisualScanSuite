package bit.harrl7.visscan.Tools;


import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectionalFragment extends Fragment  implements IVisualTest
{
    ImageView stim;

    public DirectionalFragment() { } // Required empty public constructor


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_directional, container, false);

        // Stimulus
        stim = (ImageView) v.findViewById(R.id.ivDirStim);

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
            stim.setRotation(stim.getRotation()+90);
        }
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
