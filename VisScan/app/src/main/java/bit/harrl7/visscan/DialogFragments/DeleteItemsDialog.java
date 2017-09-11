package bit.harrl7.visscan.DialogFragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bit.harrl7.visscan.Activities.MainActivity;
import bit.harrl7.visscan.Activities.ResultsActivity;
import bit.harrl7.visscan.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * Base class for comfirmation dialog fragments
 */
public class DeleteItemsDialog extends ConfirmationDialog {


    @Override
    public void onStart()
    {
        super.onStart();

        tvMessage.setText("Are you sure you want to delete the selected item/s?");

    }

    @Override
    // === Positive action ===
    public void PositiveAction()
    {
        ResultsActivity activity = (ResultsActivity) getActivity();

        activity.RemoveFiles();
        activity.SetNewAdapter();

        dismiss();
    }

    @Override
    // === Negative action ===
    public void NegativeAction()
    {
        dismiss();
    }
}