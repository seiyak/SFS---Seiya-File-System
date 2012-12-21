package sfs.async.handler.http.reader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

import sfs.stat.message.MessageStat;

public class Server {

	private int count = 0;
	private int port;
	private final ServerSocketChannel socket;
	private final Selector selector;
	private final HTTPMessageReader requestReader;
	private static Logger log = Logger.getLogger( Server.class );

	public Server(String host, int port) throws IOException {
		this.port = port;
		socket = ServerSocketChannel.open();
		socket.configureBlocking( false );
		socket.socket().bind( new InetSocketAddress( host, port ) );
		selector = Selector.open();
		SelectionKey key = socket.register( selector, SelectionKey.OP_ACCEPT );
		requestReader = new HTTPMessageReader( 10 );

		startPoll( key );
	}

	private void startPoll(SelectionKey key) {

		System.out.println( "about to poll on server" );
		MessageStat messageStat = null;

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
						messageStat = requestReader.read( (SocketChannel) ready.channel() );

						if ( messageStat.isEndOfMessage() ) {
							log.debug( "count: " + ( ++count ) + " from: " + ( (SocketChannel) ready.channel() )
									+ " got all the message: " + messageStat.getMessage() );
						}
					}
					else if ( ready.isValid() && ready.isAcceptable() ) {

						ServerSocketChannel so = (ServerSocketChannel) key.channel();

						SocketChannel sc = so.accept();
						sc.configureBlocking( false );
						sc.register( selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT );
					}
				}
			}
			catch ( IOException e ) {
				// TODO Auto-generated catch block
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
}
