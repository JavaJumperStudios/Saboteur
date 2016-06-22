package org.javajumper.saboteur.test;

import org.javajumper.saboteur.SaboteurServer;
import org.newdawn.slick.SlickException;

import com.sun.istack.internal.logging.Logger;

public class ServerTest {

	public static void main(String[] _args) throws SlickException, InterruptedException {
		SaboteurServer server = new SaboteurServer();
		SaboteurServer.instance = server;
		Logger.getLogger(SaboteurServer.class).info("Server wird gestartet.");
		server.start();
	}

}
