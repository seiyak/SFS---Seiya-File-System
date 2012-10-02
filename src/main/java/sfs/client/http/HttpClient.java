package sfs.client.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import sfs.cpuinfo.CPUInfo;
import sfs.header.http.HeaderEntry;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.Separator;
import sfs.mime.Mime;
import sfs.verb.http.Verb;
import sfs.writer.Writer;

public class HttpClient {

	private final String serverAddress;
	private final int port;
	private final int INTERVAL_BETWEEN_CONNECT_TRY = 1000;
	private final int maxTrial;
	private SocketChannel serverChannel;
	public static final String HTTP = "http://";
	public static final String HTTP_VERSION = "HTTP/1.1" + Ending.CRLF.toString();
	private static final Logger log = Logger.getLogger( HttpClient.class );

	public HttpClient(String serverAddress, int port) {

		this.serverAddress = serverAddress;
		this.port = port;
		this.maxTrial = 30;
	}

	public HttpClient(String serverAddress, int port, int maxTrial) {

		this.serverAddress = serverAddress;
		this.port = port;
		this.maxTrial = maxTrial;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getPort() {
		return port;
	}

	public int getMaxTrial() {
		return maxTrial;
	}

	/**
	 * Initiates a connection between this client and the server.
	 * 
	 * @param writer
	 *            Object to determine the output format to the server.
	 * @throws IOException
	 */
	public void initiate(Writer writer) throws IOException {

		log.debug( "about to open connection to: " + getServerHost() );
		open();
		sendGreeting( new CPUInfo( writer ).getCPUInfo() );
	}

	/**
	 * Opens a connection between this client and the server.
	 * 
	 * @throws IOException
	 */
	private void open() throws IOException {

		serverChannel = SocketChannel.open();
		serverChannel.configureBlocking( false );
		serverChannel.connect( new InetSocketAddress( serverAddress, port ) );

		if ( !connectUpToMaxTrial() ) {
			throw new ConnectException( "Could not connect to the server, " + serverAddress + " after " + maxTrial
					+ " tries." );
		}

	}

	/**
	 * Tries to connect up to maxTrial number of times when the connection is not established.
	 * 
	 * @return True when the connection is established less than maxTrial times, false otherwise.
	 */
	private boolean connectUpToMaxTrial() {

		if ( serverChannel.isConnected() ) {
			return true;
		}

		int count = maxTrial;
		while ( count > 0 ) {
			--count;

			try {
				while ( !serverChannel.finishConnect() ) {

				}

				if ( serverChannel.isConnected() ) {
					break;
				}
			}
			catch ( IOException ex ) {
				log.error( "can't connect to: " + serverAddress + " after " + ( maxTrial - count )
						+ " trie(s) with interval: " + ( INTERVAL_BETWEEN_CONNECT_TRY / 1000 ) + " second" );
			}

			try {
				Thread.sleep( INTERVAL_BETWEEN_CONNECT_TRY );
			}
			catch ( InterruptedException e ) {

			}
		}

		return serverChannel.isConnected();
	}

	/**
	 * Sends a greeting to the server.
	 * 
	 * @param greeting
	 *            CPU info.
	 * @throws IOException
	 */
	private void sendGreeting(String greeting) throws IOException {

		String str = Verb.GET.getVerb() + " /" + " " + HTTP_VERSION + HeaderEntry.ACCEPT + Separator.COLON + " "
				+ Mime.HTML + Ending.CRLF + HeaderEntry.HOST + Separator.COLON + " " + getServerHost() + Ending.CRLF
				+ HeaderEntry.GREETING + Separator.COLON + " " + greeting + Ending.CRLF.toString()
				+ Ending.CRLF.toString();

		log.info( "about to greeting with: " + str );
		serverChannel.write( ByteBuffer.wrap( str.getBytes( "US-ASCII" ) ) );
	}

	/**
	 * Gets server and port combined with ':'.
	 * 
	 * @return Concatenation of serverAddress and port properties.
	 */
	private String getServerHost() {

		return serverAddress + Separator.COLON.toString() + port;
	}

	/**
	 * Closes the connection.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {

		if ( serverChannel != null ) {
			serverChannel.close();
		}
	}
}
