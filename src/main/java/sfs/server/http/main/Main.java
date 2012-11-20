package sfs.server.http.main;

import java.io.IOException;

import sfs.entry.HostEntry;
import sfs.server.http.InteractiveServer;

public class Main {

	private InteractiveServer server;

	public void setUp() throws Exception {

		server = new InteractiveServer( new HostEntry( "tucana.bsd.uchicago.edu", 60625 ) );
	}

	public void start() throws IOException {
		server.start();

		try {
			System.out.println( "server lives for 20 seconds and die for the testing purpose" );
			Thread.sleep( 10000000 );
		}
		catch ( InterruptedException e ) {
			server.stop();
		}

	}

	public static void main(String[] args) throws IOException {

		if ( args.length != 2 ) {
			System.out.println( "usage: Main host port" );
			System.exit( 0 );
		}

		InteractiveServer server = new InteractiveServer( new HostEntry( args[0], Integer.parseInt( args[1] ) ) );
		server.start();

		try {
			System.out.println( "server lives for 20 seconds and die for the testing purpose" );
			Thread.sleep( 10000000 );
		}
		catch ( InterruptedException e ) {
			server.stop();
		}
	}
}
