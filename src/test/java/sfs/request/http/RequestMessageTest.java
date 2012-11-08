package sfs.request.http;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.header.http.ending.Ending;
import sfs.verb.http.Verb;

public class RequestMessageTest {

	private RequestMessage requestMessage;

	@Before
	public void setUp() throws Exception {

		requestMessage = new RequestMessage();
	}

	@Test
	public void testExtractMessage() {

		String request = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html";
		requestMessage.extractMessage( request );
		assertTrue( "expecting verb.equals('GET') but found " + requestMessage.getVerb(), requestMessage.getVerb()
				.equals( "GET" ) );
		assertTrue( "expecting contextPath.equals('/') but found " + requestMessage.getContextPath(), requestMessage
				.getContextPath().equals( "/" ) );
		assertTrue( "expecting HttpVersion.equals('HTTP/1.1') but found " + requestMessage.getRequestHTTPVersion(),
				requestMessage.getRequestHTTPVersion().equals( "HTTP/1.1" ) );
		assertTrue( "expecting Host.equals('www.google.com') but found " + requestMessage.getHeader().get( 0 ).getValue(),
				requestMessage.getHeader().get( 0 ).getValue().equals( "www.google.com" ) );
		assertTrue( "expecting Accept.equals('text/html') but found " + requestMessage.getHeader().get( 1 ).getValue(),
				requestMessage.getHeader().get( 1 ).getValue().equals( "text/html" ) );
	}

}
