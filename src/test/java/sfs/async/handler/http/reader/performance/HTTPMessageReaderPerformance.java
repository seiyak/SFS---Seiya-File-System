package sfs.async.handler.http.reader.performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.http.HttpServerDecoder;
import org.springframework.util.StopWatch;

import sfs.async.handler.http.reader.HTTPMessageReader;
import sfs.header.http.ending.Ending;
import sfs.mime.Mime;
import sfs.stat.message.ContentDisposition;
import sfs.stat.message.DataType;
import sfs.stat.message.MessageStat;
import sfs.verb.http.Verb;

public class HTTPMessageReaderPerformance {

	private HTTPMessageReader reader;
	private HttpServerDecoder httpServerDecoder;
	private StopWatch stopWatch;
	private static Logger log = Logger.getLogger( HTTPMessageReaderPerformance.class );

	public HTTPMessageReaderPerformance() {
		reader = new HTTPMessageReader();
		httpServerDecoder = new HttpServerDecoder();
		stopWatch = new StopWatch();
	}

	public final void performanceWithShortMessage() {
		String request = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: text/html" + Ending.CRLF + Ending.CRLF;

		startStopWatch( request );
	}

	public final void performanceWithShortMessage2() {
		String request = Verb.GET + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF.toString()
				+ "Content-type: application/json" + Ending.CRLF.toString() + "Content-length: 45"
				+ Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:05 PM GMT" + Ending.CRLF.toString()
				+ "Greeting-back: true" + Ending.CRLF.toString() + Ending.CRLF.toString()
				+ "{\"status\": \"OK\",\"message\": \"This is a sample message.\"}";

		startStopWatch( request );
	}

	public void performanceWithMediumMessage() {
		/*
		 * The request is from http://stackoverflow.com/questions/1378476/http-get-request-packet-size-in-bytes
		 */
		String request = "GET /ga.js HTTP/1.1\r\n"
				+ "Host: www.google-analytics.com\r\n"
				+ "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)\r\n"
				+ "Accept: */*\r\n" + "Accept-Language: en-us,en;q=0.5\r\n" + "Accept-Encoding: gzip,deflate\r\n"
				+ "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\r\n" + "Keep-Alive: 300\r\n"
				+ "Connection: keep-alive\r\n" + "Referer: http://stackoverflow.com/\r\n"
				+ "If-Modified-Since: Mon, 31 Aug 2009 17:13:58 GMT\r\n" + "\r\n" + "\r\n";

		startStopWatch( request );
	}

	public void performanceWithLongMessage() {
		/*
		 * The request is based on the sample request at
		 * http://stackoverflow.com/questions/1378476/http-get-request-packet-size-in-bytes
		 */
		String request = "GET /ga.js HTTP/1.1\r\n"
				+ "Host: www.google-analytics.com\r\n"
				+ "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)\r\n"
				+ "Accept: */*\r\n"
				+ "Accept-Language: en-us,en;q=0.5\r\n"
				+ "Accept-Encoding: gzip,deflate\r\n"
				+ "Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7\r\n"
				+ "Keep-Alive: 300\r\n"
				+ "Connection: keep-alive\r\n"
				+ "Referer: http://stackoverflow.com/\r\n"
				+ "If-Modified-Since: Mon, 31 Aug 2009 17:13:58 GMT\r\n"
				+ "Greeting-back: true\r\n"
				+ "Liveness: true\r\n"
				+ "Origin: sampleOrigin\r\n"
				+ "Type: sampleType\r\n"
				+ "Verb: sampleVerb\r\n"
				+ "From: sample@email.com\r\n"
				+ "Cookie: uid=1274108650-45267447-66848880; mc=1137458542-57565784-88898864"
				+ Ending.CRLF.toString()
				+ Ending.CRLF.toString()
				+ "{\"status\": \"OK\",\"message\": \"This is a sample message. I hope that this example shows the performance of HTTPMessageReader to read incoming messages and create the header and message body objects for the further processes. This is a performance test to see how fast HTTPMessageReader can read.\",\"username\":\"userName\",\"password\":\"password1\"}";

		startStopWatch( request );
	}

	public final void performanceWithSmallFile() {
		List<ContentDisposition> contentDispositions = new LinkedList<ContentDisposition>();
		String boundary = "AAAA";
		String multiForm = generateMultipartForm( boundary, contentDispositions );
		String request = Verb.POST + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
				+ "Content-type: multipart/form-data; boundary=" + boundary + Ending.CRLF + "Content-length: "
				+ multiForm.length() + Ending.CRLF.toString() + Ending.CRLF.toString() + multiForm;

		startStopWatch( request );
	}

	public final void performanceWithMediumFile() {
		String boundary = "EEEEE";
		List<ContentDisposition> contentDispositions = new LinkedList<ContentDisposition>();
		String multiForm = generateMultiForm3( "SFS.html", boundary, contentDispositions );
		String request = Verb.POST + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
				+ "Content-type: multipart/form-data; boundary=" + boundary + Ending.CRLF + "Content-length: "
				+ multiForm.length() + Ending.CRLF.toString() + Ending.CRLF.toString() + multiForm;

		startStopWatch( request );
	}

	public final void performanceWithLongFile() {
		String boundary = "DDDD";
		List<ContentDisposition> contentDispositions = new LinkedList<ContentDisposition>();
		String multiForm = generateMultiForm4( boundary, contentDispositions, 50000, 1 );
		String request = Verb.POST + " / HTTP/1.1" + Ending.CRLF + "Host: www.google.com" + Ending.CRLF
				+ "Accept: image/gif, image/jpeg,*/*" + Ending.CRLF + "Accept-Language: en-us" + Ending.CRLF
				+ "Content-type: multipart/form-data; boundary=" + boundary + Ending.CRLF + "Content-length: "
				+ multiForm.length() + Ending.CRLF.toString() + Ending.CRLF.toString() + multiForm;

		startStopWatch( request );

	}

	private void startStopWatch(String request) {
		IoBuffer buf = IoBuffer.allocate( request.length(), false );
		buf.put( request.getBytes() );
		MessageStat messageStat = new MessageStat();
		stopWatch = new StopWatch();

		stopWatch.start( "MINA with message length=" + request.length() );
		httpServerDecoder.decode( new DummySession(), buf, new DummyProtocolDecoderOutput() );
		stopWatch.stop();

		stopWatch.start( "HTTPMessageReader with message length=" + +request.length() );
		reader.findEndOfMessage( request, messageStat );
		stopWatch.stop();

		System.out.println( stopWatch.prettyPrint() );
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

		log.debug( "sample file size: " + "Here goes the content of the uploaded file.".length() );

		return form;
	}

	private String generateMultiForm3(String fileName, String boundary, List<ContentDisposition> contentDispositions) {

		File file = null;
		String content = "", fullPath = "";
		try {
			file = new File( getClass().getClassLoader().getResource( fileName ).toURI() );
			content = getAsString( file );

			log.debug( "sample file size: " + content.length() );

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

	private String generateMultiForm4(String boundary, List<ContentDisposition> contentDispositions, int width,
			int height) {

		String content = createDummyImage( width, height ), fullPath = "samplePath", name = "sampleName.jpg";
		String form = "";

		form = "--" + boundary + Ending.CRLF + "Content-Disposition: form-data; name=\"name\"" + Ending.CRLF + name
				+ Ending.CRLF + "--" + boundary + Ending.CRLF
				+ "Content-Disposition: form-data; name=\"FileName\"; filename=\"" + fullPath
				+ "\" Content-type: text/html" + Ending.CRLF + content + Ending.CRLF + "--" + boundary + "--"
				+ Ending.CRLF;

		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"name\"", "", Mime.NULL, name ) );
		contentDispositions.add( new ContentDisposition( DataType.FORM_DATA, "\"FileName\"", "\"" + fullPath + "\"",
				Mime.JPEG, content ) );

		return form;
	}

	private String createDummyImage(int width, int height) {

		Random rand = new Random();
		char[] image = new char[width * height];
		for ( int row = 0; row < height; row++ ) {
			for ( int col = 0; col < width; col++ ) {
				image[col + ( row * width )] = (char) ( rand.nextInt( 26 ) + 'a' );
			}
		}

		log.debug( "dummy cimage size: " + image.length );

		return String.valueOf( image );
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

	public static void main(String[] args) {

		HTTPMessageReaderPerformance readerPerformance = new HTTPMessageReaderPerformance();
		readerPerformance.performanceWithLongFile();
	}
}
