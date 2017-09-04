package bit.harrl7.visscan.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFileDialogFragment extends DialogFragment {


    public SaveFileDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.confirmation_dialog, null);

        //TextView message = (TextView)view.findViewById(R.id.txtLoginConfirmationMessage);
      //  message.setText("Are you " + empName + "?");



        builder.setView(view);
        return builder.create();
    }

}
