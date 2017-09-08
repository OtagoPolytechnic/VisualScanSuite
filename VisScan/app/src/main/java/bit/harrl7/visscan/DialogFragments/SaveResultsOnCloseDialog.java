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
    public enum ENextActivity { HOME, TOOL_LIST }

    MainActivity activity;
    public ENextActivity nextActivity;


    @Override
    public void onStart()
    {
        super.onStart();

        activity = (MainActivity) getActivity();

        tvMessage.setText("Would You Like To Save Your Results?");
    }



    // === Positive action ===
    // Save and go to next Activity
    @Override
    public void PositiveAction()
    {
        activity.OutputTestResults();

        if(nextActivity.equals(ENextActivity.HOME)) activity.ReturnToHomeActivity();
        if(nextActivity.equals(ENextActivity.TOOL_LIST)) activity.ReturnToToolListActivity();

        dismiss();
    }


    // === Negative action ===
    // Don't save and go to next Activity
    @Override
    public void NegativeAction()
    {
        if(nextActivity.equals(ENextActivity.HOME)) activity.ReturnToHomeActivity();
        if(nextActivity.equals(ENextActivity.TOOL_LIST)) activity.ReturnToToolListActivity();

        dismiss();
    }
}
