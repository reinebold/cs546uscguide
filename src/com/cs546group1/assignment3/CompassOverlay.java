package com.cs546group1.assignment3;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Unused class in final project.  We were experimenting with compass overlays.
 * @author Jay
 *
 */
public class CompassOverlay extends Overlay {
	
	private String myMessage;
	
	public CompassOverlay(String message) {
		this.myMessage = message;
	}
	
	public void updateMessage(String newMessage) {
		this.myMessage = newMessage;
	}
	
	public void draw(Canvas c, MapView mv, boolean shadow) {
		super.draw(c, mv, shadow);
		
		Paint p = new Paint();
		c.drawText(this.myMessage, 50, 50, p);
	}

}
