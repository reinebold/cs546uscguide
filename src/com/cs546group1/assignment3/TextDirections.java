package com.cs546group1.assignment3;


import java.util.ArrayList;

import com.cs546group1.assignment3.R;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

/**
 * List UI for display steps to move across campus.
 * @author Jay
 *
 */
public class TextDirections extends ListActivity {
	
	private ArrayList<String> path;
	
	public static final int BACK_ID = Menu.FIRST;
	
	/**
	 * onCreate() - make a new GUI to view directions
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_text_directions);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	this.path = extras.getStringArrayList("path");
        	this.arrange();
        }
        registerForContextMenu(getListView());
    }
    
    /**
     * Display the steps.
     */
    public void arrange() {
    	ArrayAdapter<String> steps = new ArrayAdapter<String>(this, R.layout.info, path);
    	setListAdapter(steps);
    }
    
    /**
     * Make an options menu with the ability to go back.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, BACK_ID, 0, R.string.menu_back);
        return result;
    }
    
    /**
     * Handle returning back to the main map.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
    	Bundle bundle = new Bundle();
    	Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        switch(item.getItemId()) {
        case BACK_ID:
        	finish();
        	return true;
        }
        finish();
        return true;   
    }

}
