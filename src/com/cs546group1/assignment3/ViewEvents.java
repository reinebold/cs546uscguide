package com.cs546group1.assignment3;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Access the USC webpage and list all events.
 * @author Jay
 *
 */
public class ViewEvents extends ListActivity {
	
	private ArrayList<Event> events;
	
	public static final int BACK_ID = Menu.FIRST;
	
	public static final int ACTIVITY_VIEW_EVENT = 11;
	
	/**
	 * onCreate() - make a new GUI to view events
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_events);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	
        }
        registerForContextMenu(getListView());
        events = new ArrayList<Event>();
        this.accessWeb();
        this.arrange();
    }
    
    /**
     * Display the buildings
     */
    public void arrange() {
    	ArrayAdapter<String> names =  new ArrayAdapter<String>(this, R.layout.info, this.getEventNames());
    	setListAdapter(names);
    }
    
    public ArrayList<String> getEventNames() {
    	ArrayList<String> names = new ArrayList<String>();
    	for(int i=0; i < this.events.size(); i++) {
    		names.add(this.events.get(i).getName());
    	}
    	return names;
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
    
    /**
     * onListItemClick() - when the user clicks an event, show it the details
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EventView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("BUTTON", TypeList.BUTTON_SELECTED);
        bundle.putString("name", this.events.get(position).getName());
        bundle.putString("date", this.events.get(position).getDate());
        bundle.putString("summary", this.events.get(position).getSummary());
        setResult(RESULT_OK, i);
        i.putExtras(bundle);
        finish();
    }
    
    public void accessWeb(){
        String url = "http://web-app.usc.edu/ws/eo2/calendar/32/list/today";
        ArrayList<String> content = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        try{
            HttpResponse response = client.execute(request);
            content = HTTPHelper.request(response);
        }catch(Exception ex){
            System.out.println("could not access internet ;_;");
        }


        if (content != null) {
        	for(int i=0; i < content.size(); i++) {
        		if (content.get(i).contains("<a class=\"feed_title\"")) {
        			String name = content.get(i);
        			name = findContent(name, "a");
        			String date = content.get(i + 2);
        			date = findContent(date, "div");
        			String summary = content.get(i + 4);
        			summary = findContent(summary, "p");
        			System.out.println(name);
        			System.out.println(date);
        			System.out.println(summary);
        			System.out.println();
        			Event e = new Event(name, date, summary);
        			this.events.add(e);
        		}
        	}
        }
    }
    
    public static String findContent(String oneLine, String simbol)
    {
    	int start = oneLine.indexOf(">", oneLine.indexOf("<" + simbol) + 1) + 1;
    	int end = oneLine.indexOf("</" + simbol);
    	String result = oneLine.substring(start, end);
    	for(;;)
    	{
    		int deleteStart = result.indexOf("<");
    		if (deleteStart == -1) break;
    		int deleteEnd = result.indexOf(">", deleteStart + 1);
    		String text1 = new String(""), text2 = new String("");
    		if (deleteStart >1) text1 = result.substring(0, deleteStart);
    		if (deleteEnd + 1 < result.length()) text2 = result.substring(deleteEnd + 1, result.length());
    		result = text1 + text2;
    	}
    	return result;
    }
}