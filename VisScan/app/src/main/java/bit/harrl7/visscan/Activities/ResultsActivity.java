package bit.harrl7.visscan.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bit.harrl7.visscan.DialogFragments.DeleteItemsDialog;
import bit.harrl7.visscan.DialogFragments.NoItemsSelectedDialog;
import bit.harrl7.visscan.Patient;
import bit.harrl7.visscan.Graphing.PlottableObject;
import bit.harrl7.visscan.R;
import bit.harrl7.visscan.ResultsList;

public class ResultsActivity extends AppCompatActivity {

    ListView lvFiles;
    ArrayList<ResultsList> resultFiles;
    String path;
    CustomArrayAdapter adapter;
    Button btnDeleteResults;
    Button btnEmail;
    Button btnViewOnDevice;
    Button btnGraph;
    ImageButton btnBackToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //set up buttons
        btnDeleteResults = (Button)findViewById(R.id.btnDeleteResults);
        btnEmail = (Button)findViewById(R.id.btnSaveToEmail);
        btnViewOnDevice = (Button)findViewById(R.id.btnViewDevice);
        btnGraph = (Button)findViewById(R.id.btnGraph);
        btnBackToHome = (ImageButton)findViewById(R.id.imgbtnBackHome);

        //set listview
        lvFiles = (ListView)findViewById(R.id.lvFiles);
        lvFiles.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        //create results array list
        resultFiles = new ArrayList<>();

        //get path
        path = Environment.getExternalStorageDirectory().toString()+"/VisScan";
        //get directory
        File directory = new File(path);

        //get all files in the directory
        File[] files = directory.listFiles();

        if(files != null)
        {
            //loop over files adding to array list of Results
            for (int i = 0; i < files.length; i++)
            {
                if(files[i].getName().startsWith("_user" + Patient.userID))
                {
                    resultFiles.add(new ResultsList(files[i].getName(), false));
                }

            }
        }
        else
        {
            Toast.makeText(this, "No results yet!", Toast.LENGTH_LONG).show();
        }


        Collections.sort(resultFiles);

        //set adapter and pass it the list of tools and a custom layout (listviewlayout.xml)
        adapter = new CustomArrayAdapter(ResultsActivity.this, R.layout.listview_results, resultFiles);

        //set the listview adapter
        lvFiles.setAdapter(adapter);

        //set on click listeners
        lvFiles.setOnItemClickListener(new ListViewListener());
        btnDeleteResults.setOnClickListener(new ButtonDeleteHandler());
        btnEmail.setOnClickListener(new ButtonEmailHandler());
        btnViewOnDevice.setOnClickListener(new ButtonViewCSV());
        btnGraph.setOnClickListener(new ButtonViewGraph());
        btnBackToHome.setOnClickListener(new ButtonBackHomeHandler());

    }

    public class ButtonBackHomeHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Intent homeIntent = new Intent(ResultsActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    public class ButtonEmailHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            String subject = Patient.userID + " Test Results";
            String message = "Here are the results for " + Patient.userID;
            ArrayList<Uri> uris = new ArrayList<>();

            for(ResultsList results : resultFiles)
            {
                if(results.isChosen())
                {
                    System.out.println("CHOSEN");
                    String fullpath =  path + "/" + results.getFileName();
                    File file = new File(fullpath);

                    uris.add(Uri.parse("file://" + file.getAbsolutePath()));

                }
            }
            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            Intent mailer = Intent.createChooser(emailIntent, null);

            startActivity(mailer);
        }
    }


    public class ButtonViewCSV implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            int count = 0;
            for(ResultsList result : resultFiles)
            {
                if(result.isChosen())
                {
                    count++;
                }
            }

            for(ResultsList result : resultFiles)
            {
                if(count == 1)
                {
                    if(result.isChosen())
                    {
                        Intent csvIntent = new Intent(Intent.ACTION_VIEW);
                        File csvFile = new File(path + "/" + result.getFileName());
                        csvIntent.setDataAndType(Uri.fromFile(csvFile), "text/csv");

                        startActivity(csvIntent);
                    }

                }

            }
            if(count > 1)
            {
                Toast.makeText(ResultsActivity.this, "Please choose only one result", Toast.LENGTH_SHORT).show();
            }

            if(count == 0)
            {
                NoItemsSelectedDialog noItemsDialog = new NoItemsSelectedDialog();
                noItemsDialog.setCancelable(false);
                noItemsDialog.show(getFragmentManager(), "");
            }

        }
    }

    public class ButtonViewGraph implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            ReadingCSV readingAsync = new ReadingCSV(ResultsActivity.this); //start async task for reading the csv
            readingAsync.execute();

        }
    }

    private class ReadingCSV extends AsyncTask<Void, Void, ArrayList<PlottableObject>>
    {
        ProgressDialog dialog;

        public ReadingCSV(ResultsActivity activity) {

            dialog = new ProgressDialog(activity);
            if(!this.dialog.isShowing())
            {
//                this.dialog.show(); //showt the dialog
            }
            else
            {
                this.dialog.dismiss();
            }
        }
        @Override
        protected ArrayList<PlottableObject> doInBackground(Void... params)
        {
            ArrayList<PlottableObject> plottableTrialsList = new ArrayList<PlottableObject>();
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            for(ResultsList result : resultFiles)
            {
                if(result.isChosen())
                {

                    try {

                        br = new BufferedReader(new FileReader(new File(path + "/" + result.getFileName()).getAbsoluteFile()));
                        int iteration = 0;
                        while ((line = br.readLine()) != null)
                        {
                            if(iteration == 0)  // as to not do the title line of the csv
                            {
                                iteration++;
                                continue;
                            }

                            // use comma as separator
                            String[] data = line.split(cvsSplitBy);

                            Point position = new Point(Integer.valueOf(data[0]),Integer.valueOf(data[1])); //save the point data x and y
                            boolean wasHit = Boolean.valueOf(data[2]); //save the true or false if it was hit
                            PlottableObject individualTrial = new PlottableObject(position, wasHit); //create new flash stim (SUBJECT TO CHANGE)
                            individualTrial.hit = wasHit;   //set its hit bool to the boolean value from the csv
                            plottableTrialsList.add(individualTrial); //add to the arraylist of flash stims.

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            return plottableTrialsList;
        }

        @Override
        protected void onPostExecute(ArrayList<PlottableObject> results)
        {
            if (dialog.isShowing()) {
                //dialog.dismiss();
            }

            Intent graphIntent = new Intent(ResultsActivity.this, GraphActivity.class); //go to the output graph display

            graphIntent.putParcelableArrayListExtra("graphDataToPlot", results);

            startActivity(graphIntent);
        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setMessage("Generating Graph... Please wait");
            this.dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public class ListViewListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {


            if(!resultFiles.get(position).isChosen())
            {
                resultFiles.get(position).setChosen(true);
                view.setBackgroundColor(Color.argb(255,100,221,23));
            }
            else
            {
                //Remove Discrete unselected item highlight
                resultFiles.get(position).setChosen(false);
                view.setBackgroundColor(Color.TRANSPARENT);
            }

            ResultsList result = (ResultsList)parent.getAdapter().getItem(position);

            Log.d("RESULT NAME", result.getFileName());
            Log.d("RESULT BOOl", String.valueOf(result.isChosen()));

        }
    }

    public class ButtonDeleteHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {

            DeleteItemsDialog deleteDialog = new DeleteItemsDialog();
            deleteDialog.setCancelable(false);
            deleteDialog.show(getFragmentManager(), "");
        }
    }

    public void RemoveFiles()
    {
        ArrayList<ResultsList> resultsToRemove = new ArrayList<>();

        for(ResultsList result : resultFiles)
        {
            if(result.isChosen())
            {
                System.out.println(new File(path + "/" + result.getFileName()).getAbsoluteFile().delete());
                resultsToRemove.add(result);

            }
        }

        resultFiles.removeAll(resultsToRemove);
    }

    public void SetNewAdapter()
    {
        adapter = new CustomArrayAdapter(ResultsActivity.this, R.layout.listview_results, resultFiles);

        //set the listview adapter
        lvFiles.setAdapter(adapter);
    }

    public class CustomArrayAdapter extends ArrayAdapter<ResultsList>
    {


        public CustomArrayAdapter(Context context, int resource, List<ResultsList> objects) {
            super(context, resource, objects);
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(ResultsActivity.this);
            View v = inflater.inflate(R.layout.listview_results, parent, false);

            ResultsList listOfResults = getItem(position);

            String fileName = listOfResults.getFileName();

            String fileTypeName = GetTestTypeFileName(fileName);
            String dateOfFile = GetDateFromFileName(fileName);

            TextView tvFileName = (TextView)v.findViewById(R.id.tvResultName);
            TextView tvDate = (TextView)v.findViewById(R.id.tvDate);
            tvDate.setText(dateOfFile);
            tvFileName.setText(fileTypeName);

            return v;

        }

        public String GetTestTypeFileName(String fileName)
        {
            int firstDash = fileName.indexOf("_tool");

            String outputName = fileName.substring(firstDash + 5);
            int secondUnderscore = outputName.indexOf('_');
            String properOutputOfTestName = outputName.substring(0, secondUnderscore);

            return properOutputOfTestName;
        }

        public String GetDateFromFileName(String fileName)
        {
            //date
            int startOfDate = fileName.indexOf("_date");
            int endOfDate = fileName.indexOf("_time");
            String outputDate = fileName.substring(startOfDate + 5, endOfDate);
            outputDate = outputDate.replace('-', ' ');

            //time
            int startOfTime = fileName.indexOf("_time");
            int endOfTime = fileName.indexOf(".");
            String outputTime = fileName.substring(startOfTime + 5, endOfTime);
            outputTime = outputTime.replace('-', ':');

            String dateandtimeoutput = outputDate + "  " +  outputTime;


            return dateandtimeoutput;
        }
    }


    // On back key pressed go to start menu
    @Override
    public void onBackPressed()
    {
        Log.d("TAG", "onBackPressed Called");
        Intent setIntent = new Intent(ResultsActivity.this, HomeActivity.class);
        startActivity(setIntent);
    }
}
