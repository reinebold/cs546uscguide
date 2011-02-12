package com.cs546group1.assignment3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.cs546group1.assignment3.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;



/**
 * Main view for the map assignment.
 * @author Jay
 *
 */
public class Assignment3 extends MapActivity implements LocationListener {
	
	private static final int ACTIVITY_SELECT_TYPE = 0;
	private static final int ACTIVITY_SELECT_BUILDING = 1;
	private static final int ACTIVITY_DIRECTIONS = 2;
	private static final int ACTIVITY_TEXT_DIRECTIONS = 3;
	private static final int ACTIVITY_VIEW_EVENTS = 4;
	
	public static final int TYPE_ID = Menu.FIRST;
	public static final int EVENTS_ID = Menu.FIRST + 1;
	
	protected boolean doUpdates = true;
	protected MapView myMapView = null;
	protected Location myLocation = null;
	protected LocationManager myLocationManager = null;
	protected MapController myMapController = null;
	
	private MyLocationOverlay loc;
	
	private Campus campus;
	
	
    /**
     * Create the map view and set up gps for refresh.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.myLocation = new Location("gps");
        this.myMapView = new MapView(this, "0qrdk76GfZi_YY2I6B3RAyXXakcpK4by2T__x-w");
        this.myMapView.setClickable(true);
        this.setContentView(myMapView);
        this.myMapController = this.myMapView.getController();
        this.campus = new Campus(this, this);
        this.myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.gpsRefresh();
        
        List<Overlay> mapOverlays = this.myMapView.getOverlays();
        this.loc = new MyLocationOverlay(this, this.myMapView);
        this.loc.enableMyLocation();
        mapOverlays.add(this.loc);
    }
    
	/**
	 * Set the refersh rate:  25m change or 5000 millisecond timeout
	 */
	private void gpsRefresh() {
		final float MINIMUM_DISTANCECHANGE_FOR_UPDATE = 25;
		final long MINIMUM_TIME_BETWEEN_UPDATE = 5000;
		this.myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE,
				MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
	}
    
    /**
     * onCreateOptionsMenu() - allow the user to add buildings or get directions from options menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, TYPE_ID, 0, R.string.menu_directions);
        menu.add(0, EVENTS_ID, 1, R.string.menu_events);
        return result;
    }
    
    /**
     * onOptionsItemSelected() - add a building or get directions.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
        switch(item.getItemId()) {
        case TYPE_ID:
        	i = new Intent(this, TypeList.class);
        	ArrayList<String> types = campus.getBuildingTypes();
        	if (types != null) {
    			i.putStringArrayListExtra("types", types);
    		}
        	startActivityForResult(i, ACTIVITY_SELECT_TYPE);
        	return true;
        case EVENTS_ID:
        	i = new Intent(this, ViewEvents.class);
        	startActivityForResult(i, ACTIVITY_VIEW_EVENTS);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Respond to user key presses.
     * i = zoom in
     * o = zoom out
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_I) {
	    	this.myMapController.zoomIn();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_O) {
	    	this.myMapController.zoomOut();
            return true;
        }
        return false;
    }

    /**
     * Method not implemented.
     */
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * Update the map to a new location.
	 * @param l
	 */
	private void updateView(Location l) { 
		this.myLocation = l;
		List<Overlay> mapOverlays = this.myMapView.getOverlays();
		for(int i=0; i < mapOverlays.size(); i++) {
			if (mapOverlays.get(i) instanceof PathOverlay) {
				((PathOverlay)mapOverlays.get(i)).checkCovered(this.myLocation);
			}
		}
	}
	
	/**
     * onActivityResult() - respond to completed activities.
     * Either:  add a building, give directions, or do nothing (cancel).
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        int buttonCode;
        switch(requestCode) {
        case ACTIVITY_VIEW_EVENTS:
        	break;
        case ACTIVITY_SELECT_TYPE:
        	buttonCode = extras.getInt("BUTTON");
        	if (buttonCode == TypeList.BUTTON_CANCEL) {
        		
        	} else {
        		long selection = extras.getLong(TypeList.TYPE_NAME);
        		Intent i = new Intent(this, BuildingList.class);
        		ArrayList<String> types = campus.getBuildingsWithType(selection);
        		i.putStringArrayListExtra("buildings", types);
        		i.putExtra(TypeList.TYPE_NAME, selection);
        		startActivityForResult(i, ACTIVITY_SELECT_BUILDING);
        	}
        	break;
        case ACTIVITY_SELECT_BUILDING:
        	buttonCode = extras.getInt("BUTTON");
        	if (buttonCode == TypeList.BUTTON_CANCEL){
        		
        	} else {
        		long building = extras.getLong(BuildingList.BUILDING_CODE);
        		long type = extras.getLong(TypeList.TYPE_NAME);
        		String builCode = this.campus.getCodeWithId(type, building);
        		Intent i = new Intent(this, GetDirections.class);
        		if (builCode != null) {
        			i.putExtra(BuildingList.BUILDING_CODE, builCode);
        			startActivityForResult(i, ACTIVITY_DIRECTIONS);
        		}
        	}
        	break;
        case ACTIVITY_DIRECTIONS:
        	buttonCode = extras.getInt("BUTTON");
        	if (buttonCode == GetDirections.BUTTON_CANCEL) {
        		//do nothing
        	} else if (buttonCode == GetDirections.BUTTON_TEXT) {
        		//display list of text directions
        		List<Overlay> mapOverlays = this.myMapView.getOverlays();
        		mapOverlays.clear();
        		mapOverlays.add(this.loc);
        		Intent i = new Intent(this, TextDirections.class);
        		ArrayList<String> dirs = campus.getTextDirections(extras.getString(GetDirections.CODE_NAME));
        		if (dirs != null) {
        			i.putStringArrayListExtra("path", campus.getTextDirections(extras.getString(GetDirections.CODE_NAME)));
        		}
            	startActivityForResult(i, ACTIVITY_TEXT_DIRECTIONS);
        	} else if (buttonCode == GetDirections.BUTTON_VISUAL){
        		//display visual directions
        		List<Overlay> mapOverlays = this.myMapView.getOverlays();
        		mapOverlays.clear();
        		mapOverlays.add(this.loc);
        		ArrayList<Integer> points = campus.getPoints(extras.getString(GetDirections.CODE_NAME));
        		if (points != null) {
        			GeoPoint g1 = null;
        			GeoPoint g2 = null;
        			//draw paths
        			if (points.size() % 2 == 0) {
        				for(int i=0; i < points.size(); i += 2) {
        					if (g1 == null) {
        						g1 = new GeoPoint(points.get(i), points.get(i + 1));
        					} else if (g2 == null) {
        						g2 = new GeoPoint(points.get(i), points.get(i + 1));
        						mapOverlays.add(new PathOverlay(g1, g2));
        						g1 = g2;
        						g2 = null;
        					}
        				}
        			}
        		}
        	} else {
        		//do nothing
        	}
        	break;
        }
        
    }

    /**
     * On GPS activity, update map.
     */
	public void onLocationChanged(Location location) {
		this.updateView(location);
	}

	/**
	 * Method not implemented.
	 */
	public void onProviderDisabled(String provider) {
	}

	/**
	 * Method not implemented.
	 */
	public void onProviderEnabled(String provider) {
		
	}

	/**
	 * Method not implemented.
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}