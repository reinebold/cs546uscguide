package com.cs546group1.assignment3;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import java.util.Calendar;



/**
 * Main view for the map assignment.
 * @author Jay
 *
 */
public class Assignment3 extends MapActivity implements LocationListener, OnInitListener, SensorEventListener {
	
	private static final int ACTIVITY_SELECT_TYPE = 0;
	private static final int ACTIVITY_SELECT_BUILDING = 1;
	private static final int ACTIVITY_DIRECTIONS = 2; 
	private static final int ACTIVITY_TEXT_DIRECTIONS = 3;
	private static final int ACTIVITY_VIEW_EVENTS = ViewEvents.ACTIVITY_VIEW_EVENT;
	private static final int ACTIVITY_CREATE = 5;
	private static final int MY_DATA_CHECK_CODE = 6;
	private static final int VIEW_EVENT_DETAILS = EventView.VIEW_EVENT_DETAILS;
	
	public static final int TYPE_ID = Menu.FIRST;
	public static final int EVENTS_ID = Menu.FIRST + 1;
	
	protected boolean doUpdates = true;
	protected MapView myMapView = null;
	protected Location myLocation = null;
	protected LocationManager myLocationManager = null;
	protected MapController myMapController = null;
	
	
	private Location currentBuilding;
	private ArrayList<Location> locationsToVisit;
	private ArrayList<String> namesToSay;
	
	private MyLocationOverlay loc;
	
	private Campus campus;
	
	private TextToSpeech tts = null;
	
	private SensorManager mySensorManager;
	
	private CompassOverlay compass;
	
	private Calendar myCalendar;
	
	private long lastTime = 0;
	
	
    /**
     * Create the map view and set up gps for refresh.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.myLocation = new Location("gps");
        this.myMapView = new MapView(this, "0qrdk76GfZi_YY2I6B3RAyXXakcpK4by2T__x-w");
        this.myMapView.setClickable(true);
        this.setContentView(myMapView);
        this.myMapView.setBuiltInZoomControls(true);
        this.myMapController = this.myMapView.getController();
        this.myCalendar = Calendar.getInstance();
        this.campus = new Campus(this, this);
        this.myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.gpsRefresh();
        
        List<Overlay> mapOverlays = this.myMapView.getOverlays();
        this.loc = new MyLocationOverlay(this, this.myMapView);
        this.loc.enableMyLocation();
        mapOverlays.add(this.loc);
        
        //install or turn on tts
        Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		//make gps overlay
        compass = new CompassOverlay("");
        //set up sensors
        mapOverlays.add(this.compass);
    	mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(this, mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        
        myMapController.stopPanning();
        GeoPoint p = new GeoPoint((int) (34.0217094421 * 1E6), (int) (-118.2828216553 * 1E6));
        myMapController.animateTo(p);
    }
    
	/**
	 * Set the refresh rate:  25m change or 5000 millisecond timeout
	 */
	private void gpsRefresh() {
		final float MINIMUM_DISTANCECHANGE_FOR_UPDATE = 25;
		final long MINIMUM_TIME_BETWEEN_UPDATE = 5000;
		this.myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE,
				MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
	}
	
	protected void onStop() {
		super.onStop();
		this.currentBuilding = null;
		List<Overlay> mapOverlays = this.myMapView.getOverlays();
		mapOverlays.clear();
		mapOverlays.add(this.loc);
		mapOverlays.add(this.compass);
		this.namesToSay = null;
		this.locationsToVisit = null;
	}

	
	void draw() {
		
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
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_I) {
	    //	this.myMapController.zoomIn();
	    //	this.myMapView.invalidate();
        //    return true;
        //} else if (keyCode == KeyEvent.KEYCODE_O) {
	    //	this.myMapController.zoomOut();
	    //	this.myMapView.invalidate();
        //    return true;
        //}
        //return false;
    //}

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
		int pathOverlayIndex = 0;
		List<Overlay> mapOverlays = this.myMapView.getOverlays();
		for(int i=0; i < mapOverlays.size(); i++) {
			if (mapOverlays.get(i) instanceof PathOverlay) {
				pathOverlayIndex += 1;
				int colorBefore = ((PathOverlay)mapOverlays.get(i)).getColor();
				((PathOverlay)mapOverlays.get(i)).checkCovered(this.myLocation);
				int colorAfter = ((PathOverlay)mapOverlays.get(i)).getColor();
				if (colorBefore != colorAfter) {
					//speak
					this.adjustNextBuilding(pathOverlayIndex);
					return;
				}
			}
		}
	}
	
	/**
	 * Figure out what building is the next to say and the next to compare current location to.
	 * If we are at the end of the list, then say "finished" and stop comparing direction.
	 * @param pathOverlayIndex
	 */
	private void adjustNextBuilding(int pathOverlayIndex) {
		if (pathOverlayIndex == this.locationsToVisit.size() - 1) {
			this.currentBuilding = null;
			if (this.namesToSay != null) {
				Speak("You have arrived at the destination.");
			}
		} else {
			this.currentBuilding = this.locationsToVisit.get(pathOverlayIndex + 1);
			if (this.namesToSay != null) {
				Speak("Walk to " + this.namesToSay.get(pathOverlayIndex + 1));
			}
		}
	}

	/**
     * onActivityResult() - respond to completed activities.
     * Either:  add a building, give directions, or do nothing (cancel).
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        Bundle extras = null;
        if (intent != null) {
        	extras = intent.getExtras();
        }
        int buttonCode;
        switch(requestCode) {
        case VIEW_EVENT_DETAILS:
        	break;
        case ACTIVITY_VIEW_EVENTS:
        	buttonCode = extras.getInt("BUTTON");
    		if (buttonCode == TypeList.BUTTON_CANCEL) {
    			
    		} else {
    			Intent viewIntent = new Intent(this, EventView.class);
    			viewIntent.putExtra("name", extras.getString("name"));
    			viewIntent.putExtra("date", extras.getString("date"));
    			viewIntent.putExtra("summary", extras.getString("summary"));
    			startActivityForResult(viewIntent, VIEW_EVENT_DETAILS);
    		}
        	break;
        case MY_DATA_CHECK_CODE:
    		if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
    			// success, create the TTS instance
    			tts = new TextToSpeech(this, this);
    		} 
    		else {
    			// missing data, install it
    			Intent installIntent = new Intent();
    			installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
    			startActivity(installIntent);
    		}
        	break;
        case ACTIVITY_CREATE:
        	break;
        case ACTIVITY_SELECT_TYPE:
        	if (extras != null && extras.containsKey("BUTTON")) {
        		buttonCode = extras.getInt("BUTTON");
        		if (buttonCode == TypeList.BUTTON_CANCEL) {
        		
        		} else {
        			long selection = extras.getLong(TypeList.TYPE_NAME);
        			Intent inte = new Intent(this, BuildingList.class);
        			ArrayList<String> types = campus.getBuildingsWithType(selection);
        			inte.putStringArrayListExtra("buildings", types);
        			inte.putExtra(TypeList.TYPE_NAME, selection);
        			startActivityForResult(inte, ACTIVITY_SELECT_BUILDING);
        		}
        	}
        	break;
        case ACTIVITY_SELECT_BUILDING:
        	if (extras != null) {
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
        	}
        	break;
        case ACTIVITY_DIRECTIONS:
        	//this.myCalendar = Calendar.getInstance();
        	//this.lastTime = this.myCalendar.getTimeInMillis();
        	if (extras != null) {
        		buttonCode = extras.getInt("BUTTON");
        		if (buttonCode == GetDirections.BUTTON_CANCEL) {
        			//do nothing
        		} else if (buttonCode == GetDirections.BUTTON_TEXT) {
        			//display list of text directions
        			List<Overlay> mapOverlays = this.myMapView.getOverlays();
        			mapOverlays.clear();
        			mapOverlays.add(this.loc);
        			mapOverlays.add(this.compass);
        			this.locationsToVisit = null;
        			this.namesToSay = null;
        			this.currentBuilding = null;
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
        			mapOverlays.add(this.compass);
        			ArrayList<Integer> points = campus.getPoints(extras.getString(GetDirections.CODE_NAME));
        			this.locationsToVisit = campus.getBuildingsToVisit(extras.getString(GetDirections.CODE_NAME));
        			this.namesToSay = null;
        			this.currentBuilding = null;
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
        			if (this.locationsToVisit.size() > 1) {
        				this.currentBuilding = this.locationsToVisit.get(1);
        			} else {
        				this.currentBuilding = this.locationsToVisit.get(0);
        			}
        		} else if (buttonCode == GetDirections.BUTTON_AUDIO){
        			//display visual directions
        			List<Overlay> mapOverlays = this.myMapView.getOverlays();
        			mapOverlays.clear();
        			mapOverlays.add(this.loc);
        			mapOverlays.add(this.compass);
        			this.currentBuilding = null;
        			ArrayList<Integer> points = campus.getPoints(extras.getString(GetDirections.CODE_NAME));
        			this.namesToSay = campus.getNamesOfPoints(extras.getString(GetDirections.CODE_NAME));
        			this.locationsToVisit = campus.getBuildingsToVisit(extras.getString(GetDirections.CODE_NAME));
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
        			Speak("Start from " + this.namesToSay.get(0));
        			if (this.namesToSay.size() > 1) {
        				Speak("Walk to " + this.namesToSay.get(1));
        				this.currentBuilding = this.locationsToVisit.get(1);
        			} else {
        				this.currentBuilding = this.locationsToVisit.get(0);
        			}
        		} else {
        			//do nothing
        		}
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
	
	public void Speak(String text)
    {
		if (this.tts != null) {
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
    }

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float direction = (float)event.values[0];
		this.compass.updateMessage(Float.toString(direction));
		if (this.currentBuilding != null) {
			boolean facing = facingBuilding(this.myLocation, this.currentBuilding, direction);
			this.myCalendar = Calendar.getInstance();
			long distance = this.myCalendar.getTimeInMillis() - this.lastTime;
			if (distance > 2000) {
				
				this.lastTime = this.myCalendar.getTimeInMillis();
				if (facing == true) {
					Toast.makeText(this, "FACING BUILDING", Toast.LENGTH_SHORT).show();
					//this.compass.updateMessage("FACING BUILDING");
				} else {
					Toast.makeText(this, "NOT FACING BUILDING", Toast.LENGTH_SHORT).show();
					//this.compass.updateMessage("NOT FACING BUILDING");
				}
			}
		}
	}
	
	public boolean facingBuilding(Location current, Location destination, float current_angle)
	   {
	           double delta_y = destination.getLatitude() - current.getLatitude();
	           double delta_x = destination.getLongitude() - current.getLongitude();
	           double angle = Math.atan(Math.abs(delta_y/delta_x));
	           angle = angle / Math.PI * 180;
	           if (delta_x >= 0 && delta_y >=0) angle = 90 - angle;
	           else if (delta_x > 0 && delta_y < 0) angle = 90 + angle;
	           else if (delta_x < 0 && delta_y < 0) angle = 270 - angle;
	           else if (delta_x < 0 && delta_y > 0) angle = 270 + angle;
	           
	           float diff_angle = (float) (Math.abs(current_angle - angle) > 180 ? 360 - Math.abs(current_angle - angle) : Math.abs(current_angle - angle));  
	           return  diff_angle <= 45;
	   }


}