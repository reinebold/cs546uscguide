package com.cs546group1.assignment3;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

public class HTTPHelper {
   
    public static ArrayList<String> request(HttpResponse response){
        ArrayList<String> lines = new ArrayList<String>();
        try{
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null){
                lines.add(line + "\n");
            }
            in.close();
        }catch(Exception ex){
        	
        }
        return lines;
    }
} 
