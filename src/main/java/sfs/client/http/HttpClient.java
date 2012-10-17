package sfs.client.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.concatenable.cpuinfo.CPUInfo;
import sfs.concatenable.ip.local.LocalIPAddressInfo;
import sfs.concatenable.port.PortInfo;
import sfs.header.http.HTTPHeader;
import sfs.header.http.RequestHeaderEntry;
import sfs.header.http.separator.Colon;
import sfs.mime.Mime;
import sfs.request.http.RequestMessage;
import sfs.server.http.NodeServer;
import sfs.server.http.NodeServer;
import sfs.util.http.JSoupViewCreator;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.util.string.StringUtil;
import sfs.verb.http.Verb;

public class HttpClient {

	private final String serverAddress;
	private final int port;
	private final int INTERVAL_BETWEEN_CONNECT_TRY = 1000;
	private final int maxTrial;
	private SocketChannel serverChannel;
	public static final String HTTP = "http://";
	private static final Logger log = Logger.getLogger( HttpClient.class );
	private Map<String, String> localAddressMap;
	private NodeServer nodeServer;
	private Selector selector;

	public HttpClient(String serverAddress, int port) {

		this.serverAddress = serverAddress;
		this.port = port;
		this.maxTrial = 30;
		localAddressMap = LocalIPAddress.getLocalIPAddress();
	}

	public HttpClient(String serverAddress, int port, int maxTrial) {

		this.serverAddress = serverAddress;
		this.port = port;
		this.maxTrial = maxTrial;
		localAddressMap = LocalIPAddress.getLocalIPAddress();
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

	protected Map<String, String> getLocalAddressMap() {

		return Collections.unmodifiableMap( localAddressMap );
	}

	/**
	 * Initiates a connection between this client and the server.
	 * 
	 * @throws IOException
	 */
	public void initiate() throws IOException {

		log.debug( "about to open connection to: " + getServerHost() );
		open();
		doInitiate();
	}

	protected void doInitiate() throws IOException {

		try {
			sendGreeting( new CPUInfo().add( new LocalIPAddressInfo() ).add( new PortInfo( port ) ).getJson(),
					"/greeting" );
		}
		catch ( IOException ex ) {
			throw ex;
		}

		startNodeServer( 5 );
	}

	/**
	 * Opens a connection between this client and the server.
	 * 
	 * @throws IOException
	 */
	private void open() throws IOException {

		configure();
		if ( !connectUpToMaxTrial() ) {
			throw new ConnectException( "Could not connect to the server, " + serverAddress + " after " + maxTrial
					+ " tries." );
		}
	}

	/**
	 * Configures serverChannel property before the connection is established.
	 */
	private void configure() {

		try {
			serverChannel = SocketChannel.open();
			serverChannel.configureBlocking( false );
			serverChannel.connect( new InetSocketAddress( serverAddress, port ) );

			selector = Selector.open();
			serverChannel.register( selector, SelectionKey.OP_READ );
		}
		catch ( IOException ex ) {
			log.error( ex.getMessage() );
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
			try {
				if ( serverChannel.finishConnect() ) {
					log.info( "connected to the server at: " + getServerHost() );
					break;
				}
			}
			catch ( IOException ex ) {

				log.error( "can't connect to: " + serverAddress + " after " + ( maxTrial - count )
						+ " trie(s) with interval: " + ( INTERVAL_BETWEEN_CONNECT_TRY / 1000 ) + " second" );

				sleep();
				configure();
			}

			--count;
		}

		return serverChannel.isConnected();
	}

	/**
	 * Sleeps.
	 */
	private void sleep() {

		try {
			Thread.sleep( INTERVAL_BETWEEN_CONNECT_TRY );
		}
		catch ( InterruptedException e ) {

		}
	}

	/**
	 * Sends a greeting to the server.
	 * 
	 * @param greeting
	 *            CPU info.
	 * @throws IOException
	 */
	private void sendGreeting(String greeting, String greetingContextPath) throws IOException {

		RequestMessage requestMessage = new RequestMessage();
		String str = requestMessage.request( Verb.GET, StringUtil.getContextPath( greetingContextPath ) );

		HTTPHeader header = new HTTPHeader();
		header.put( RequestHeaderEntry.ACCEPT, Mime.HTML );
		header.put( RequestHeaderEntry.HOST, getServerHost() );
		header.put( RequestHeaderEntry.GREETING, greeting );
		header.put( RequestHeaderEntry.ORIGIN, localAddressMap.get( "v4" ) );

		str += header.format();

		log.info( "about to greeting with: " + str );
		write( str );
	}

	/**
	 * Writes out the message to a channel.
	 * 
	 * @param message
	 *            Message to be sent.
	 * @throws IOException
	 */
	protected final void write(String message) throws IOException {
		serverChannel.write( ByteBuffer.wrap( message.getBytes( "US-ASCII" ) ) );
	}

	/**
	 * Gets server and port combined with ':'.
	 * 
	 * @return Concatenation of serverAddress and port properties.
	 */
	protected final String getServerHost() {

		return serverAddress + new Colon().getSeparator() + port;
	}

	/**
	 * Starts an internal server to do the actual distributed jobs.
	 */
	private void startNodeServer(final int backLog) throws IOException {

		// TODO check the port + 1 later.
		nodeServer = new NodeServer( localAddressMap.get( "v4" ), port + 1, backLog );
		nodeServer.start();
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

		if ( nodeServer != null ) {
			nodeServer.stop();
		}
		
		if(selector.isOpen()){
			selector.close();
		}
	}

	/**
	 * Checks if the connection to the server is connected or not.
	 * 
	 * @return True if the connection is connected, false otherwise.
	 */
	public boolean isConnected() {

		if ( serverChannel != null ) {
			return serverChannel.isConnected();
		}

		return false;
	}

	/**
	 * Checks if the connection to the server is open or not.
	 * 
	 * @return True if the connection is open, false otherwise.
	 */
	public boolean isOpen() {

		if ( serverChannel != null ) {
			return serverChannel.isOpen();
		}

		return false;
	}

	/**
	 * Checks if the socket is closed or not.
	 * 
	 * @return True if the socket is closed, false otherwise.
	 */
	public boolean isClosed() {

		if ( serverChannel != null ) {

			return serverChannel.socket().isClosed();
		}

		return false;
	}

	public String read() throws IOException {

		String res = "";
		int ready = 0;
		while ( ready == 0 ) {
			try {
				ready = selector.select();
			}
			catch ( IOException ex ) {
				log.error( ex );
			}
		}

		Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
		while ( itr.hasNext() ) {

			SelectionKey key = itr.next();

			if ( key.isReadable() ) {
				log.debug( "found readable on selector" );
				res = doRead();
			}

			itr.remove();
		}

		return res;
	}
	
	private String doRead() throws IOException{
		
		String str = "";
		ByteBuffer buffer = ByteBuffer.allocate( 1024 );
			try {
				serverChannel.read( buffer );
				buffer.flip();
				byte[] b = new byte[buffer.remaining()];
				buffer.get( b );
				str = new String(b);
			}
			catch ( IOException ex ) {
				throw ex;
			}finally{
				buffer.clear();
			}
			
			return str;
	}
}
