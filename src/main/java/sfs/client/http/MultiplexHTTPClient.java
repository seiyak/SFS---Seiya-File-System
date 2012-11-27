package sfs.client.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import sfs.concatenable.cpuinfo.CPUInfo;
import sfs.concatenable.date.Date;
import sfs.concatenable.ip.local.LocalIPAddressInfo;
import sfs.concatenable.port.PortInfo;
import sfs.entry.HostEntry;
import sfs.request.http.RequestMessage;
import sfs.util.header.http.HeaderUtil;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.util.string.StringUtil;
import sfs.verb.http.Verb;

public class MultiplexHTTPClient extends HTTPClient {

	private ServerSocketChannel internalServer;
	private int internalServerPort;
	private Thread internalServerThread;
	private static Logger log = Logger.getLogger( MultiplexHTTPClient.class );

	public MultiplexHTTPClient(HostEntry hostEntry) {
		super( hostEntry );
		internalServerPort = hostEntry.getPort() + 1;
	}

	public void initiate() throws IOException {
		if ( isHostEntriesNullOrEmpty() ) {
			throw new IllegalStateException( "No host(s) is specified.Please specify host information for this client" );
		}

		// initiates the connection between this client and the server.
		doInitiate();

		while ( true ) {

		}
	}

	/**
	 * Initiates the communication between this client and the server.
	 * 
	 * @throws IOException
	 */
	private void doInitiate() throws IOException {

		open( 0, getHostEntry( 0 ) );
		log.debug( "done with open() and about to send greeting" );
		setUpInternalServer();
		doAction( getServerChannel( 0 ), getHostEntry( 0 ) );
	}

	/**
	 * Acts on the specified SocketChannel, host and port.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @param hostEntry
	 *            Holds host's name and port.
	 * @throws IOException
	 */
	protected void doAction(SocketChannel serverChannel, HostEntry hostEntry) throws IOException {

		try {
			sendGreeting(
					serverChannel,
					hostEntry,
					new CPUInfo().add( new LocalIPAddressInfo() ).add( new PortInfo( hostEntry.getPort() ) )
							.add( new Date() ).add( new PortInfo( "internal", internalServerPort ) ).getJson(),
					"/greeting" );
		}
		catch ( IOException ex ) {
			throw ex;
		}
	}

	/**
	 * Sends a greeting to the server.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @param hostEntry
	 *            HostEntry object in hostEntries property.
	 * @param greeting
	 *            CPU info.
	 * @throws IOException
	 */
	private void sendGreeting(SocketChannel serverChannel, HostEntry host, String greeting, String greetingContextPath)
			throws IOException {

		write( serverChannel, new RequestMessage().createMessage( Verb.GET, StringUtil
				.getContextPath( greetingContextPath ), HeaderUtil.getRequestGreetingHeader( greeting, host,
				getServerHost( host.getHost(), host.getPort() ), getLocalAddressMap().get( "v4" ) ) ) );
	}

	/**
	 * Sets up internal server and starts i on a separate thread.
	 */
	public void setUpInternalServer() {

		internalServerThread = new Thread( new Runnable() {

			public void run() {
				
				configureInternalServer();
				log.info( "about to start internal server" );
				
				while ( true ) {
					SelectionKey selectionKey = null;
					if ( ( selectionKey = registerInternalServer() ) == null ) {
						return;
					}

					try {
						workWithSelectionKey( selectionKey,internalServerThread,internalServer );
					}
					catch ( IOException ex ) {
						log.error( ex );
						shutdownInternalServerThread( ex );
					}
					catch ( Exception ex ) {
						log.error( ex );
						shutdownInternalServerThread( ex );
						break;
					}
				}
			}
		},"internalServer" );

		internalServerThread.start();
	}

	/**
	 * Configures internal server.
	 * 
	 */
	private void configureInternalServer() {

		try {
			internalServer = ServerSocketChannel.open();
			internalServer.configureBlocking( false );
			internalServer.socket().setReuseAddress( true );
			internalServer.socket().bind(
					new InetSocketAddress( LocalIPAddress.getLocalIPAddress().get( "v4" ), internalServerPort ) );
			log.debug( "done with configure internal server" );
		}
		catch ( Exception ex ) {
			shutdownInternalServerThread( ex );
		}
	}

	private SelectionKey registerInternalServer() {

		SelectionKey selectionKey = null;
		try {
			selectionKey = internalServer.register( getSelector(), SelectionKey.OP_ACCEPT );
		}
		catch ( IOException ex ) {
			log.error( ex );
			shutdownInternalServerThread( ex );
		}catch(NullPointerException ex){
			log.warn("internal server is about to shutdown");
		}

		return selectionKey;
	}

	/**
	 * Shuts down internal server and the thread.
	 * 
	 * @param ex
	 *            Exception thrown when internal server was working which is the reason to shut down it.
	 */
	private void shutdownInternalServerThread(Exception ex) {

		log.error( ex );

		if ( internalServer != null && internalServer.isOpen() ) {
			log.info( "about to shut down internalServer because of the thrown exception" );
			try {
				internalServer.close();
				internalServer = null;
			}
			catch ( IOException e ) {
				log.error( e );
			}
		}

		try {
			internalServerThread.join( 1000 );
		}
		catch ( InterruptedException e ) {
			log.error( e );
			Thread.currentThread().interrupt();
		}
		catch ( Exception e ) {
			log.error( e );
		}
	}
}
