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


public class ViewEvents extends ListActivity {
	
	private ArrayList<Event> events;
	
	public static final int BACK_ID = Menu.FIRST;
	
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
    
    /**
     * onListItemClick() - when the user clicks an event, show it the details
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, EventView.class);
        i.putExtra("name", this.events.get(position).getName());
        i.putExtra("date", this.events.get(position).getDate());
        i.putExtra("summary", this.events.get(position).getSummary());
        //startActivityForResult(i, ACTIVITY_EDIT); 
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


        for(int i=0; i < content.size(); i++) {
        	if (content.get(i).contains("<a class=\"feed_title\"")) {
        		String name = content.get(i);
        		name = name.substring(name.indexOf('>') + 1, name.indexOf('<', name.indexOf('>')));
        		String date = content.get(i + 2);
        		date = date.substring(date.indexOf('>') + 1, date.indexOf('<', date.indexOf('>')));
        		String summary = content.get(i + 4);
        		summary = summary.substring(summary.indexOf('>') + 1, summary.indexOf('<', summary.indexOf('>')));
        		//System.out.println(name);
        		//System.out.println(date);
        		//System.out.println(summary);
        		//if (!name.equals("")) {
        			Event e = new Event(name, date, summary);
        			this.events.add(e);
        		//}
        	}
        }
    }

}
