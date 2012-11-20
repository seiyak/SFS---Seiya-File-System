package sfs.server.http;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import sfs.entry.HostEntry;

public class HttpServerTest {

	private InteractiveServer server;

	@Before
	public void setUp() throws Exception {

		server = new InteractiveServer( new HostEntry( "192.168.0.103", 60625 ) );
	}

	@Test
	public void test() throws IOException {
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
