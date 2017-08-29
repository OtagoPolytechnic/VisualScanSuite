package bit.harrl7.visscan.Tools;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;


/**
 * A static stimulus will appear periodicaly, prompting the user for input
 */
public class DirectionalSizeFrag extends Fragment implements IVisualTest
{





    public DirectionalSizeFrag()
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
        //bounds =  main.GetBounds();



        return v;
    }

    @Override
    public void Run()
    {

    }


    // On click
    public class CickHandler implements OnClickListener
    {

        @Override
        public void onClick(View view)
        {

        }
    }



    // Return test results as CSV String
    public String ToCSV()
    {
        String s = "X, Y, Hit, Shown, Diameter,Size\r\n";

        return s;
    }

    public String GetTestType()
    {
        return "Directional";
    }

}
