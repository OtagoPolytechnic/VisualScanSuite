package bit.harrl7.visscan.DialogFragments;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import bit.harrl7.visscan.Activities.MainActivity;

/**
 * Created by Liam on 04-Sep-17.
 *
 * Dialog to ask user if they would like to save results
 * before sending user to a new Activity
 */

public class SaveResultsOnCloseDialog extends ConfirmationDialog
{
    MainActivity activity;

    @Override
    public void onStart()
    {
        super.onStart();

        activity = (MainActivity) getActivity();

        tvMessage.setText("Would You Like To Save Your Results?");
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
        activity.ReturnToHomeActivity();
        dismiss();
    }
}
