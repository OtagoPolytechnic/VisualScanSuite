package bit.harrl7.visscan.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import bit.harrl7.visscan.R;

public class ToolListActivity extends AppCompatActivity {

    ListView lvTools;
    ImageButton backToHomeButton;
    String[] tools = {"Flash", "Wander", "Walking Diagonal", "Zig Zag", "Shape Cancellation", "Contrast", "Directional", "Line Bisection"};  //TO BE ENUMS?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_list);

        //get ref to listview
        lvTools = (ListView)findViewById(R.id.lvTools);
        backToHomeButton = (ImageButton)findViewById(R.id.imgbtnBackHome);

        backToHomeButton.setOnClickListener(new BackToHomeButtonClickHandler());

        //set adapter and pass it the list of tools and a custom layout (listviewlayout.xml)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listviewlayout, tools);

        //set the listview adapter
        lvTools.setAdapter(adapter);

        //set the onClickListener for the listview
        lvTools.setOnItemClickListener(new ListViewListener());
    }

    public class BackToHomeButtonClickHandler implements ImageButton.OnClickListener
    {

        @Override
        public void onClick(View v)
        {
            Intent homeIntent = new Intent(ToolListActivity.this, HomeActivity.class);
            startActivity(homeIntent);
        }
    }

    public class ListViewListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            //get the string of the listview item clicked
            String selectedTool = parent.getItemAtPosition(position).toString();

            //Show the fragment with the header of the selected tool
            ShowToolFrag(selectedTool);

        }
    }

    public void ShowToolFrag(String toolName)
    {
        //Create the fragment
        Fragment toolDescriptionFrag = new DisplayToolFrag();

        //Bundle up the name of the tool
        Bundle bundle = new Bundle();
        bundle.putString("toolname", toolName);

        //give the name to the fragment.
        toolDescriptionFrag.setArguments(bundle);

        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        if(toolDescriptionFrag != null) { ft.replace(R.id.fragment_container, toolDescriptionFrag); }
        ft.commit();

    }

    // On back key pressed go to start menu
    @Override
    public void onBackPressed()
    {
        Log.d("TAG", "onBackPressed Called");
        Intent setIntent = new Intent(ToolListActivity.this, HomeActivity.class);
        startActivity(setIntent);
    }
}
