package sfs.async.handler.http.reader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import sfs.header.http.RequestHeaderEntry;
import sfs.server.http.page.notfound.NotFoundWriter;
import sfs.server.http.page.notfound.SimpleNotFoundWriter;
import sfs.server.http.standalone.handler.contextpath.ContextPathHandler;
import sfs.stat.message.MessageStat;

public class Server {

	private int port;
	private final ServerSocketChannel socket;
	private final Selector selector;
	private final HTTPMessageReader requestReader;
	private final Map<String, ContextPathHandler> contextPathMap;
	private static Logger log = Logger.getLogger( Server.class );

	public Server(String host, int port) throws IOException {
		this.port = port;
		socket = ServerSocketChannel.open();
		socket.configureBlocking( false );
		socket.socket().bind( new InetSocketAddress( host, port ) );
		selector = Selector.open();
		SelectionKey key = socket.register( selector, SelectionKey.OP_ACCEPT );
		requestReader = new HTTPMessageReader( 10 );
		contextPathMap = new HashMap<String, ContextPathHandler>();

		startPoll( key );
	}

	public Server(String host, int port, int bufferSize) throws IOException {
		this.port = port;
		socket = ServerSocketChannel.open();
		socket.configureBlocking( false );
		socket.socket().bind( new InetSocketAddress( host, port ) );
		selector = Selector.open();
		SelectionKey key = socket.register( selector, SelectionKey.OP_ACCEPT );
		requestReader = new HTTPMessageReader( bufferSize );
		contextPathMap = new HashMap<String, ContextPathHandler>();

		startPoll( key );
	}

	public Server(String host, int port, int bufferSize, Map<String, ContextPathHandler> contextPathMap)
			throws IOException {
		this.port = port;
		socket = ServerSocketChannel.open();
		socket.configureBlocking( false );
		socket.socket().bind( new InetSocketAddress( host, port ) );
		selector = Selector.open();
		SelectionKey key = socket.register( selector, SelectionKey.OP_ACCEPT );
		requestReader = new HTTPMessageReader( bufferSize );
		this.contextPathMap = contextPathMap;

		startPoll( key );
	}

	private void startPoll(SelectionKey key) {

		System.out.println( "about to poll on server" );
		MessageStat messageStat = null;
		ServerSocketChannel so = null;
		SocketChannel sc = null;

		while ( true ) {
			try {
				if ( key.selector().select() <= 0 ) {
					continue;
				}

				Iterator<SelectionKey> itr = key.selector().selectedKeys().iterator();

				while ( itr.hasNext() ) {

					SelectionKey ready = itr.next();
					itr.remove();

					if ( ready.isValid() && ready.isReadable() ) {
						sc = (SocketChannel) ready.channel();
						messageStat = requestReader.read( sc );

						if ( messageStat.isEndOfMessage() ) {
							handle( sc, messageStat );
							sc.close();
							//TODO need to delete message stat entry ??
							//TODO with this way, Connection: keeyp-alive is meaningless ?
						}
					}
					else if ( ready.isValid() && ready.isAcceptable() ) {

						so = (ServerSocketChannel) key.channel();

						sc = so.accept();
						sc.configureBlocking( false );
						sc.register( selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT );
					}
				}
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
			catch ( Exception ex ) {
				ex.printStackTrace();
			}
		}
	}

	public int getPort() {
		return port;
	}

	/**
	 * Clears contextPathMap.
	 */
	private void clearContextPathMap() {
		contextPathMap.clear();
	}

	/**
	 * Handles request corresponding to the specified context paths.
	 * 
	 * @param socketChannel
	 *            Used to communicate between client and server.
	 * @param messageStat
	 *            Holds necessary request data to act on the context paths.
	 */
	private void handle(SocketChannel socketChannel, MessageStat messageStat) {

		log.debug( "stat: " + messageStat.getHeader().get( RequestHeaderEntry.CONTEXT_PATH ) );
		ContextPathHandler contextPathHandler = getCorrespondingHandler( socketChannel, messageStat );

		if ( contextPathHandler != null ) {
			contextPathHandler.handle( socketChannel, messageStat );
		}
		else {
			//TODO favicon.ico handler is needed
			if(messageStat.getContextPathWithoutQuery().equals("/favicon.ico")){
				log.warn( "currently favicon is not supported. will be done soon." );
			}else{
				contextPathMap.get( "NotFound" ).handle( socketChannel, messageStat );
			}
		}
	}

	private ContextPathHandler getCorrespondingHandler(SocketChannel socketChannel, MessageStat messageStat) {

		ContextPathHandler contextPathHandler = contextPathMap.get( messageStat.getHeader().get(
				RequestHeaderEntry.CONTEXT_PATH ) );

		return contextPathHandler != null ? contextPathHandler : contextPathMap.get( messageStat
				.getContextPathWithoutQuery() );
	}
	
	/**
	 * Adds context path and the corresponding action to the path.
	 * 
	 * @param contextPath
	 *            Context path on the Http request header.
	 * @param contextPathHandler
	 *            Actions on the specified context path.
	 */
	public void addContextPathHandler(String contextPath, ContextPathHandler contextPathHandler) {

		contextPathMap.put( contextPath, contextPathHandler );
	}

	/**
	 * Removes context path and the corresponding action mapping.
	 * 
	 * @param contextPath
	 *            Unique context path.
	 */
	public void removeContextPathHandler(String contextPath) {

		contextPathMap.remove( contextPath );
	}

	/**
	 * Closes connection and clears internal states.
	 */
	public void close() {
		if ( selector.isOpen() ) {
			try {
				selector.close();
			}
			catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if ( socket.isOpen() ) {
			try {
				socket.close();
			}
			catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		clearContextPathMap();
	}
}
