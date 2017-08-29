package bit.harrl7.visscan;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayToolFrag extends Fragment {

 TextView tvNameOfTool;
    TextView tvDescription;
    Button btnStartTool;
    ImageView imgOfTest;

    ETestType selectedTool;
    public DisplayToolFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_tool, container, false);
        //get the name of the tool clicked from the activity (ToolListActivity)
        String toolName = getArguments().getString("toolname");

        //get a ref to the description text view
        tvDescription = (TextView)v.findViewById(R.id.tvInfoTool);
        imgOfTest = (ImageView)v.findViewById(R.id.imgToolShow);
        selectedTool = ETestType.Error;

        //Testing, might be better to just pass across as bundle/something else
        GetEnumForTool(toolName);

        //Button reference and listener setup
        btnStartTool = (Button)v.findViewById(R.id.btnStartTool);
        btnStartTool.setOnClickListener(new ButtonStartToolListener());

        //text view containing the name of the tool clicked.
        tvNameOfTool = (TextView)v.findViewById(R.id.tvDescriptionTool);
        tvNameOfTool.setText(toolName);

        //return the view
        return v;
    }

    //Button click listener
    public class ButtonStartToolListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            //TO BE CHANGED (WILL START A TOOL)
            Intent toolIntent = new Intent(getActivity(), MainActivity.class);
            toolIntent.putExtra("toolType", selectedTool);
            startActivity(toolIntent);
        }
    }

    private void GetEnumForTool(String toolName)
    {
        switch(toolName)
        {
            case "Wander":
                selectedTool = ETestType.Wander;
                imgOfTest.setImageResource(R.drawable.wandertest);
                tvDescription.setText(R.string.desc_wander);
                break;
            case "Flash":
                selectedTool = ETestType.Flash;
                imgOfTest.setImageResource(R.drawable.flashtest);
                tvDescription.setText(R.string.desc_flash);
                break;
            case "Flash Size":
                selectedTool = ETestType.FlashSize;
                imgOfTest.setImageResource(R.drawable.flashtest);
                tvDescription.setText(R.string.desc_Size);
                break;
            case "Walking Diagonal":
                selectedTool = ETestType.Walking_Diagonal;
                imgOfTest.setImageResource(R.drawable.diagonaltest);
                tvDescription.setText(R.string.desc_diagonal);
                break;
            case "Zig Zag":
                selectedTool = ETestType.ZigZag;
                imgOfTest.setImageResource(R.drawable.zigzag);
                tvDescription.setText(R.string.desc_zigzig);
                break;
            case "Shape Cancellation":
                selectedTool = ETestType.ShapeCancellation;
                tvDescription.setText(R.string.desc_shape_cancel);
                break;
            case "Contrast":
                selectedTool = ETestType.Contrast;
                tvDescription.setText(R.string.desc_contrast);
                break;
            case "Peripherial":
                selectedTool = ETestType.Peripheral;
                tvDescription.setText(R.string.desc_peripheral);
                break;
            default:
                selectedTool = ETestType.Error;
                tvDescription.setText("ERROR, Something went wrong!");

        }

    }

}
