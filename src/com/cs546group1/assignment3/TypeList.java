package com.cs546group1.assignment3;

import java.util.ArrayList;

import com.cs546group1.assignment3.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class TypeList extends ListActivity {
	
private ArrayList<String> path;

	public static final int SELECT_ID = Menu.FIRST;	
	
	public static final int BACK_ID = Menu.FIRST + 1;
	
	public static final String TYPE_NAME = "type";
	
	public static final int BUTTON_CANCEL = 0;
	public static final int BUTTON_SELECTED = 1;
	
	private ArrayList<String> types;
	
	/**
	 * onCreate() - make a new GUI to view directions
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_types);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.types = extras.getStringArrayList("types");
            this.arrange();
        }
        registerForContextMenu(getListView());
    }
    
    /**
     * Display the types.
     */
    public void arrange() {
    	ArrayAdapter<String> buildingTypes = new ArrayAdapter<String>(this, R.layout.info, this.types);
    	setListAdapter(buildingTypes);
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
     * onContextItemSelected() - delete the building selected from the context menu
     */
    public boolean onContextItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    		case SELECT_ID:
    			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    			
    			return true;
    		}
    	return super.onContextItemSelected(item);
    }
    
    /**
     * onListItemClick() - when the user clicks a building, show details in the building edit window.
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent();
        Bundle bundle = new Bundle();
		bundle.putInt("BUTTON", TypeList.BUTTON_SELECTED);
		bundle.putLong(TypeList.TYPE_NAME, id);
		i.putExtras(bundle);
        setResult(RESULT_OK, i);
        finish();
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
