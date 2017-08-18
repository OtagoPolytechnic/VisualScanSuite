package bit.harrl7.visscan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    ListView lvFiles;
    ArrayList<ResultsList> resultFiles;
    String path;
    CustomArrayAdapter adapter;
    Button btnDeleteResults;
    Button btnEmail;
    Button btnViewOnDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //set up buttons
        btnDeleteResults = (Button)findViewById(R.id.btnDeleteResults);
        btnEmail = (Button)findViewById(R.id.btnSaveToEmail);
        btnViewOnDevice = (Button)findViewById(R.id.btnViewDevice);

        //set listview
        lvFiles = (ListView)findViewById(R.id.lvFiles);

        //create results array list
        resultFiles = new ArrayList<>();

        //get path
        path = Environment.getExternalStorageDirectory().toString()+"/VisScan";
        //get directory
        File directory = new File(path);

        //get all files in the directory
        File[] files = directory.listFiles();

        //loop over files adding to array list of Results
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].getName().startsWith(Patient.userID))
            {
                resultFiles.add(new ResultsList(files[i].getName(), false));
            }

        }


        //set adapter and pass it the list of tools and a custom layout (listviewlayout.xml)
        adapter = new CustomArrayAdapter(ResultsActivity.this, R.layout.listview_results, resultFiles);

        //set the listview adapter
        lvFiles.setAdapter(adapter);

        //set on click listeners
        lvFiles.setOnItemClickListener(new ListViewListener());
        btnDeleteResults.setOnClickListener(new ButtonDeleteHandler());
        btnEmail.setOnClickListener(new ButtonEmailHandler());
        btnViewOnDevice.setOnClickListener(new ButtonViewCSV());

    }

    public class ButtonEmailHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            //TODO actually get it firing off emails
            Toast.makeText(ResultsActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();

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




            //String fullpath =  path + "/" + resultFiles.get(0).getFileName();
            //File file = new File(fullpath);
            //Uri uri = Uri.parse("file://" + file.getAbsolutePath());

            //emailIntent .putExtra(Intent.EXTRA_STREAM, uri);

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

        }
    }

    public class ListViewListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //get the string of the listview item clicked
            String selectedFile = parent.getItemAtPosition(position).toString();
            Log.d("SELECTED", selectedFile);
            if(!resultFiles.get(position).isChosen())
            {
                resultFiles.get(position).setChosen(true);
                view.setBackgroundColor(Color.GREEN);
            }
            else
            {
                //Remove Discrete unselected item highlight
                resultFiles.get(position).setChosen(false);
                view.setBackgroundColor(Color.TRANSPARENT);
            }

            Intent csvIntent = new Intent(Intent.ACTION_VIEW);

            ResultsList result = (ResultsList)parent.getAdapter().getItem(position);

            Log.d("RESULT NAME", result.fileName);
            Log.d("RESULT BOOl", String.valueOf(result.chosen));

            //File csvFile = new File(path + "/" + selectedFile);
            //csvIntent.setDataAndType(Uri.fromFile(csvFile), "text/csv");

            //startActivity(csvIntent);

        }
    }

    public class ButtonDeleteHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            createAndShowAlertDialog();
        }
    }


    //this method creates a simple dialog, confirming if the user wants to delete the files or not.
    private void createAndShowAlertDialog()
    {
        //create the builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);

        //set the message
        builder.setMessage("Are you sure you want to delete these files?");

        //set the positive button
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                RemoveFiles();
                SetNewAdapter();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                //do nothing
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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
            TextView tvFileName = (TextView)v.findViewById(R.id.tvResultName);
            tvFileName.setText(fileName);

            return v;

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
