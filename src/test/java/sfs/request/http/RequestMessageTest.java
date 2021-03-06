package sfs.request.http;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sfs.header.http.RequestHeaderEntry;
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
		assertTrue(
				"expecting Host.equals('www.google.com') but found "
						+ requestMessage.getHeader().get( RequestHeaderEntry.HOST ),
				requestMessage.getHeader().get( RequestHeaderEntry.HOST ).equals( "www.google.com" ) );
		assertTrue(
				"expecting Accept.equals('text/html') but found "
						+ requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ),
				requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ).equals( "text/html" ) );
		assertTrue(
				"expecting contextPathWithoutQuery.equals('/') but found "
						+ requestMessage.getContextPathWithoutQuery(), requestMessage.getContextPathWithoutQuery()
						.equals( "/" ) );
	}

	@Test
	public void testExtractMessage2() {
		String request = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com?keyword=google" + Ending.CRLF
				+ "Accept: text/html";
		requestMessage.extractMessage( request );
		assertTrue( "expecting verb.equals('GET') but found " + requestMessage.getVerb(), requestMessage.getVerb()
				.equals( "GET" ) );
		assertTrue( "expecting contextPath.equals('/') but found " + requestMessage.getContextPath(), requestMessage
				.getContextPath().equals( "/" ) );
		assertTrue( "expecting HttpVersion.equals('HTTP/1.1') but found " + requestMessage.getRequestHTTPVersion(),
				requestMessage.getRequestHTTPVersion().equals( "HTTP/1.1" ) );
		assertTrue(
				"expecting Host.equals('www.google.com') but found "
						+ requestMessage.getHeader().get( RequestHeaderEntry.HOST ),
				requestMessage.getHeader().get( RequestHeaderEntry.HOST ).equals( "www.google.com?keyword=google" ) );
		assertTrue(
				"expecting Accept.equals('text/html') but found "
						+ requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ),
				requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ).equals( "text/html" ) );
		assertTrue(
				"expecting contextPathWithoutQuery.equals('/') but found "
						+ requestMessage.getContextPathWithoutQuery(), requestMessage.getContextPathWithoutQuery()
						.equals( "/" ) );
	}
	
	@Test
	public void testClear() {

		String request = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html";
		requestMessage.extractMessage( request );
		requestMessage.clear();

		assertTrue( "expecting ver.equals('') but found " + requestMessage.getVerb(),
				requestMessage.getVerb().equals( "" ) );
		assertTrue( "expecting contextPath.equals('') but found " + requestMessage.getContextPath(), requestMessage
				.getContextPath().equals( "" ) );
		assertTrue( "expecting HttpVersion.equals('') but found " + requestMessage.getRequestHTTPVersion(),
				requestMessage.getRequestHTTPVersion().equals( "" ) );
		assertNull( "expecting getHeader().get( RequestHeaderEntry.HOST )== null but found " + requestMessage.getHeader().get( RequestHeaderEntry.HOST ),
				requestMessage.getHeader().get( RequestHeaderEntry.HOST ) );
		assertNull(
				"expecting getHeader().get( RequestHeaderEntry.ACCEPT ) but found " + requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ),
				requestMessage.getHeader().get( RequestHeaderEntry.ACCEPT ));
		assertTrue( "expecting requestMessage.getHeader().isEmpty()==true but found "
				+ requestMessage.getHeader().isEmpty(), requestMessage.getHeader().isEmpty() );
	}

}
