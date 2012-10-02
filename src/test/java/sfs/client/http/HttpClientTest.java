package sfs.client.http;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import sfs.cpuinfo.writer.json.CPUInfoJsonWriter;

public class HttpClientTest {

	private HttpClient client;

	@Before
	public void setUp() throws Exception {

		client = new HttpClient( "localhost", 2071 );
	}

	@Test
	public void testInitiate() throws IOException {
		client.initiate( new CPUInfoJsonWriter() );
		try {
			Thread.sleep( 10000 );
		}
		catch ( InterruptedException e ) {
			System.out.println( "about to close the connection" );
			client.close();
		}
	}

}
