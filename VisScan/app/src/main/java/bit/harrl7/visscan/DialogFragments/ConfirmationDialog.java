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
 *
 * Base class for comfirmation dialog fragments
 */
public class ConfirmationDialog extends DialogFragment
{
    // Views
    TextView tvMessage;

    Button btnPos;
    Button btnNeg;


    public ConfirmationDialog() { }  // Required empty public constructor

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.confirmation_dialog, null);


        // === Views ===
        tvMessage = (TextView) v.findViewById(R.id.tvMessage);

        btnPos = (Button) v.findViewById(R.id.btnPositive);
        btnNeg = (Button) v.findViewById(R.id.btnNegative);

        btnPos.setOnClickListener(new PositiveBtnClickHandler());
        btnNeg.setOnClickListener(new NegativeBtnClickHandler());

        builder.setView(v);

        Dialog d =  builder.create();
        d.setCanceledOnTouchOutside(false);

        return d;
    }

    public class PositiveBtnClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            PositiveAction();
        }
    }

    public class NegativeBtnClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            NegativeAction();
        }
    }


    // === Positive action ===
    public void PositiveAction() { }

    // === Negative action ===
    public void NegativeAction() { }
}
