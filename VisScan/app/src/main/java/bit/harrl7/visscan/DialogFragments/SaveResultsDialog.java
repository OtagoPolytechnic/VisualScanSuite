package bit.harrl7.visscan.DialogFragments;


import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Toast;

import bit.harrl7.visscan.Activities.MainActivity;

/**
 * Created by Liam on 04-Sep-17.
 *
 * Dialog to ask user if they would like to save results
 *
 */

public class SaveResultsDialog extends ConfirmationDialog
{
    MainActivity activity;

    @Override
    public void onStart()
    {
        super.onStart();

        activity = (MainActivity) getActivity();

        tvMessage.setText("Are you sure you want to save and end the test?");
    }

    @Override
    // === Positive action ===
    public void PositiveAction()
    {
        activity.OutputTestResults();
        activity.ReturnToHomeActivity();
        dismiss();
    }

    @Override
    // === Negative action ===
    public void NegativeAction()
    {
        dismiss();
    }
}
