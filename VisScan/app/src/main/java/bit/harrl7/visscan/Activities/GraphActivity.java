package bit.harrl7.visscan.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bit.harrl7.visscan.Graphing.GraphShow;
import bit.harrl7.visscan.Patient;
import bit.harrl7.visscan.Graphing.PlottableObject;

public class GraphActivity extends AppCompatActivity {

    public ArrayList<PlottableObject> trialsToShow;
    // Screen bounds for stim placement
    public Point bounds;
    Paint paint;
    int upperLeftCount = 0;
    int upperRightCount = 0;
    int missedUpperLeft = 0;
    int missedUpperRight = 0;
    int missedLowerLeft = 0;
    int missedLowerRight = 0;
    int lowerLeftCount = 0;
    int lowerRightCount = 0;
    float percentageUpperLeft;
    float percentageUpperRight;
    float percentageLowerLeft;
    float percentageLowerRight;
    File resultsFolder;
    String wholeFileName;
    FileOutputStream outputStream;
    SimpleDateFormat df;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<PlottableObject> lists = this.getIntent().getParcelableArrayListExtra("graphDataToPlot"); //get the parcelable array list of flash stims

        //code to get the width and height of screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        bounds =  new Point(width, height); //set a point variable with the bounds of the screen

        trialsToShow = lists; //set the array list to trialsToSHow

        CanvasView canvasView = new CanvasView(this);

        setContentView(canvasView);


    }

    private class CanvasView extends View {
        public CanvasView(Context context) {
            super(context);

            setDrawingCacheEnabled(true);
            paint = new Paint();
            String docsFolder = Environment.getExternalStorageDirectory().toString();
            resultsFolder = new File(docsFolder +"/Graphs");
            df = new SimpleDateFormat("dd-MMM-yy_HH:mm:ss");
            date = new Date();
            String fileTimeStamp = df.format(date);
            wholeFileName = Patient.getUserID() + "_" + Patient.getDOB() + "-" +  fileTimeStamp + ".jpg";

            try
            {
                outputStream = new FileOutputStream(resultsFolder + "/" + wholeFileName);
            }
            catch(FileNotFoundException e)
            {
                Log.e("FIle not found", "NOT FOUND");
            }



            CheckQuadrant();

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // custom drawing code here

            paint.setStyle(Paint.Style.FILL);

            // make the entire canvas white
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);

            paint.setAntiAlias(true);

            //loop over the points and add to canvas.
            for(PlottableObject t : trialsToShow)
            {
                Float xPos = (float)t.pos.x / 100 * bounds.x;
                Float yPos = (float)t.pos.y /100 * bounds.y;

                //if missed, draw red.. subject to change
                if(!t.hit)
                {

                    paint.setColor(Color.RED);
                    paint.setAlpha(150);
                    canvas.drawCircle(xPos,yPos,50f, paint);
                }
                else
                {
                    //draw white circles
                    paint.setColor(Color.WHITE);
                    paint.setAlpha(200);
                    canvas.drawCircle(xPos,yPos,50f, paint);
                }

            }

            //draws the intersecting lines
            paint.setColor(Color.WHITE);
            canvas.drawLine((float)(bounds.x /2),0.0f, (float)(bounds.x/2),(float)(bounds.y), paint);
            canvas.drawLine(0.0f,(float)(bounds.y/2), (float)(bounds.x),(float)(bounds.y /2), paint);

            try
            {
                getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //outputs the canvas to jpg in graphs folder

            } catch (Exception e) {
                Log.e("Didn't save.", e.toString());
            }

            GoToNextActivity();

            //if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M)
               // canvas.restore();
        }

        public void GoToNextActivity()
        {
            // Outer folder, make if doesn't exist already

            if(!resultsFolder.exists())
            {
                resultsFolder.mkdirs();
            }

            Bitmap graphToShow;

            graphToShow = BitmapFactory.decodeFile(resultsFolder+ "/" + wholeFileName);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            graphToShow.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent graphImageViewShow = new Intent(GraphActivity.this, GraphShow.class);
            graphImageViewShow.putExtra("Graph", byteArray);
            graphImageViewShow.putExtra("fileName", wholeFileName);

            graphImageViewShow.putExtra("upperRight", percentageUpperRight);
            graphImageViewShow.putExtra("upperLeft", percentageUpperLeft);
            graphImageViewShow.putExtra("lowerLeft", percentageLowerLeft);
            graphImageViewShow.putExtra("lowerRight", percentageLowerRight);
            startActivity(graphImageViewShow);
        }

        public void CheckQuadrant()
        {

            for(PlottableObject f : trialsToShow) //loop over the plottable points and check which corner theyre in.
            {
                Float xPosition = (float)f.pos.x / 100 * bounds.x;
                Float yPosition = (float)f.pos.y /100 * bounds.y;

                if(f.hit)
                {
                    if((xPosition > (bounds.x/2)) && (yPosition < (bounds.y/2)))
                    {
                        upperRightCount++;
                    }
                    if((xPosition < (bounds.x/2)) && (yPosition < (bounds.y/2)))
                    {
                        upperLeftCount++;
                    }
                    if((xPosition < (bounds.x/2)) && (yPosition > (bounds.y/2)))
                    {
                        lowerLeftCount++;
                    }
                    if((xPosition > (bounds.x/2)) && (yPosition > (bounds.y/2)))
                    {
                        lowerRightCount++;
                    }
                }
                else
                {
                    if((xPosition > (bounds.x/2)) && (yPosition < (bounds.y/2)))
                    {
                        missedUpperRight++;
                    }
                    if((xPosition < (bounds.x/2)) && (yPosition < (bounds.y/2)))
                    {
                        missedUpperLeft++;
                    }
                    if((xPosition < (bounds.x/2)) && (yPosition > (bounds.y/2)))
                    {
                        missedLowerLeft++;
                    }
                    if((xPosition > (bounds.x/2)) && (yPosition > (bounds.y/2)))
                    {
                        missedLowerRight++;
                    }
                }

            }

           SetPercentages();
        }


        public void SetPercentages()
        {
            if(upperRightCount > 0)
            {
                percentageUpperRight = (float)(upperRightCount / (upperRightCount + (double)missedUpperRight));
            }
            else
            {
                percentageUpperRight = 0.0f;
            }

            if(upperLeftCount > 0)
            {
                percentageUpperLeft = (float)(upperLeftCount / (upperLeftCount + (double)missedUpperLeft));
            }
            else
            {
                percentageUpperLeft = 0.0f;
            }

            if(lowerRightCount > 0)
            {
                percentageLowerRight = (float)(lowerRightCount / (lowerRightCount + (double)missedLowerRight));
            }
            else
            {
                percentageLowerLeft = 0.0f;
            }

            if(lowerLeftCount > 0)
            {
                percentageLowerLeft = (float)(lowerLeftCount / (lowerLeftCount + (double)missedLowerLeft));
            }
            else
            {
                percentageLowerLeft = 0.0f;
            }
        }

    }






}
