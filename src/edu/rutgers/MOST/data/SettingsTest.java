package edu.rutgers.MOST.data;

import java.util.ArrayList;

public class SettingsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Settings set = new Settings();
		//set.writeMethod1();
		set.read();
		System.out.println(set.lastL_SBML);
		
		SettingsFactory sFactory = new SettingsFactory();
		//sFactory.add("Test","Tester");
		//System.out.println(sFactory.toString());
		//sFactory.writeMethod1();
		
		System.out.println("Current Paths:");
		
		ArrayList<String> paths = sFactory.Gurobi.getPaths();
		
		
		for (String path : paths) {
			System.out.println(path);
		}
	}

}
