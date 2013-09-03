package edu.rutgers.MOST.data;

import java.util.ArrayList;

public class GurobiStatTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GurobiStatus gStat = new GurobiStatus();
		ArrayList<String> paths = gStat.getPaths();
		String cur = System.getProperty("user.dir");
		System.out.println(cur);
		//for (String path : paths) {
			//System.out.println(path);
		//}
	}

}
