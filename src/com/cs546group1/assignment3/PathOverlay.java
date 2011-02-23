package com.cs546group1.assignment3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 2D Line segment overlay for maps.
 * @author Jay
 *
 */
public class PathOverlay extends Overlay {
	
	
	private GeoPoint start;
	
	private GeoPoint end;
	
	private int color;
	
	private double DETECT_DISTANCE = 300.0;
	
	/**
	 * A line segment starting at one geopoint and ending at another.
	 * @param startP
	 * @param endP
	 */
	public PathOverlay(GeoPoint startP, GeoPoint endP) {
		this.start = startP;
		this.end = endP;
		this.color = Color.GREEN;
	}
	
	/**
	 * Determine if the location is close to the endpoint.  If it is, change the color to black.
	 * Rationale:  the user has already taken the path.
	 * @param l
	 */
	public void checkCovered(Location l){
		if (this.color == Color.GREEN) {
			int locLat = (int) (l.getLatitude() * 1e6);
			int locLon = (int) (l.getLongitude() * 1e6);
			double dist = Math.sqrt(Math.pow(locLat - end.getLatitudeE6(), 2) + Math.pow(locLon - end.getLongitudeE6(), 2));
			if (dist <= DETECT_DISTANCE) {
				this.color = Color.BLACK;
			}
		}
	}
	
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Draw to the display.  2D line segment in green or black connecting the two locations.
	 */
	public void draw(Canvas c, MapView mv, boolean shadow) {
		super.draw(c, mv, shadow);
		
		Paint p = new Paint();
		p.setColor(this.color);
		p.setStrokeWidth(3);
		mv.getProjection();
		Point sPoint = new Point();
		Point ePoint = new Point();
		
		mv.getProjection().toPixels(this.start, sPoint);
		mv.getProjection().toPixels(this.end, ePoint);
		c.drawCircle(sPoint.x, sPoint.y, 5, p);
		c.drawCircle(ePoint.x, ePoint.y, 5, p);
		c.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y, p);
	}

}
