package bit.harrl7.visscan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GraphShow extends AppCompatActivity {

    Button btnBack;
    Button btnEmail;
    String wholeFilePath;
    float upperRightCount;
    float upperLeftCount;
    float lowerRightCount;
    float lowerLeftCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_show);

        //to get bitmap to draw to imageview
        byte[] byteArray = getIntent().getByteArrayExtra("Graph");
        DecimalFormat twoD = new DecimalFormat(".##");

        upperRightCount = getIntent().getFloatExtra("upperRight", -1f) * 100.0f;
        upperLeftCount = getIntent().getFloatExtra("upperLeft", -1f)* 100.0f;
        lowerRightCount = getIntent().getFloatExtra("lowerRight", -1f)* 100.0f;
        lowerLeftCount = getIntent().getFloatExtra("lowerLeft", -1f)* 100.0f;
        wholeFilePath = getIntent().getStringExtra("fileName");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imgGraph = (ImageView)findViewById(R.id.imgGraph);
        imgGraph.setImageBitmap(bmp);


        TextView tvUpperRight = (TextView)findViewById(R.id.tvUpperRight);
        TextView tvUpperLeft = (TextView)findViewById(R.id.tvUpperLeft);
        TextView tvLowerRight = (TextView)findViewById(R.id.tvLowerRight);
        TextView tvLowerLeft = (TextView)findViewById(R.id.tvLowerLeft);

        String upperRightPercentage = String.valueOf(twoD.format(upperRightCount)) + "%";
        String upperLeftPercentage = String.valueOf(twoD.format(upperLeftCount)) + "%";
        String lowerLeftPercentage = String.valueOf(twoD.format(lowerLeftCount)) + "%";
        String lowerRightPercentage = String.valueOf(twoD.format(lowerRightCount)) + "%";

        tvUpperRight.setText(upperRightPercentage);
        tvUpperLeft.setText(upperLeftPercentage);
        tvLowerLeft.setText(lowerLeftPercentage);
        tvLowerRight.setText(lowerRightPercentage);

        btnBack = (Button)findViewById(R.id.btnBackToResults);
        btnEmail = (Button)findViewById(R.id.btnSendEmail);

        btnBack.setOnClickListener(new ButtonBackHandler());
        btnEmail.setOnClickListener(new ButtonEmailHandler());

    }

    public class ButtonBackHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Intent backToResults = new Intent(GraphShow.this, ResultsActivity.class);
            startActivity(backToResults);

        }
    }

    public class ButtonEmailHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            DecimalFormat twoD = new DecimalFormat(".##");

            String subject = "Graph for " + Patient.userID;
            String message = "Upper Right Hits: " + twoD.format(upperRightCount) + "% \n" + "Upper Left Hits: " + twoD.format(upperLeftCount) + "% \n" + "Lower Left Hits: " + twoD.format(lowerLeftCount) +
                    "% \n" + "Lower Right Hits: " + twoD.format(lowerRightCount) + "%";

            String docsFolder = Environment.getExternalStorageDirectory().toString();
            File resultsFolder = new File(docsFolder +"/Graphs");
            if(!resultsFolder.exists())
            {
                resultsFolder.mkdirs();
            }
            String pathofBmp = resultsFolder+ "/" + wholeFilePath;
            File pictureOfGraphURI = new File(pathofBmp);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("image/*");
            //attach the file to the intent
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pictureOfGraphURI));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);


            Intent mailer = Intent.createChooser(emailIntent, null);

            startActivity(mailer);

        }
    }

    @Override
    public void onBackPressed()
    {
        Intent backToResults = new Intent(GraphShow.this, ResultsActivity.class);
        startActivity(backToResults);

    }
}
