package bit.harrl7.visscan.DialogFragments;

import android.widget.Toast;

import bit.harrl7.visscan.Activities.MainActivity;

/**
 * Created by Liam on 04-Sep-17.
 */

public class SaveResultsDialog extends ConfirmationDialog
{
    @Override
    public void onStart()
    {
        super.onStart();

        tvMessage.setText("Would You Like To Save Your Results?");
    }


    @Override
    // === Positive action ===
    public void PositiveAction()
    {
        MainActivity activity = (MainActivity) getActivity();

        activity.OutputTestResults();
        activity.ReturnToHomeActivity();

        dismiss();
    }

    @Override
    // === Negative action ===
    public void NegativeAction()
    {
        MainActivity activity = (MainActivity) getActivity();

        activity.ReturnToHomeActivity();

        dismiss();
    }
}
