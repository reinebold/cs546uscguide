package com.cs546group1.assignment3;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

/**
 * Old class to help us learn how to use Android's compass.  Not used in the project.
 * @author Jay
 *
 */
public class AndroidCompass extends Activity {

private static SensorManager mySensorManager;
private boolean sersorrunning;
private static double direction;

 /** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);
  
     mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
     List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
  
     if(mySensors.size() > 0){
      mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
      sersorrunning = true;
//      Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
    
     }
     else{
//      Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
      sersorrunning = false;
      finish();
     }
 }
 
 public static double getDirection()
 {
	 return direction;
 }

 private SensorEventListener mySensorEventListener = new SensorEventListener(){

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
 // TODO Auto-generated method stub

}

@Override
public void onSensorChanged(SensorEvent event) {
 // TODO Auto-generated method stub
	direction = (float)event.values[0];
}
 };

@Override
protected void onDestroy() {
// TODO Auto-generated method stub
super.onDestroy();

if(sersorrunning){
 mySensorManager.unregisterListener(mySensorEventListener);
}
}

}