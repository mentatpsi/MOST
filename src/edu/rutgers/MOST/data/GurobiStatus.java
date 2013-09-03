package edu.rutgers.MOST.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GurobiStatus {
	private ArrayList<String> paths;
	private Iterator<String> pathIter;
	private String mostRecent;
	private Map<String,String> mapping;
	
	public GurobiStatus() {
		this.possiblePaths();
	}
	
	public ArrayList<String> getPaths() {
		return paths;
	}
	
	public String getNext() {
		if (pathIter.hasNext()) {
			String cur = (String) pathIter.next();
			return cur;
		}
		
		else {
			return null;
		}
	}
	
	/*
	private boolean isMoreRecent(String ver1, String ver2) {
		int verStart = ver1.indexOf("i") + 1;
		int i = 0;
		int ver1L = ver1.length();
		int ver2L = ver2.length();
		int dif = Math.abs((ver1L - ver2L));
		if (ver1L > ver2L) {
			ver2 += String.format("%0" + dif + "d", 0);
			
		}
		
		if (ver1L < ver2L) {
			ver1 += String.format("%0" + dif + "d", 0);
		}
		
		while (((int) ver1.charAt(i)) == ((int) ver2.charAt(i)) && (i < ver1L)) {
			i += 1;
		}
		
		if ((int) ver1.charAt(i) > (int) ver2.charAt(i)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void mostRecent() {
		String curMRecent = paths.get(0);
		int verStart = curMRecent.indexOf("i") + 1;
		String curVer;
		for (String path : paths) {
			if (isMoreRecent(path,curMRecent)) {
				curMRecent = path;
			}
			curVer = path.substring(verStart);
		}
	}
	*/
	
	public void addGurobiJava(String path) {
		
	}
	
	public void addGurobiJava() {
		
	}
	
	
	private void possiblePaths() {	
		String path = "C:\\"; 
	    
	    String files;
	    ArrayList<String> paths = new ArrayList<String>();
	    
	    File folder = new File(path);
	    File[] listOfFiles = folder.listFiles(); 
	    
	    for (int i = 0; i < listOfFiles.length; i++) 
	    {
	   
		     if (listOfFiles[i].isDirectory()) 	{
			     files = listOfFiles[i].getName();
			     
				 if (files.matches("gurobi*")) {
					 System.out.println(files);
					 paths.add(files);
				 }
		     }
		
	    }
	}
	
	
}

