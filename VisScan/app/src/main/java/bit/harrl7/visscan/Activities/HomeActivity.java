package bit.harrl7.visscan.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import bit.harrl7.visscan.Patient;
import bit.harrl7.visscan.R;
import bit.harrl7.visscan.UserDialogFragment;

public class HomeActivity extends AppCompatActivity {

    //public static String userID;
    TextView tvUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton btnCloseApp = (ImageButton)findViewById(R.id.imgCloseApp);
        Button btnResults = (Button)findViewById(R.id.btnViewResults);
        Button btnToolList = (Button)findViewById(R.id.btnViewListOfTools);
        Button btnSwitchUser = (Button)findViewById(R.id.btnSwitchUser);
        tvUserID = (TextView)findViewById(R.id.tvUserID);

        btnToolList.setOnClickListener(new ButtonHandler());
        btnResults.setOnClickListener(new ButtonHandler());
        btnSwitchUser.setOnClickListener(new ButtonHandler());
        btnCloseApp.setOnClickListener(new ButtonHandler());

        if((Patient.userID == null) || (Patient.dob == null))
        {
            StartUserIDFragment();
        }
        else
        {
            tvUserID.setText("Logged in as: " + Patient.getUserID());
        }

    }

    public void setUserID(String inputUserID)
    {
        tvUserID.setVisibility(View.VISIBLE);
        Patient.setUserID(inputUserID.trim());
    }

    public void setUserDOB(int day, int month, int year)
    {
        StringBuilder bDayString = new StringBuilder();

        bDayString.append(day);
        bDayString.append("-");
        bDayString.append(month);
        bDayString.append("-");
        bDayString.append(year);

        tvUserID.setVisibility(View.VISIBLE);
        Patient.setDate(String.valueOf(bDayString));
        tvUserID.setText("Logged in as " + Patient.getUserID());

        //tvUserID.setText("Logged in as " + Patient.getUserID());
    }

    public void StartUserIDFragment()
    {
        FragmentManager fm = getFragmentManager();
        tvUserID.setVisibility(View.INVISIBLE);
        UserDialogFragment dialogFragment = new UserDialogFragment ();
        dialogFragment.show(fm, "User ID Fragment");
    }

    public class ButtonHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btnViewListOfTools:
                    Intent listToolsIntent = new Intent(HomeActivity.this, ToolListActivity.class);
                    startActivity(listToolsIntent);
                    break;
                case R.id.btnViewResults:
                    Intent resultsIntent = new Intent(HomeActivity.this, ResultsActivity.class);
                    startActivity(resultsIntent);
                    break;
                case R.id.btnSwitchUser:
                    StartUserIDFragment();
                    break;
                case R.id.imgCloseApp:
                    System.exit(0); //close the app.
                    break;


            }



        }
    }
}
