package org.javajumper.saboteur.test;

import java.util.logging.Logger;

import org.javajumper.saboteur.SaboteurServer;
import org.newdawn.slick.SlickException;

public class ServerTest {

	public static void main(String[] _args) throws SlickException, InterruptedException {
		SaboteurServer server = new SaboteurServer();
		SaboteurServer.instance = server;
		Logger.getLogger(SaboteurServer.class.getName()).info("Server wird gestartet.");
		server.init();
		server.start();
	}

}
