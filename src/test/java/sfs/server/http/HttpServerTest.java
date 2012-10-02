package sfs.server.http;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class HttpServerTest {

	private HttpServer server;

	@Before
	public void setUp() throws Exception {

		server = new HttpServer( "localhost", 2071 );
	}

	@Test
	public void test() throws IOException {
		server.start();

		try {
			System.out.println( "server lives for 20 seconds and die for the testing purpose" );
			Thread.sleep( 20000 );
		}
		catch ( InterruptedException e ) {
			server.stop();
		}

	}

}
