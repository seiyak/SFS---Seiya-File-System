package sfs.async.handler.http.reader;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import sfs.header.http.ending.Ending;
import sfs.mime.Mime;
import sfs.stat.message.ContentDisposition;
import sfs.stat.message.DataType;
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
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF.toString() + "Content-type: application/json" + Ending.CRLF.toString()
						+ "Content-le", "ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
				"05 PM GMT" + Ending.CRLF.toString(), "Greeting-back: true" + Ending.CRLF.toString(),
				Ending.CRLF.toString() + "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" };
		String entireMessage = "";
		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}
		checkMessageStat( messageStat, 45, entireMessage );

		messageStat.clearStat();
		greetings = null;
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF + "Content-type: application/json" + Ending.CRLF.toString()
						+ "Content-le", "ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
				"05 PM GMT" + Ending.CRLF.toString(),
				"Greeting-back: true" + Ending.CRLF.toString() + Ending.CRLF.toString(),
				"{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" };

		entireMessage = "";
		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}
		checkMessageStat( messageStat, 45, entireMessage );

		messageStat.clearStat();
		greetings = null;
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF + "Content-type: application/json" + Ending.CRLF.toString()
						+ "Content-le", "ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
				"05 PM GMT" + Ending.CRLF.toString(),
				"Greeting-back: true" + Ending.CRLF.toString() + Ending.CRLF.toString(), "{\"status\": \"OK\",\"mes",
				"sage\": \"Welcome to SFS!\"}" };
		entireMessage = "";
		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}
		checkMessageStat( messageStat, 45, entireMessage );

		messageStat.clearStat();
	}

	@Test
	public void testFindEndOfMessageWithData1() {
		String boundary = "BOUNDARY";
		List<ContentDisposition> contentDispositions = new LinkedList<ContentDisposition>();
		String multiForm = generateMultipartForm( boundary, contentDispositions );

		String[] greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content-type: multipart/form-data; boundary=" + boundary + Ending.CRLF + "Content-length: "
						+ multiForm.length(), Ending.CRLF.toString() + Ending.CRLF.toString() + multiForm };

		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "BBBB";
		greetings = null;
		multiForm = generateMultipartForm( boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content-type: multipart/form-data; boundary=" + boundary + Ending.CRLF + "Content-length: "
						+ multiForm.length() + Ending.CRLF.toString(), Ending.CRLF.toString() + multiForm };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "AAAA";
		greetings = null;
		multiForm = generateMultipartForm( boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content-type: multipart/",
				"form-data; boundary=" + boundary + Ending.CRLF + "Content-length: " + multiForm.length()
						+ Ending.CRLF.toString(), Ending.CRLF.toString() + multiForm };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "CCCC";
		greetings = null;
		multiForm = generateMultipartForm( boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content", "-type: multipart/", "form-data; boun", "dary=" + boundary,
				Ending.CRLF + "Content-length: " + multiForm.length() + Ending.CRLF.toString(),
				Ending.CRLF.toString() + multiForm };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "DDD";
		greetings = null;
		multiForm = generateMultipartForm2( boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content", "-type: multipart/", "form-data; boun", "dary=" + boundary,
				Ending.CRLF + "Content-length: " + multiForm.length() + Ending.CRLF.toString(),
				Ending.CRLF.toString() + multiForm };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "EEEEE";
		greetings = null;
		multiForm = generateMultiForm3( "SFS.html", boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content", "-type: multipart/", "form-data; boun", "dary=" + boundary,
				Ending.CRLF + "Content-length: " + multiForm.length() + Ending.CRLF.toString(),
				Ending.CRLF.toString() + multiForm };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "FFFF";
		greetings = null;
		multiForm = generateMultiForm3( "SFS.html", boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content", "-type: multipart/", "form-data; boun", "dary=" + boundary,
				Ending.CRLF + "Content-length: " + multiForm.length() + Ending.CRLF.toString(),
				Ending.CRLF.toString() + multiForm.substring( 0, multiForm.length() / 2 ),
				multiForm.substring( ( multiForm.length() / 2 ) ) };
		checkMessageStat( greetings, multiForm, contentDispositions );

		contentDispositions.clear();
		boundary = "GGGG";
		greetings = null;
		multiForm = generateMultiForm3( "SFS.html", boundary, contentDispositions );
		greetings = new String[] {
				Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
						+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
						+ "Content", "-type: multipart/", "form-data; boun", "dary=" + boundary,
				Ending.CRLF + "Content-length: " + multiForm.length() + Ending.CRLF.toString(),
				Ending.CRLF.toString() + multiForm.substring( 0, 10 ), multiForm.substring( 10, 20 ),
				multiForm.substring( 20 ) };
		checkMessageStat( greetings, multiForm, contentDispositions );
	}
	
	@Test
	public void testFindEndOfMessageWithData2() {
		
		fail("'chunked' type is not implemented yet");
		
		List<ContentDisposition> contentDispositions = new LinkedList<ContentDisposition>();
		String boundary = "HHHH";
		String multiForm = generateMultipartForm( boundary, contentDispositions );
		String[] greetings = new String[] { Verb.GET + " / HTTP/1.1 " + Ending.CRLF + "Host: www.google.com"
				+ Ending.CRLF + "Accept: image/gif" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
				+ "Transfer-Encoding: chunked" + Ending.CRLF + "Content-type: multipart/form-data; boundary="
				+ boundary + Ending.CRLF + Ending.CRLF + Integer.toHexString( multiForm.length() ) + Ending.CRLF
				+ multiForm + 0 + Ending.CRLF + Ending.CRLF };
		checkMessageStat( greetings, multiForm, contentDispositions );
		
	}

	private void checkMessageStat(String[] greetings, String multiForm, List<ContentDisposition> contentDispositions) {
		String entireMessage = "";
		MessageStat messageStat = new MessageStat();

		for ( int i = 0; i < greetings.length; i++ ) {
			entireMessage += greetings[i];
			reader.findEndOfMessage( greetings[i], messageStat );
		}
		checkMessageStat( messageStat, multiForm.length(), entireMessage );
		checkContentDispositions( messageStat, contentDispositions );
	}

	private String generateMultipartForm(String boundary, List<ContentDisposition> contentDispositions) {

		String form = "--"
				+ boundary
				+ Ending.CRLF
				+ "Content-Disposition: form-data; name=\"username\""
				+ Ending.CRLF
				+ "userName1"
				+ Ending.CRLF
				+ "--"
				+ boundary
				+ Ending.CRLF
				+ "Content-Disposition: form-data; name=\"fileName\"; filename=\"/home/seiyak/sampleFile\" Content-type: text/plain"
				+ Ending.CRLF + "Here goes the content of the uploaded file." + Ending.CRLF + "--" + boundary + "--"
				+ Ending.CRLF;

		contentDispositions
				.add( new ContentDisposition( DataType.FORM_DATA, "\"username\"", "", Mime.NULL, "userName1" ) );
		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"fileName\"",
				"\"/home/seiyak/sampleFile\"", Mime.TEXT, "Here goes the content of the uploaded file." ) );

		return form;
	}

	private String generateMultipartForm2(String boundary, List<ContentDisposition> contentDispositions) {

		String form = "--"
				+ boundary
				+ Ending.CRLF
				+ "Content-Disposition: form-data; name=\"password\""
				+ Ending.CRLF
				+ "123"
				+ Ending.CRLF
				+ "--"
				+ boundary
				+ Ending.CRLF
				+ "Content-Disposition: form-data; name=\"FileName\"; filename=\"/home/seiyak/anotherSampleFile\" Content-type: image/jpeg"
				+ Ending.CRLF + "image data is here" + Ending.CRLF + "--" + boundary + "--" + Ending.CRLF;

		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"password\"", "", Mime.NULL, "123" ) );
		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"FileName\"",
				"\"/home/seiyak/anotherSampleFile\"", Mime.JPEG, "image data is here" ) );

		return form;
	}

	private String generateMultiForm3(String fileName, String boundary, List<ContentDisposition> contentDispositions) {

		File file = null;
		String content = "", fullPath = "";
		try {
			file = new File( getClass().getClassLoader().getResource( fileName ).toURI() );
			content = getAsString( file );
			fullPath = file.getCanonicalPath();
		}
		catch ( URISyntaxException e ) {
			e.printStackTrace();
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		String form = "";

		form = "--" + boundary + Ending.CRLF + "Content-Disposition: form-data; name=\"name\"" + Ending.CRLF
				+ file.getName() + Ending.CRLF + "--" + boundary + Ending.CRLF
				+ "Content-Disposition: form-data; name=\"FileName\"; filename=\"" + fullPath
				+ "\" Content-type: text/html" + Ending.CRLF + content + Ending.CRLF + "--" + boundary + "--"
				+ Ending.CRLF;

		contentDispositions
				.add( new ContentDisposition( DataType.FORM_DATA, "\"name\"", "", Mime.NULL, file.getName() ) );
		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"FileName\"", "\"" + fullPath + "\"",
				Mime.HTML, content ) );

		return form;
	}

	private String getAsString(File file) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader( new FileReader( file ) );
		}
		catch ( FileNotFoundException e1 ) {
			e1.printStackTrace();
		}
		String str = "", content = "";
		try {
			while ( ( str = reader.readLine() ) != null ) {
				content += str;
			}
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
		}

		return content;
	}

	private void checkContentDispositions(MessageStat messageStat, List<ContentDisposition> res) {

		assertTrue(
				"expecting messageStat.getSizeOfContentDispositions==res.size() but found messageStat.getSizeOfContentDispositions()=="
						+ messageStat.getSizeOfContentDispositions() + " res.size()==" + res.size(),
				messageStat.getSizeOfContentDispositions() == res.size() );
		Iterator<ContentDisposition> itr = messageStat.getIteratorForContentDispositions();
		Iterator<ContentDisposition> itr2 = res.iterator();

		while ( itr2.hasNext() ) {

			ContentDisposition c1 = itr.next();
			ContentDisposition c2 = itr2.next();

			assertTrue( "expecting c1.equals(c2)==ture but found false. c1: " + c1 + "\nc2: " + c2, c1.equals( c2 ) );
		}
	}

	private void checkMessageStat(MessageStat messageStat, int length, String entireMessage) {
		assertTrue( "expecting messageStat.getMessage().equals(entireMessage) but found entireMessage=="
				+ entireMessage + " messageStat.getMessage()==" + messageStat.getMessage(),
				entireMessage.equals( messageStat.getMessage() ) );
		assertTrue(
				"expecting messageStat.getMessageBodyLength()==" + length + " but found "
						+ messageStat.getMessageBodyLength(), messageStat.getMessageBodyLength() == length );
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
