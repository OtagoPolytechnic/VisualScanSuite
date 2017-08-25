package bit.harrl7.visscan;


import android.app.DialogFragment;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aberhjk1 on 5/06/2017.
 */

public class UserDialogFragment extends DialogFragment
{
    EditText userIDText;
    DatePicker datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_userinput, container, false);
        Button confirmID = (Button)rootView.findViewById(R.id.btnSubmitID);
        userIDText = (EditText)rootView.findViewById(R.id.editUserID);
        datePicker = (DatePicker)rootView.findViewById(R.id.datePicker);
        confirmID.setOnClickListener(new ButtonSubmitHandler());
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setTitle("Personal Details");
        return rootView;
    }

    public class ButtonSubmitHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            if(userIDText.getText().toString().equals(""))
            {
                userIDText.setError("Required");

            }
            else
            {
                String userID = userIDText.getText().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                HomeActivity homeActivity = (HomeActivity)getActivity();
                homeActivity.setUserID(userID);
                homeActivity.setUserDOB(day, month, year);
                dismiss();
            }

        }
    }


}
