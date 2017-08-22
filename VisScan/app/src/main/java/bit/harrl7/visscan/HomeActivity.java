package bit.harrl7.visscan;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    //public static String userID;
    TextView tvUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        Button btnResults = (Button)findViewById(R.id.btnViewResults);
        Button btnToolList = (Button)findViewById(R.id.btnViewListOfTools);
        Button btnSwitchUser = (Button)findViewById(R.id.btnSwitchUser);
        tvUserID = (TextView)findViewById(R.id.tvUserID);

        btnToolList.setOnClickListener(new ButtonHandler());
        btnResults.setOnClickListener(new ButtonHandler());
        btnSwitchUser.setOnClickListener(new ButtonHandler());

        if((Patient.userID == null) || (Patient.dob == null))
        {
            StartUserIDFragment();
        }
        else
        {
            tvUserID.setText("Logged in as " + Patient.getUserID() + "DOB - " + Patient.getDOB());
        }

    }

    public void setUserID(String inputUserID)
    {

        Patient.setUserID(inputUserID.trim());

    }

    public void setUserDOB(int day, int month, int year)
    {
        StringBuilder bDayString = new StringBuilder();

        bDayString.append(day);
        bDayString.append("/");
        bDayString.append(month);
        bDayString.append("/");
        bDayString.append(year);

        Patient.setDate(String.valueOf(bDayString));
        tvUserID.setText("Logged in as " + Patient.getUserID() + ", D.O.B " + Patient.getDOB());

        //tvUserID.setText("Logged in as " + Patient.getUserID());
    }

    public void StartUserIDFragment()
    {
        FragmentManager fm = getFragmentManager();
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

            }



        }
    }
}
