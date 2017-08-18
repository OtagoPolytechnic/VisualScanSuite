package bit.harrl7.visscan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by aberhjk1 on 5/06/2017.
 */

public class UserDialogFragment extends DialogFragment
{
    EditText userIDText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_userinput, container, false);
        Button confirmID = (Button)rootView.findViewById(R.id.btnSubmitID);
        userIDText = (EditText)rootView.findViewById(R.id.editUserID);

        confirmID.setOnClickListener(new ButtonSubmitHandler());

        getDialog().setTitle("Enter User ID");
        return rootView;
    }

    public class ButtonSubmitHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            String userID = userIDText.getText().toString();
            HomeActivity homeActivity = (HomeActivity)getActivity();
            homeActivity.setUserID(userID);
            dismiss();
        }
    }
}
