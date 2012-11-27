package sfs.client.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import sfs.concatenable.date.Date;
import sfs.concatenable.liveness.LivenessTrue;
import sfs.concatenable.status.StatusOK;
import sfs.entry.HostEntry;
import sfs.header.http.separator.Colon;
import sfs.request.http.RequestMessage;
import sfs.response.http.ResponseMessage;
import sfs.response.statuscode.StatusCode;
import sfs.util.header.http.HeaderUtil;
import sfs.util.ipaddress.LocalIPAddress;
import sfs.verb.http.Verb;

public abstract class AbstractClient implements Clientable {

	private Map<String, String> localAddressMap;
	private Selector selector;
	private final ReentrantLock selectorLock;
	private List<SocketChannel> serverChannels;
	private SelectionKey initialSelectionKey;
	private HostEntry[] hostEntries;
	private final int INTERVAL_BETWEEN_CONNECT_TRY = 1000;
	private final int READ_BUFFER_CAPACITY = 4000;
	private static final Logger log = Logger.getLogger( AbstractClient.class );

	protected AbstractClient(HostEntry hostEntry) {
		serverChannels = new LinkedList<SocketChannel>();
		hostEntries = new HostEntry[] { hostEntry };
		localAddressMap = LocalIPAddress.getLocalIPAddress();
		selectorLock = new ReentrantLock();
	}

	/**
	 * Gets local IP address in v4 and v6 formats as Map.
	 * 
	 * @return Map holding IP address in v4 and v6 formats.
	 */
	protected final Map<String, String> getLocalAddressMap() {

		return Collections.unmodifiableMap( localAddressMap );
	}

	/**
	 * Checks if hostEntries property is null or not.
	 * 
	 * @return True if it's null, false otherwise.
	 */
	protected final boolean isHostEntriesNull() {
		return hostEntries == null;
	}

	/**
	 * Sets hostEntries property.
	 * 
	 * @param hostEntries
	 *            HostEntry array to be set as the property value.
	 */
	protected final void setHostEntries(HostEntry[] hostEntries) {
		this.hostEntries = hostEntries;
	}

	/**
	 * Gets HostEntry object in the hostEntries array specified by the specified index.
	 * 
	 * @param index
	 *            Used to specify which HostEntry in hostEntries array needs to be accessed.
	 * @return HostEntry object specified by the index.
	 */
	protected final HostEntry getHostEntry(int index) {
		return hostEntries[index];
	}

	/**
	 * Gets the size of the hostEntries property.
	 * 
	 * @return Size of the property.
	 */
	protected final int getSizeOfHostEntries() {
		return hostEntries != null ? hostEntries.length : -1;
	}

	/**
	 * Checks if serberChannels property is null or not.
	 * 
	 * @return True if it's null, false otherwise.
	 */
	protected final boolean isServerChannelsNull() {
		return serverChannels == null;
	}

	/**
	 * Clears the serverChannels property.
	 */
	protected final void clearServerChannels() {
		serverChannels.clear();
	}

	/**
	 * Gets SocketChannel object specified by the index.
	 * 
	 * @param index
	 *            Used to specify which SocketChannel is interested in.
	 * @return SocketChannel object specified by the index.
	 */
	protected final SocketChannel getServerChannel(int index) {
		return serverChannels.get( index );
	}

	/**
	 * Gets the size of serverChannels property.
	 * 
	 * @return Size of the property.
	 */
	protected final int getSizeOfServerChannels() {
		return serverChannels.size();
	}

	/**
	 * Gets selector property.
	 * 
	 * @return
	 */
	protected final Selector getSelector() {
		return selector;
	}

	protected final SelectionKey getInitialSelectionKey() {
		return initialSelectionKey;
	}

	/**
	 * Checks if hostEntries property is null or empty.
	 * 
	 * @return True if hostEntries property is null or empty, false otherwise.
	 */
	protected final boolean isHostEntriesNullOrEmpty() {

		return ( ( hostEntries == null ) || ( hostEntries.length == 0 ) ) ? true : false;
	}
	
	/**
	 * Opens a connection between this client and the server.
	 * 
	 * @param index
	 *            Index in serverChannels property.
	 * @param hostEntry
	 *            HostEntry object in hostEntries property.
	 * @throws IOException
	 */
	protected final void open(int index, HostEntry hostEntry) throws IOException {

		configure( index, hostEntry, false );
		log.debug( "done with configuration for channel" );

		if ( !connectUpToMaxTrial( index, hostEntry ) ) {
			throw new ConnectException( "Could not connect to the server, " + hostEntry.getHost() + " at "
					+ hostEntry.getPort() + " after " + hostEntry.getMaxTrial() + " tries." );
		}
	}

	/**
	 * @param index
	 *            Index in serverChannels property.
	 * @param hostEntry
	 *            HostEntry object in hostEntries property.
	 *            Configures serverChannel property before the connection is established.
	 * @param fromMaxTrial
	 *            Called by connectUpToMaxTrial() method or not.
	 */
	private void configure(int index, HostEntry hostEntry, boolean fromMaxTrial) {

		try {
			if ( fromMaxTrial ) {
				serverChannels.remove( index );
				serverChannels.add( SocketChannel.open() );
			}
			else {
				serverChannels.add( SocketChannel.open() );
			}

			serverChannels.get( index ).configureBlocking( false );

			if ( serverChannels.get( index )
					.connect( new InetSocketAddress( hostEntry.getHost(), hostEntry.getPort() ) ) ) {
				// in case that localhost is used
				selectorLock.lock();
				try{
					selector.wakeup();
					initialSelectionKey = serverChannels.get( index ).register( selector, SelectionKey.OP_READ );
				}finally{
					selectorLock.unlock();
				}
			}

			if ( selector == null ) {
				selector = Selector.open();
			}
		}
		catch ( IOException ex ) {
			log.error( ex.getMessage() );
		}
	}

	/**
	 * Tries to connect up to maxTrial number of times when the connection is not established.
	 * 
	 * @param index
	 *            Index in serverChannels property.
	 * @param hostEntry
	 *            HostEntry object in hostEntries property.
	 * @return True when the connection is established less than maxTrial times, false otherwise.
	 */
	private boolean connectUpToMaxTrial(int index, HostEntry hostEntry) {

		if ( serverChannels.get( index ).isConnected() ) {
			return true;
		}

		int count = hostEntry.getMaxTrial();
		while ( count > 0 ) {

			try {
				if ( serverChannels.get( index ).finishConnect() ) {

					selectorLock.lock();
					try {
						selector.wakeup();
						initialSelectionKey = serverChannels.get( index ).register( selector, SelectionKey.OP_READ );
					}
					finally {
						selectorLock.unlock();
					}

					log.info( "connected to the server at: " + getServerHost( hostEntry.getHost(), hostEntry.getPort() ) );
					break;
				}
			}
			catch ( IOException ex ) {
				log.error( "can't connect to: " + hostEntry.getHost() + " at " + hostEntry.getPort() + " after "
						+ ( hostEntry.getMaxTrial() - count ) + " trie(s) with interval: "
						+ ( INTERVAL_BETWEEN_CONNECT_TRY / 1000 ) + " second" );

				// sleep();
				configure( index, hostEntry, true );
			}catch(Exception ex){
				log.error(ex);
			}

			sleep();
			--count;
		}

		return serverChannels.get( index ).isConnected();
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
	 * Gets server and port combined with ':'.
	 * 
	 * @return Concatenation of serverAddress and port properties.
	 */
	protected final String getServerHost(String serverAddress, int port) {

		return serverAddress + new Colon().getSeparator() + port;
	}

	/**
	 * Closes the connection and clean up the resources that the client has taken.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {

		for ( int i = 0; i < serverChannels.size(); i++ ) {
			closeServerChannel( serverChannels.get( i ) );
		}

		serverChannels.clear();
		serverChannels = null;
		hostEntries = null;
		initialSelectionKey = null;
		closeSelector();
	}

	/**
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @throws IOException
	 */
	protected final void closeServerChannel(SocketChannel serverChannel) throws IOException {

		if ( serverChannel != null ) {
			serverChannel.close();
		}
	}

	/**
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @throws IOException
	 */
	protected void closeSelector() throws IOException {

		if ( selector.isOpen() ) {
			selector.close();
		}
	}

	/**
	 * Checks if the connection to the server is connected or not.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return True if the connection is connected, false otherwise.
	 */
	public boolean isConnected(SocketChannel serverChannel) {

		if ( serverChannel != null ) {
			return serverChannel.isConnected();
		}

		return false;
	}

	/**
	 * Checks if the connection to the server is open or not.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return True if the connection is open, false otherwise.
	 */
	public boolean isOpen(SocketChannel serverChannel) {

		if ( serverChannel != null ) {
			return serverChannel.isOpen();
		}

		return false;
	}

	/**
	 * Checks if the socket is closed or not.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return True if the socket is closed, false otherwise.
	 */
	public boolean isClosed(SocketChannel serverChannel) {

		if ( serverChannel != null ) {

			return serverChannel.socket().isClosed();
		}

		return false;
	}

	/**
	 * Reads data on the channel.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return Data read on the channel.
	 * @throws IOException
	 */
	public String read(SocketChannel serverChannel) throws IOException {

		return doRead( serverChannel );
	
	}

	/**
	 * Reads data on the channel.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return Data read on the channel.
	 * @throws IOException
	 */
	protected final String readNow(SocketChannel serverChannel) throws IOException {

		while ( true ) {

			selector.select();
			Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

			while ( itr.hasNext() ) {

				SelectionKey key = itr.next();

				if ( key.isReadable() ) {
					itr.remove();
					String str = doRead( serverChannel );
					if ( !str.isEmpty() ) {
						log.debug( "str: " + str );
						return str;
					}
				}
			}
		}
	}

	/**
	 * Reads data on the channel.
	 * 
	 * @param serverChannel
	 *            Holds channel between this client and the host.
	 * @return Data read on the channel.
	 * @throws IOException
	 */
	private String doRead(SocketChannel serverChannel) throws IOException {

		String str = "";
		ByteBuffer buffer = ByteBuffer.allocate( READ_BUFFER_CAPACITY );
		try {
			serverChannel.read( buffer );
			buffer.flip();
			Charset charset = Charset.forName("us-ascii");
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buffer);
			str = charBuffer.toString();
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			buffer.clear();
			buffer = null;
		}

		return str;
	}

	public void write(SocketChannel serverChannel, String message) throws IOException {
		serverChannel.write( ByteBuffer.wrap( message.getBytes( "US-ASCII" ) ) );
	}

	/**
	 * Close all socket channels.
	 * 
	 * @throws IOException
	 */
	protected final void closeAllChannels() throws IOException {
		for ( SocketChannel serverChannel : serverChannels ) {
			closeServerChannel( serverChannel );
		}
	}
	
	protected final void workWithSelectionKey(SelectionKey selectionKey, Thread internalServerThread,
			ServerSocketChannel internalServer) throws Exception {
		try {
			RequestMessage request = new RequestMessage();
			ResponseMessage response = new ResponseMessage();
			SocketChannel channel = null;

			while ( selectionKey.selector().select() > 0 ) {
				Iterator<SelectionKey> itr = selectionKey.selector().selectedKeys().iterator();
				while ( itr.hasNext() ) {
					SelectionKey readyKey = itr.next();
					itr.remove();

					if ( readyKey.isAcceptable() ) {
						log.debug( "readyKey is acceptable" );
						ServerSocketChannel ssc = (ServerSocketChannel) readyKey.channel();
						channel = (SocketChannel) ssc.accept();
						channel.configureBlocking( false );
						channel.register( getSelector(), SelectionKey.OP_READ );
					}
					else if ( readyKey.isReadable() ) {
						log.debug( "readyKey is readable" );

						if ( channel == null ) {
							log.warn( "channel is null, set up channel for read" );
							channel = (SocketChannel) readyKey.channel();
						}

						String res = read( channel );
						log.debug( "got res: " + res );

						if ( res.isEmpty() ) {
							log.warn( "reached the end of socket. about to close the channel" );
							channel.close();
						}

						if ( isMessageRequest( res ) ) {
							request.extractMessage( res );
							if ( request.getContextPath().equals( "/liveness" ) ) {
								String content = new StatusOK().add( new LivenessTrue() ).add( new Date() ).getJson();
								log.debug( "about to response with: " + content + " ..." );
								write( channel,
										new ResponseMessage().createMessage( StatusCode._200,
												HeaderUtil.getResponseLivenessHeader( content.length() ), content ) );
								log.debug( "done with response ..." );
							}
							else {
								log.debug( "got message: " + res );
							}
						}
						else if ( isMessageResponse( res ) ) {
							response.extractMessage( res );
							if(response.contains( "nextHosts" )){
								log.debug( "found response for greeting" );
								response.get( "nextHosts" );
								initializeConnection(internalServerThread,internalServer,response);
							}
						}
						else {
							log.debug( "non request message: " + read( channel ) );
						}
					}
				}
			}
		}
		catch ( Exception ex ) {
			log.error( ex );
			throw ex;
		}
	}

	/**
	 * Checks if the specified message is a request message or not.
	 * 
	 * @param message
	 *            Checked if it's a request or not.
	 * @return True if it's a request, false otherwise.
	 */
	protected final boolean isMessageRequest(String message) {

		if ( message.startsWith( Verb.DELETE.toString() ) || message.startsWith( Verb.GET.toString() )
				|| message.startsWith( Verb.POST.toString() ) ) {
			return true;
		}

		return false;
	}

	protected final boolean isMessageResponse(String message) {
		if ( message.startsWith( "HTTP/" ) ) {
			return true;
		}

		return false;
	}

	/**
	 * Initializes connection between this client and the server through greeting.
	 * 
	 * @param internalServerThread
	 *            Internal server thread.
	 * @param internalServer
	 *            Internal server.
	 * @param responseMessage
	 *            Response message that has just gotten from the server.
	 * @throws IOException
	 */
	private void initializeConnection(Thread internalServerThread, ServerSocketChannel internalServer,
			ResponseMessage responseMessage) throws IOException {

		closeServerChannel( getServerChannel( 0 ) );
		clearServerChannels();

		// sets next hosts.
		setHostEntries( null );
		log.debug( "response content here: " + responseMessage.getContent() );
		setHostEntries( responseMessage.getNextHosts( ResponseMessage.KEY_NEXT_HOSTS ) );

		if ( ( !isHostEntriesNull() ) && ( getHostEntry( 0 ) != null )
				&& responseMessage.getReasonPhrase().equals( StatusCode._200.getString() )
				&& responseMessage.get( "nextHosts" ) != null ) {
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
				catch ( Exception ex ) {
					log.error( ex );
				}
				log.info( "opened with the next host successfully" );
			}
		}
		else {
			if ( responseMessage.getReasonPhrase().equals( StatusCode._200.getString() ) ) {
				// TODO this clause for the debugging purpose. can be deleted later.
				log.debug( "might be added as the root. response from server: " + responseMessage.getContent() );
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
	}
}
