package sfs.response.http;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import sfs.entry.HTTPHeaderEntry;
import sfs.entry.HostEntry;
import sfs.header.http.HeaderEntry;
import sfs.header.http.ResponseHeaderEntry;
import sfs.header.http.ending.Ending;
import sfs.mime.Mime;
import sfs.response.statuscode.StatusCode;

public class ResponseMessageTest {

	private ResponseMessage responseMessage;

	@Before
	public void setUp() throws Exception {

		responseMessage = new ResponseMessage();
	}

	@Test
	public void testExtractMessage() {

		String greeting = "HTTP/1.1 200 OK" + Ending.CRLF.toString();
		greeting += "Content-type: application/json" + Ending.CRLF.toString();
		greeting += "Content-length: 45" + Ending.CRLF.toString();
		greeting += "Date: Mon, Oct 22, 2012 08:15:05 PM GMT" + Ending.CRLF.toString();
		greeting += "Greeting-back: true" + Ending.CRLF.toString();
		greeting += Ending.CRLF.toString();
		greeting += "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}";
		responseMessage.extractMessage( greeting );

		HTTPHeaderEntry[] header = responseMessage.getHeader();
		checkEachHeader( new HTTPHeaderEntry[] { new HTTPHeaderEntry( HeaderEntry.CONTENT_TYPE, Mime.JSON ),
				new HTTPHeaderEntry( ResponseHeaderEntry.CONTENT_LENGTH, 45 ),
				new HTTPHeaderEntry( HeaderEntry.DATE, "Mon, Oct 22, 2012 08:15:05 PM GMT" ),
				new HTTPHeaderEntry( ResponseHeaderEntry.GREETING_BACK, "true" ) }, header );
		assertTrue( "getContent(): " + responseMessage.getContent() + "expecting: "
				+ "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}",
				responseMessage.getContent().equals( "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" ) );
		assertTrue( "expecting status=='OK' but found " + responseMessage.get( "status" ),
				responseMessage.get( "status" ).equals( "OK" ) );
		assertTrue( "expecting message==\"Welcome to SFS!\" but found " + responseMessage.get( "message" ),
				responseMessage.get( "message" ).equals( "Welcome to SFS!" ) );
	}

	private void checkEachHeader(HTTPHeaderEntry[] expected, HTTPHeaderEntry[] res) {

		assertTrue( "expecting expected.length == res.length but found rexpected.length==" + expected.length
				+ " res.length==" + res.length, expected.length == res.length );
		for ( int i = 0; i < expected.length - 1; i++ ) {

			assertTrue( "expecting expected[" + i + "]==res[" + i + "] but found expected[" + i + "]==" + expected[i]
					+ " res[" + i + "]==" + res[i], expected[i].toString().equals( res[i].toString() ) );
		}
	}
	
	@Test
	public void testGetNextHosts(){
		
		String input = "HTTP/1.1 200 OK" + Ending.CRLF.toString();
		input += "Content-type: application/json" + Ending.CRLF.toString();
		input += "Content-length: 45" + Ending.CRLF.toString();
		input += "Date: Mon, Oct 22, 2012 08:15:05 PM GMT" + Ending.CRLF.toString();
		input += Ending.CRLF.toString();
				
		input += "{\"message\":\"Welcome to SFS!\",\"status\":\"OK\",\"nextHosts\":[{\"port\":2071,\"host\":\"tucana.bsd.uchicago.edu\",\"maxTrial\":30}]}";
		HostEntry[] res = responseMessage.extractMessage( input ).getNextHosts( "nextHosts" );
		assertNotNull("expecting res != null but found res==null", res );
	}

	@Test
	public void testCreateMessage() {

		String message = "{\"message\":\"hello\"}";
		String res1 = responseMessage.createMessage( StatusCode._200, new HTTPHeaderEntry[] {
				new HTTPHeaderEntry( ResponseHeaderEntry.CONTENT_LENGTH, message.length() ),
				new HTTPHeaderEntry( HeaderEntry.CONTENT_TYPE, Mime.JSON ),
				new HTTPHeaderEntry( ResponseHeaderEntry.LIVENESS_BACK, true ) }, message );

		String expected = "HTTP/1.1 200 OK" + Ending.CRLF + ResponseHeaderEntry.CONTENT_LENGTH + ": "
				+ message.length() + Ending.CRLF + HeaderEntry.CONTENT_TYPE + ": " + Mime.JSON + Ending.CRLF
				+ ResponseHeaderEntry.LIVENESS_BACK + ": " + true + Ending.CRLF + Ending.CRLF + message;

		assertTrue( "expecting res1.equals(expected but found res1==" + res1 + " expected==" + expected,
				res1.equals( expected ) );
	}
	
	@Test
	public void testGetHeaderAsMap(){

		String greeting = "HTTP/1.1 200 OK" + Ending.CRLF.toString();
		greeting += "Content-type: application/json" + Ending.CRLF.toString();
		greeting += "Content-length: 45" + Ending.CRLF.toString();
		greeting += "Date: Mon, Oct 22, 2012 08:15:05 PM GMT" + Ending.CRLF.toString();
		greeting += "Greeting-back: true" + Ending.CRLF.toString();
		greeting += Ending.CRLF.toString();
		greeting += "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}";
		responseMessage.extractMessage( greeting );
		
		Map<String,Object> headers = responseMessage.getHeaderAsMap();
		assertTrue("expecting header.size()==4 but found " + headers.size(),headers.size() == 4);
	}
}
