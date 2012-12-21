package sfs.async.handler.http.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.async.handler.Reader;
import sfs.stat.message.MessageStat;

public abstract class AbstractHTTPReader implements Reader {

	private final int bufferCapacity;
	private final int DEFAULT_BUFFER_CAPACITY = 1000;
	private final ByteBuffer buffer;
	private final Map<SocketChannel, MessageStat> connectionMap;
	private static final Logger log = Logger.getLogger( AbstractHTTPReader.class );

	protected AbstractHTTPReader() {
		bufferCapacity = DEFAULT_BUFFER_CAPACITY;
		buffer = ByteBuffer.allocateDirect( bufferCapacity );
		connectionMap = new HashMap<SocketChannel, MessageStat>();
	}

	protected AbstractHTTPReader(int bufferCapacity) {
		this.bufferCapacity = bufferCapacity;
		buffer = ByteBuffer.allocateDirect( this.bufferCapacity );
		connectionMap = new HashMap<SocketChannel, MessageStat>();
	}

	/**
	 * Gets read buffer capacity.
	 * 
	 * @return Capacity for the read buffer.
	 */
	public int getBufferCapacity() {
		return bufferCapacity;
	}

	/**
	 * Gets accumulated message for the specified channel.
	 * 
	 * @param channel
	 *            Used to find the corresponding message.
	 * @return Accumulated message for the specified channel.
	 */
	public String getMessage(SocketChannel channel) {

		String str = connectionMap.get( channel ).getMessage();
		return str == null ? "" : str;
	}

	/**
	 * Removes the specified channel entry from the internal data structure.
	 * 
	 * @param channel
	 *            Used to find the channel.
	 * @return True if removes the channel successfully, otherwise false.
	 */
	public boolean removeChannel(SocketChannel channel) {

		try {
			connectionMap.remove( channel );
		}
		catch ( RuntimeException ex ) {
			log.error( ex );
			return false;
		}

		return true;
	}

	/**
	 * Gets number of currently active connections.
	 * 
	 * @return Number of currently active connections.
	 */
	public int getNumberOfConnections() {
		return connectionMap.size();
	}

	/**
	 * Clears all the active connections from the internal data structure.
	 */
	public void clearAllConnections() {
		connectionMap.clear();
	}

	/**
	 * Clears the accumulated message for the specified channel.
	 * 
	 * @param channel
	 *            Used to find the corresponding message.
	 * @return True if clears successfully, otherwise false.
	 */
	public boolean clearMessage(SocketChannel channel) {

		try {
			connectionMap.get( channel ).clearStat();
		}
		catch ( RuntimeException ex ) {
			log.error( ex );
			return false;
		}

		return true;
	}

	/**
	 * Reads message from the specified channel.
	 * 
	 * @param Channel
	 *            where the read is taken place.
	 * @throws IOException
	 */
	public MessageStat read(SocketChannel channel) throws IOException {

		buffer.clear();
		int numOfRead = channel.read( buffer );
		buffer.flip();

		String str = decodeBuffer( buffer );

		if ( connectionMap.get( channel ) == null ) {
			connectionMap.put( channel, new MessageStat() );
		}

		findEndOfMessage( str, connectionMap.get( channel ) );

		return connectionMap.get( channel );
	}

	/**
	 * Decodes the message from the specified buffer.
	 * 
	 * @param buffer
	 *            Where the message is taken.
	 * @return Decoded message.
	 */
	protected String decodeBuffer(ByteBuffer buffer) {
		Charset charset = Charset.forName( "us-ascii" );
		CharsetDecoder decoder = charset.newDecoder();

		try {
			return decoder.decode( buffer ).toString();
		}
		catch ( CharacterCodingException ex ) {
			log.error( ex );
		}

		return null;
	}

	public abstract boolean findEndOfMessage(String message, MessageStat messageStat);
}
