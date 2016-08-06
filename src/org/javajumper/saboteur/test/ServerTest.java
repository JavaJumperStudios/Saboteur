package org.javajumper.saboteur.test;

import java.util.logging.Logger;

import org.javajumper.saboteur.SaboteurServer;

/**
 * A class for manually testing the server
 */
public class ServerTest {

	/**
	 * Test entry point
	 * 
	 * @param _args
	 *            CLI arguments
	 */
	public static void main(String[] _args) {
		SaboteurServer server = new SaboteurServer();
		SaboteurServer.instance = server;
		Logger.getLogger(SaboteurServer.class.getName()).info("Server wird gestartet.");
		server.init();
		server.start();
	}

}
