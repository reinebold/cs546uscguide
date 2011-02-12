package com.cs546group1.assignment3;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BuildingList extends ListActivity {
	
	public static final int SELECT_ID = Menu.FIRST;	
	public static final int BACK_ID = Menu.FIRST + 1;
	
	public static final String BUILDING_CODE = "code";
	
	private ArrayList<String> buildings;

	
	/**
	 * onCreate() - make a new GUI to view directions
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_buildings);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	this.buildings = extras.getStringArrayList("buildings");
            this.arrange();
        }
        registerForContextMenu(getListView());
    }
    
    /**
     * Display the buildings
     */
    public void arrange() {
    	ArrayAdapter<String> buildingTypes = new ArrayAdapter<String>(this, R.layout.info, this.buildings);
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
     * onListItemClick() - when the user clicks a building, show details in the building edit window.
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent();
        Bundle bundle = new Bundle();
		bundle.putInt("BUTTON", TypeList.BUTTON_SELECTED);
		bundle.putLong(BuildingList.BUILDING_CODE, id);
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
