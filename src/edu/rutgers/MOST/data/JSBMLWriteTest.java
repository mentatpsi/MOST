package edu.rutgers.MOST.data;

import edu.rutgers.MOST.config.LocalConfig;


public class JSBMLWriteTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//LocalConfig.getInstance().getLoadedDatabase()
		JSBMLWriter1 jsWrite = new JSBMLWriter1();
		
		//TODO: Likely won't work without GraphicalInterface implemented, and a database running
		//TODO: Implement in GraphicalInterface
		
	}
	
	public void connect(LocalConfig config) throws Exception {
		JSBMLWriter1 jsWrite = new JSBMLWriter1();
		jsWrite.formConnect(config);
	}

}
