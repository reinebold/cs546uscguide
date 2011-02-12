package com.cs546group1.assignment3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import com.cs546group1.assignment3.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Environment;
import android.util.Log;

/**
 * Datastructure representing the campus map.
 * @author Jay
 *
 */
public class Campus {
	
	
	private Context context;
	
	private ArrayList<String> buildingCodes;
	
	private ArrayList<Double> buildingLats;
	
	private ArrayList<Double> buildingLongs;
	
	private ArrayList<ArrayList<String>> nodes;
	
	private ArrayList<String> buildingTypes;
	
	private ArrayList<ArrayList<String>> typesToBuildings;
	
	private Assignment3 master;

	
    public static final String KEY_CODE = "code";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_ID = "_id";
	
	/**
	 * Return the index for a given building code.
	 * @param code
	 * @return
	 */
	public int getIndex(String code) {
		for(int i=0; i < this.buildingCodes.size(); i++) {
			if (this.buildingCodes.get(i).equals(code)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * http://www.personal.kent.edu/~rmuhamma/Algorithms/MyAlgorithms/GraphAlgor/breadthSearch.htm
	 * Figure out how to get across the building by doign a simple BFS.
	 */
	public ArrayList<String> BFS(String start, String dest) {
		LinkedList<String> q = new LinkedList<String>();
		ArrayList<String> color = new ArrayList<String>();
		ArrayList<String> prev = new ArrayList<String>();
		ArrayList<Integer> dist = new ArrayList<Integer>();
		for(int i=0; i < this.buildingCodes.size(); i++) {
			color.add("WHITE");
			prev.add("NIL");
			dist.add(-1);
		}
		q.add(start);
		color.set(this.getIndex(start), "GRAY");
		dist.set(this.getIndex(start), 0);
		while (q.isEmpty() == false) {
			String u = q.poll();
			int index = this.getIndex(u);
			ArrayList<String> neighbors = this.nodes.get(index);
			for(int i=0; i < neighbors.size(); i++) {
				String neighbor = neighbors.get(i);
				int neighborIndex = this.getIndex(neighbor);
				if (color.get(neighborIndex).equals("WHITE")) {
					color.set(neighborIndex, "GRAY");
					dist.set(neighborIndex, dist.get(index) + 1);
					prev.set(neighborIndex, u);
					q.add(neighbor);
				}
			}
		}
		ArrayList<String> path = new ArrayList<String>();
		path.add(0, dest);
		int index = this.getIndex(dest);
		if (index == -1) {
			return null;
		}
		String previous = prev.get(index);
		while (! previous.equals("NIL")) {
			path.add(0, previous);
			index = this.getIndex(previous);
			previous = prev.get(index);
		}
		return path;
	}
	
	/**
	 * Consruct a new campus object.
	 * @param c
	 * @param m
	 */
	public Campus(Context c, Assignment3 m) {
		this.buildingCodes = new ArrayList<String>();
		this.buildingLats = new ArrayList<Double>();
		this.buildingLongs = new ArrayList<Double>();
		this.buildingTypes = new ArrayList<String>();
		this.nodes = new ArrayList<ArrayList<String>>();
		this.typesToBuildings = new ArrayList<ArrayList<String>>();
		this.context = c;
		this.master = m;
		this.parseBuildings();
		this.parseMatrix();	
		this.parseBuildingTypes();
	}
	
	/**
	 * Return a list of all building types.
	 * @return
	 */
	public ArrayList<String> getBuildingTypes() {
		return this.buildingTypes;
	}
	
	/**
	 * Return a list of strings that tell you where to go.
	 * @param code
	 * @return
	 */
	public ArrayList<String> getTextDirections(String code) {
		return this.BFS(this.getNearestBuilding(), code);
	}
	
	/**
	 * Return a list of integer lat, long pairs that tell you where to go.
	 * The path is made up of these pairs.
	 * @param code
	 * @return
	 */
	public ArrayList<Integer> getPoints(String code) {
		ArrayList<Integer> answer = new ArrayList<Integer>();
		ArrayList<String> steps = this.BFS(this.getNearestBuilding(), code);
		if (steps != null) {
			//make sure there is a valid path
			for(int i=0; i < steps.size(); i++) {
				double latD = this.buildingLats.get(this.getIndex(steps.get(i)));
				double lonD = this.buildingLongs.get(this.getIndex(steps.get(i)));
				int latI = (int) (latD * 1e6);
				int lonI = (int) (lonD * 1e6);
				answer.add(latI);
				answer.add(lonI);
			}
			return answer;
		}
		return null;
	}
	
	/**
	 * Return the nearest building to a given location.
	 * @param l
	 * @return
	 */
	public String getNearestBuilding(Location l) {
		double shortestDist = 999999;
		String shortest = this.buildingCodes.get(0);
		for(int i=0; i < this.buildingCodes.size(); i++) {
			String current = this.buildingCodes.get(i);
			double currentLat = this.buildingLats.get(i);
			double currentLon = this.buildingLongs.get(i);
			double currentDist = Math.sqrt(Math.pow(currentLat - l.getLatitude(), 2) + Math.pow(currentLon - l.getLongitude(), 2));
			if (currentDist < shortestDist) {
				shortestDist = currentDist;
				shortest = current;
			}
		}
		return shortest;
	}
	
	/**
	 * Return the nearest building to the user location.
	 * @return
	 */
	public String getNearestBuilding() {
		Location l = this.master.myLocation;
		double shortestDist = 999999;
		String shortest = this.buildingCodes.get(0);
		for(int i=0; i < this.buildingCodes.size(); i++) {
			String current = this.buildingCodes.get(i);
			double currentLat = this.buildingLats.get(i);
			double currentLon = this.buildingLongs.get(i);
			double currentDist = Math.sqrt(Math.pow(currentLat - l.getLatitude(), 2) + Math.pow(currentLon - l.getLongitude(), 2));
			if (currentDist < shortestDist) {
				shortestDist = currentDist;
				shortest = current;
			}
		}
		return shortest;
	}
	
	/**
	 * Parse the adjacency matrix to form the graph representation of campus.
	 */
	private void parseMatrix() {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader bs = new BufferedReader(new InputStreamReader((InputStream) context.getResources().openRawResource(R.raw.adjmatrix)));
		String line;
		//read the file, store in an array for later parsing
		try {
			while ((line = bs.readLine()) != null) {
				//System.out.println(line);
				lines.add(line);
			}
		} catch(IOException ex) {
			System.out.println("ERROR PARSING ADJACENCY MATRIX!");
		}
		for(int i=0; i < lines.size(); i++) {
			this.nodes.add(new ArrayList<String>());
			for(int j=0; j < lines.get(i).length(); j++) {
				char c = lines.get(i).charAt(j);
				if (c == '1') {
					this.nodes.get(i).add(this.buildingCodes.get(j));
				}
			}
		}
	}
	
	/**
	 * Parse the building code/lat/lon information.
	 */
	private void parseBuildings() {
		try {
			ArrayList<String> lines = new ArrayList<String>();
			InputStreamReader isr = new InputStreamReader((InputStream) context.getResources().openRawResource(R.raw.buildingdata));
			BufferedReader bs = new BufferedReader(isr);
			String line;
			//read the file, store in an array for later parsing
			while ((line = bs.readLine()) != null) {
				lines.add(line);
			}
			bs.close();
			for(int i=0; i < lines.size(); i++) {
				String[] parts = lines.get(i).split(":");
				String code = parts[0];
				String lat = parts[2];
				String lon = parts[3];
				this.buildingCodes.add(code);
				this.buildingLats.add(Double.parseDouble(lat));
				this.buildingLongs.add(Double.parseDouble(lon));
			}
		} catch (IOException ex) {
			System.out.println("ERROR PARSING BUILDINGS!");
		}
	}
	
	private void parseBuildingTypes() {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			InputStreamReader isr = new InputStreamReader((InputStream) context.getResources().openRawResource(R.raw.buildingsdata));
			BufferedReader bs = new BufferedReader(isr);
			String line;
			//read the file, store in an array for later parsing
			while ((line = bs.readLine()) != null) {
				lines.add(line);
			}
			bs.close();
		} catch(IOException ex) {
			System.out.println("ERROR PARSING BUILDING TYPES!");
		}
		for(int i=0; i < lines.size(); i++) {
			if (lines.get(i).startsWith("{")) {
				//parse based on file format
				//String nameLine = lines.get(i + 6);
				//String name = nameLine.substring(9, nameLine.length() - 2);
				String codeLine = lines.get(i + 7);
				String code = codeLine.substring(9, codeLine.length() - 2);
				//String latLine = lines.get(i + 1);
				//String lat = latLine.substring(12, latLine.length() - 1);
				//String lonLine = lines.get(i + 2);
				//String lon = lonLine.substring(13, lonLine.length() - 1);
				//String descLine = lines.get(i + 8);
				//String desc = descLine.substring(10, descLine.length() - 2);
				String typeLine = lines.get(i + 10);
				String type = typeLine.substring(9, typeLine.length() - 2);
				this.insertBuildingOfType(code, type);
			}
		}
	}
	
	private String getTypeFromIndex(int index) {
		return this.buildingTypes.get(index);
	}

	private void insertBuildingOfType(String code, String type) {
		if(! this.buildingTypes.contains(type)) {
			this.buildingTypes.add(type);
			this.typesToBuildings.add(new ArrayList<String>());
			this.typesToBuildings.get(this.typesToBuildings.size() - 1).add(code);
		}
		for(int i=0; i < typesToBuildings.size(); i++) {
			//look for the right bin
			if (getTypeFromIndex(i).equals(type)) {
				//see if its already in the bin
				boolean found = false;
				for(int j=0; j < typesToBuildings.get(i).size(); j++) {
					if (typesToBuildings.get(i).get(j).equals(code)) {
						found = true;
					}
				}
				if (found == false) {
					typesToBuildings.get(i).add(code);
				}
			}
		}
	}

	public ArrayList<String> getBuildingsWithType(long selection) {
		// TODO Auto-generated method stub
		return this.typesToBuildings.get((int)selection);
	}
}
