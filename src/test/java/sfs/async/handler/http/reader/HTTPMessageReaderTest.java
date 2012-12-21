package sfs.async.handler.http.reader;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import sfs.header.http.ending.Ending;
import sfs.stat.message.MessageStat;
import sfs.verb.http.Verb;

public class HTTPMessageReaderTest {

	private HTTPMessageReader reader;

	@Before
	public void setUp() throws Exception {

		reader = new HTTPMessageReader();
	}

	@Test
	public void testFindEndOfMessageWithoutMessageBody() {

		String request1 = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html" + Ending.CRLF + Ending.CRLF;
		MessageStat messageStat = new MessageStat();

		boolean res = reader.findEndOfMessage( request1, messageStat );
		assertTrue( "expecting res==true but found " + res, res );
		assertTrue( "expecting request1.equals(messageStat.getMessage() but found " + messageStat.getMessage(),
				messageStat.getMessage().equals( request1 ) );
		assertTrue(
				"expecting messageStat.getLength()==" + request1.length() + " but found " + messageStat.getLength(),
				messageStat.getLength() == request1.length() );

		messageStat.clearStat();
		String request2 = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html" + Ending.CRLF + Ending.CRLF;
		res = reader.findEndOfMessage( request2, messageStat );
		assertTrue( "expecting res==true but found " + res, res );
		assertTrue( "expecting request2.equals(messageStat.getMessage() but found " + messageStat.getMessage(),
				messageStat.getMessage().equals( request2 ) );
		assertTrue(
				"expecting messageStat.getLength()==" + request2.length() + " but found " + messageStat.getLength(),
				messageStat.getLength() == request2.length() );

		messageStat.clearStat();
		String[] strs = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF + "Accept: text/html"
						+ Ending.CRLF, Ending.CRLF.toString() };
		for ( int i = 0; i < strs.length; i++ ) {

			res = reader.findEndOfMessage( strs[i], messageStat );
			if ( i == 0 ) {
				assertFalse( "expecting res==false but found " + res, res );
			}
			else if ( i == 1 ) {
				assertTrue( "expecting res==true but found " + res, res );
			}
		}
		assertTrue(
				"expecting messageStat.getMessage().equals(" + strs[0] + strs[1] + ") but found "
						+ messageStat.getMessage(), messageStat.getMessage().equals( strs[0] + strs[1] ) );
		assertTrue( "expecting messageStat.getLength()==" + ( strs[0].length() + strs[1].length() ) + " but found "
				+ messageStat.getLength(), messageStat.getLength() == strs[0].length() + strs[1].length() );
	}

	@Test
	public void testFindEndOfMessageWithMessageBody() {

		MessageStat messageStat = new MessageStat();
		String[] greetings = new String[] {
				"HTTP/1.1 200 OK" + Ending.CRLF.toString() + "Content-type: application/json" + Ending.CRLF.toString()
						+ "Content-le", "ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
				"05 PM GMT" + Ending.CRLF.toString(), "Greeting-back: true" + Ending.CRLF.toString(),
				Ending.CRLF.toString() + "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" };
		String entireMessage = "";
		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}

		assertTrue( "expecting messageStat.getMessage().equals(entireMessage) but found entireMessage=="
				+ entireMessage + " messageStat.getMessage()==" + messageStat.getMessage(),
				entireMessage.equals( messageStat.getMessage() ) );
		assertTrue( "expecting messageStat.getMessageBodyLength()==45 but found " + messageStat.getMessageBodyLength(),
				messageStat.getMessageBodyLength() == 45 );

		messageStat.clearStat();
		greetings = null;
		greetings = new String[] {
				"HTTP/1.1 200 OK" + Ending.CRLF.toString() + "Content-type: application/json" + Ending.CRLF.toString()
						+ "Content-le", "ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
				"05 PM GMT" + Ending.CRLF.toString(),
				"Greeting-back: true" + Ending.CRLF.toString() + Ending.CRLF.toString(),
				"{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" };

		entireMessage = "";
		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}
		assertTrue( "expecting messageStat.getMessageBodyLength()==45 but found " + messageStat.getMessageBodyLength(),
				messageStat.getMessageBodyLength() == 45 );
		assertTrue( "expecting messageStat.getMessage().equals(entireMessage) but found entireMessage=="
				+ entireMessage + " messageStat.getMessage()==" + messageStat.getMessage(),
				entireMessage.equals( messageStat.getMessage() ) );
	}

	@Test(expected = IllegalStateException.class)
	public void testFindEndOfMessageWithException() {

		String request1 = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html" + Ending.CRLF + Ending.CRLF;
		MessageStat messageStat = new MessageStat();
		messageStat.setEndOfMessage( true );
		reader.findEndOfMessage( request1, messageStat );
	}

	@Test
	public void testRead() throws IOException {

		new Thread( new Runnable() {

			public void run() {
				try {
					Server server = new Server( "localhost", 60626 );
				}
				catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} ).start();

		startClient( "localhost", 60626, 2 );

		try {
			Thread.sleep( 5000 );
		}
		catch ( InterruptedException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startClient(String host, int port, int num) throws IOException {

		final Client[] clients = new Client[num];
		for ( int i = 0; i < clients.length; i++ ) {
			clients[i] = new Client( host, port );
			final Integer id = i;

			new Thread( new Runnable() {

				public void run() {
					Client client = clients[id];
					try {
						client.starPoll();
					}
					catch ( Exception ex ) {
						ex.printStackTrace();
					}
				}
			} ).start();
		}
	}
}
