package com.cs546group1.assignment3;

import com.cs546group1.assignment3.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class EventView extends Activity {
	
	public static final int BACK_ID = Menu.FIRST;
	
	public static final int VIEW_EVENT_DETAILS = 10;
	
	private TextView mName;
	private TextView mDate;
	private TextView mSummary;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String name = "error";
		String date = "error";
		String summary = "error";
		
		setContentView(R.layout.view_event);
        setTitle("Event Details");
		
		mName = (TextView) findViewById(R.id.name);
        mDate = (TextView) findViewById(R.id.date);
        mSummary = (TextView) findViewById(R.id.summary);
		
        if (extras != null) {
        	name = extras.getString("name");
        	date = extras.getString("date");
        	summary = extras.getString("summary");
        }
        
        mName.setText(name);
        mDate.setText(date);
        mSummary.setText(summary);
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
    		bundle.putInt("BUTTON", TypeList.BUTTON_CANCEL);
    		mIntent.putExtras(bundle);
    		setResult(RESULT_OK, mIntent);
    		finish();
    		return true;
    	}
    	finish();
    	return true;   
	}
}
