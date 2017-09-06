package bit.harrl7.visscan.DialogFragments;

import android.view.View;

import bit.harrl7.visscan.Activities.MainActivity;

/**
 * Created by Liam on 04-Sep-17.
 */

public class NoItemsSelectedDialog extends ConfirmationDialog
{
    @Override
    public void onStart()
    {
        super.onStart();

        tvMessage.setText("No items selected");

        btnNeg.setVisibility(View.INVISIBLE);
        btnPos.setText("OK");

    }

    @Override
    // === Positive action ===
    public void PositiveAction()
    {
        dismiss();
    }

    @Override
    // === Negative action ===
    public void NegativeAction()
    {

    }
}
