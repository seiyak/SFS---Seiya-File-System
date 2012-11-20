package sfs.client.http;

import java.io.IOException;
import java.net.ConnectException;
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
import sfs.response.http.ResponseMessage;
import sfs.response.statuscode.StatusCode;
import sfs.util.header.http.HeaderUtil;
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
		ResponseMessage responseMessage = doInitiate();
		closeServerChannel( getServerChannel( 0 ) );
		clearServerChannels();
		// closeSelector();

		// starts up internal server
		setUpInternalServer();

		// sets next hosts.
		setHostEntries( null );
		setHostEntries( responseMessage.getNextHosts( ResponseMessage.KEY_NEXT_HOSTS ) );

		if ( ( !isHostEntriesNull() ) && ( getHostEntry( 0 ) != null )
				&& responseMessage.getReasonPhrase().equals( StatusCode._200.getString() ) ) {
			// TODO can be run concurrently
			for ( int i = 0; i < getSizeOfHostEntries(); i++ ) {

				// joined sfs
				log.debug( "trying to connect to the next host from now on ..." );
				try {
					open( i, getHostEntry( i ) );
				}
				catch ( ConnectException ex ) {
					// TODO re-ask logic here
					log.error( ex );
					log.warn( "need to ask interactive server to replace the host" );
					closeServerChannel( getServerChannel( i ) );
				}
				log.debug( "opened with the next host successfully" );
			}
		}
		else {
			if ( responseMessage.getReasonPhrase().equals( StatusCode._200.getString() ) ) {
				// TODO this clause for the debugging purpose. can be deleted later.
				log.info( "might be added as the root. response from server: " + responseMessage.getContent() );
			}
			else {
				log.warn( "could not join sfs, because: " + responseMessage.getReasonPhrase() + " message: "
						+ responseMessage.getContent() );
				if ( internalServer != null ) {
					try {
						internalServer.close();
						internalServer = null;
					}
					catch ( IOException ex ) {
						log.error( ex );
					}
				}

				internalServerThread.interrupt();
				closeAllChannels();
				return;
			}
		}

		// TODO need to think what's done here
		log.debug( "ready to do the next thing" );
		while ( true ) {

		}
	}

	/**
	 * Initiates the communication between this client and the server.
	 * 
	 * @return ResponseMessage which holds the next node(s) that this client will interact from now on.
	 * @throws IOException
	 */
	private ResponseMessage doInitiate() throws IOException {

		open( 0, getHostEntry( 0 ) );
		log.debug( "done with open()" );
		doAction( getServerChannel( 0 ), getHostEntry( 0 ) );

		return new ResponseMessage().extractMessage( read( getServerChannel( 0 ) ) );
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
	private void setUpInternalServer() {

		internalServerThread = new Thread( new Runnable() {

			public void run() {

				if ( !configureInternalServer() ) {
					return;
				}

				try {
					ResponseMessage response = new ResponseMessage();
					RequestMessage request = new RequestMessage();
					log.info( "about to listen at " + internalServerPort + " internal server" );
					while ( true ) {
						SocketChannel channel = internalServer.accept();
						if ( channel != null ) {
							channel.configureBlocking( false );
							// log.debug("channel is not null, selector is " + getSelector());
							channel.register( getSelector(), SelectionKey.OP_READ | SelectionKey.OP_CONNECT );

							String res = read( channel );
							if ( isMessageRequest( res ) ) {
								request.extractMessage( res );
								if ( request.getContextPath().equals( "/liveness" ) ) {
									String content = "{\"status\":\"OK\",\"liveness\":\"true\"}";
									log.debug( "about to response ..." );
									write( channel,
											new ResponseMessage().createMessage( StatusCode._200,
													HeaderUtil.getResponseLivenessHeader( content.length() ), content ) );
									log.debug( "done with response ..." );
								}
							}
							else {
								log.debug( "non-request message res: " + res );
							}
						}
					}
				}
				catch ( Exception ex ) {
					shutdownInternalServerThread( ex );
				}
			}
		} );

		internalServerThread.start();
	}

	/**
	 * Configures internal server.
	 * 
	 * @return True if there are no exception happens during the configuration, false otherwise.
	 */
	private boolean configureInternalServer() {

		boolean done = false;

		try {
			internalServer = ServerSocketChannel.open();
			internalServer.socket()
					.bind( new InetSocketAddress( getLocalAddressMap().get( "v4" ), internalServerPort ) );
			internalServer.configureBlocking( false );
			done = true;
		}
		catch ( IOException ex ) {
			shutdownInternalServerThread( ex );
			done = false;
		}

		return done;
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
			try {
				internalServer.close();
				internalServer = null;
			}
			catch ( IOException e ) {
				log.error( e );
			}
		}

		try {
			internalServerThread.join( 10000 );
		}
		catch ( InterruptedException e ) {
			log.error( e );
			Thread.currentThread().interrupt();
		}
		catch ( Exception e ) {
			log.error( e );
		}
	}

	/**
	 * Checks if the specified message is a request message or not.
	 * 
	 * @param message
	 *            Checked if it's a request or not.
	 * @return True if it's a request, false otherwise.
	 */
	private boolean isMessageRequest(String message) {

		if ( message.startsWith( Verb.DELETE.toString() ) || message.startsWith( Verb.GET.toString() )
				|| message.startsWith( Verb.POST.toString() ) ) {
			return true;
		}

		return false;
	}
}
