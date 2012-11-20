package sfs.client.http;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.entry.HostEntry;
import sfs.header.http.separator.Colon;
import sfs.util.ipaddress.LocalIPAddress;

public abstract class AbstractClient implements Clientable {

	private Map<String, String> localAddressMap;
	private Selector selector;
	private List<SocketChannel> serverChannels;
	private HostEntry[] hostEntries;
	private final int INTERVAL_BETWEEN_CONNECT_TRY = 1000;
	private final int READ_BUFFER_CAPACITY = 1024;
	private static final Logger log = Logger.getLogger( AbstractClient.class );

	protected AbstractClient(HostEntry hostEntry) {
		serverChannels = new ArrayList<SocketChannel>();
		hostEntries = new HostEntry[] { hostEntry };
		localAddressMap = LocalIPAddress.getLocalIPAddress();
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
				serverChannels.get( index ).register( selector, SelectionKey.OP_READ );
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

					serverChannels.get( index ).register( selector, SelectionKey.OP_READ );

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
			}

			log.debug( "come here : serverChannels.get(index).isConnected ? "
					+ serverChannels.get( index ).isConnected() );
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

		while ( true ) {

			selector.select();
			Iterator<SelectionKey> itr = selector.selectedKeys().iterator();

			while ( itr.hasNext() ) {

				SelectionKey key = itr.next();

				if ( key.isAcceptable() ) {
					log.debug( "in blocking mode ? : " + serverChannel.isBlocking() );
					if ( serverChannel.isBlocking() ) {
						log.debug( "about to change to non blocking mode" );
						serverChannel.configureBlocking( false );
						log.debug( "about to register in the selector" );
						serverChannel.register( getSelector(), SelectionKey.OP_READ );
					}
				}
				else if ( key.isReadable() ) {
					itr.remove();
					String str = doRead( serverChannel );
					if ( !str.isEmpty() ) {
						return str;
					}
				}
				else if ( key.isWritable() ) {
					log.debug( "found writable on selector" );
					itr.remove();
				}
				else if ( key.isConnectable() ) {
					log.debug( "found connectable on selector" );
					itr.remove();
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
			byte[] b = new byte[buffer.remaining()];
			buffer.get( b );
			str = new String( b );
		}
		catch ( IOException ex ) {
			throw ex;
		}
		finally {
			buffer.clear();
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
}
