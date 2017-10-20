package bit.harrl7.visscan.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import bit.harrl7.visscan.DialogFragments.SaveResultsDialog;
import bit.harrl7.visscan.DialogFragments.SaveResultsOnCloseDialog;
import bit.harrl7.visscan.Enums.ETestType;
import bit.harrl7.visscan.IVisualTest;
import bit.harrl7.visscan.Patient;
import bit.harrl7.visscan.R;
import bit.harrl7.visscan.Tools.ContrastFrag;
import bit.harrl7.visscan.Tools.DirectionalFragment;
import bit.harrl7.visscan.Tools.FlashStimFrag;
import bit.harrl7.visscan.Tools.LineBisection;
import bit.harrl7.visscan.Tools.LocationFrag;
import bit.harrl7.visscan.Tools.PeripherialFlashFrag;
import bit.harrl7.visscan.Tools.WalkDiagonalFrag;
import bit.harrl7.visscan.Tools.WanderStimFrag;
import bit.harrl7.visscan.Tools.ZigZagFrag;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity
{
    private int updateDelay = 30;

    TextView tvPause;
    TextView tvName;
    DrawerLayout drawer;
    ETestType selectTool;
    Intent chosenIntent;

    // Display dimensions
    int height;
    int width;

    Thread timer;

    // Fragment for visual test
    IVisualTest testFrag;

    boolean saveResults;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab selected vision tool from ToolListActivity
        Intent data = getIntent();
        selectTool = (ETestType) data.getSerializableExtra("toolType");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        // Controls
        tvPause = (TextView) findViewById(R.id.tvPause);
        tvName = (TextView)findViewById(R.id.tvNameOfTest);

        //Variable to check if user wants to save
        saveResults = false;

        // Drawer
        drawer = (DrawerLayout) findViewById(R.id.activity_flash_stim);
        drawer.setDrawerListener(new DrawSlidePauseHandler());
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        // Start button
        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ButtonStartClickHandler());

        // Save button
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new ButtonSaveResultsClickHandler());

        Button btnToolList = (Button)findViewById(R.id.btnBackToTools);
        btnToolList.setOnClickListener(new ButtonToolsClickHandler());

        Button btnHome = (Button)findViewById(R.id.btnBackToHome);
        btnHome.setOnClickListener(new ButtonHomeClickHandler());

        //Show the tool from the correct choice from toollist.activity
        ShowTestFrag(selectTool);
        tvPause.setVisibility(GONE);
        RunTimer();
    }


    // Open test fragment
    public void ShowTestFrag(ETestType type)
    {
        Fragment dynamicFragment = null;
        saveResults = false;

        switch (type)
        {
            case Flash:
                tvName.setText("Flash Test");
                dynamicFragment = new FlashStimFrag();
                break;
                
            case Wander:
                tvName.setText("Wander Test");
                dynamicFragment = new WanderStimFrag();
                break;
                
            case Peripheral:
                tvName.setText("Peripheral Test");
                dynamicFragment = new PeripherialFlashFrag();
                break;

            case Walking_Diagonal:
                tvName.setText("Diagonal Test");
                dynamicFragment = new WalkDiagonalFrag();
                break;

            case ZigZag:
                tvName.setText("Zigzag Test");
                dynamicFragment = new ZigZagFrag();
                break;
                
            case ShapeCancellation:
                tvName.setText("Cancellation Test");
                dynamicFragment = new LocationFrag();
                break;
                
            case Contrast:
                tvName.setText("Constrast Test");
                dynamicFragment = new ContrastFrag();
                break;

            case Directional:
                tvName.setText("Directional Test");
                dynamicFragment = new DirectionalFragment();
                break;

            case Line_Bisection:
                tvName.setText("Line Bisection");
                dynamicFragment = new LineBisection();
                break;
        }

        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if(dynamicFragment != null) { ft.replace(R.id.fragment_container, dynamicFragment); }
        ft.commit();

        testFrag = (IVisualTest) dynamicFragment;
    }

    // Start the test
    public class ButtonStartClickHandler implements OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            drawer.closeDrawers();
            tvPause.setVisibility(GONE);
            RunTimer();
        }
    }

    // Save results
    public class ButtonSaveResultsClickHandler implements OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            if(testFrag == null) { Toast.makeText(MainActivity.this, "No results to save", Toast.LENGTH_LONG).show(); }
            else
            {

                new SaveResultsDialog().show(getFragmentManager(), "");

            }
        }
    }

    // Back to tools
    public class ButtonToolsClickHandler implements OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            //Go to the tool list activity, show the save dialog before going there incase they need to save
            chosenIntent = new Intent(MainActivity.this, ToolListActivity.class );
            ShowSaveOnCloseDialog(SaveResultsOnCloseDialog.ENextActivity.TOOL_LIST);

        }
    }

    // Back to tools
    public class ButtonHomeClickHandler implements OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            //Go to the home activity, show the save dialog before going there incase they need to save
            chosenIntent = new Intent(MainActivity.this, HomeActivity.class );
            ShowSaveOnCloseDialog(SaveResultsOnCloseDialog.ENextActivity.HOME);

        }
    }

    // === Return to Home Activity ===
    public void ReturnToHomeActivity() { startActivity(new Intent(MainActivity.this, HomeActivity.class )); }

    // === Return to Tool List Activity ===
    public void ReturnToToolListActivity() { startActivity(new Intent(MainActivity.this, ToolListActivity.class )); }


    public void ShowSaveOnCloseDialog(SaveResultsOnCloseDialog.ENextActivity nextActivity)
    {
        if(saveResults)
        {
            startActivity(chosenIntent);
        }
        else
        {
            SaveResultsOnCloseDialog saveDialog = new SaveResultsOnCloseDialog();
            saveDialog.setCancelable(false);
            saveDialog.show(getFragmentManager(), "");
            saveDialog.nextActivity = nextActivity;
        }
    }


    // Pause on draw slide
    public class DrawSlidePauseHandler implements DrawerLayout.DrawerListener
    {

        @Override
        public void onDrawerOpened(View drawerView)
        {
            if(timer != null) timer.interrupt();        // Pause timer
            tvPause.setVisibility(VISIBLE);             // Show Pause text

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);   // Lock drawer open
        }

        @Override
        public void onDrawerClosed(View drawerView)
        {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);  // Unlock drawer
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) { }  // Empty


        @Override
        public void onDrawerStateChanged(int newState) { } // Empty
    }


    //  Start the timer, run current fragment loop
    public void RunTimer()
    {
         //testFrag = (IVisualTest) getFragmentManager().findFragmentById(R.id.fragment_container);

        timer = new Thread()
        {

            @Override
            public void run()
            {
                try {
                    while (!isInterrupted())
                    {
                        Thread.sleep(updateDelay);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                testFrag.Run();
                            }
                        });
                    }
                } catch (InterruptedException e) { } // Handle error
            }
        };

        timer.start();
    }


    // Open the drawer to end the current test
    public void OpenDrawer()
    {

        drawer.openDrawer(GravityCompat.START);
    }


    // On back pressed open the drawer
    @Override
    public void onBackPressed() { OpenDrawer(); }


    // Print results to .csv file at /VisScan
    public void OutputTestResults()
    {

        // Storage dir
        String docsFolder = Environment.getExternalStorageDirectory().toString();

        // Outer folder, make if doesn't exist already
        File resultsFolder = new File(docsFolder +"/VisScan");
        if(!resultsFolder.exists())
        {
            resultsFolder.mkdirs();
        }

        // Date
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String date = df.format(new Date());

        SimpleDateFormat tf = new SimpleDateFormat("HH-mm-ss");
        String time = tf.format(new Date());

        String dateTime = "_date" + date + "_time" + time;

        // Filename
        File file = new File(resultsFolder.getAbsoluteFile().toString(), "_user" + Patient.getUserID() + "_tool" + testFrag.GetTestType() + dateTime + ".csv");

        try
        {
            file.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        // Write to file

        // Get results data
        String csv = "";

        try
        {
            IVisualTest fragment = (IVisualTest) getFragmentManager().findFragmentById(R.id.fragment_container);
            csv = fragment.ToCSV();
        }
        catch (NullPointerException e) {}

        // Write results
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(csv);
            Toast.makeText(MainActivity.this, "Result Saved", Toast.LENGTH_SHORT).show();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Screen bounds
    public Point GetBounds() { return new Point(width, height); }

    // Update frequency, use to time test logic in seconds
    public double GetUpdateFreg() { return 1000/updateDelay; }

}
