package sfs.async.handler.http.reader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import sfs.header.http.ending.Ending;

public class Client {

	private final String host;
	private final int port;
	private final SocketChannel socket;
	private final Selector selector;
	private SelectionKey selectedKey;
	private final List<String> ids = new LinkedList<String>();
	private static Logger log = Logger.getLogger( Client.class );

	public Client(String host, int port) throws IOException {
		this.host = host;
		this.port = port;

		InetSocketAddress addr = new InetSocketAddress( host, port );
		socket = SocketChannel.open();
		socket.configureBlocking( false );
		selector = Selector.open();
		socket.connect( addr );
		selectedKey = socket.register( selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ );
	}

	public void starPoll() {
		doStartPoll( selectedKey, socket );
	}

	private void doStartPoll(final SelectionKey key, final SocketChannel socket) {

		new Thread( new Runnable() {

			public void run() {

				try {
					while ( !socket.finishConnect() ) {

					}

					String[] greetings = new String[] {
							"HTTP/1.1 200 OK" + Ending.CRLF.toString() + "Content-type: application/json"
									+ Ending.CRLF.toString() + "Content-le",
							"ngth: 45" + Ending.CRLF.toString() + "Date: Mon, Oct 22, 2012 08:15:",
							"05 PM GMT" + Ending.CRLF.toString(), "Greeting-back: true" + Ending.CRLF.toString(),
							Ending.CRLF.toString() + "{\"status\": \"OK\",\"message\": \"Welcome to SFS!\"}" };

					for ( String greeting : greetings ) {
						socket.write( ByteBuffer.wrap( greeting.getBytes() ) );
					}
				}
				catch ( IOException e1 ) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				while ( true ) {
					try {

						if ( selector.select() <= 0 ) {
							continue;
						}
					}
					catch ( IOException e1 ) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					Iterator<SelectionKey> itr = selector.selectedKeys().iterator();
					ByteBuffer buffer = ByteBuffer.allocate( 29 );
					SocketChannel channel = null;
					while ( itr.hasNext() ) {
						SelectionKey ready = itr.next();

						if ( ready.isReadable() ) {
							log.debug( "readable" );
						}
						else if ( ready.isConnectable() ) {
							log.debug( "connectable" );
						}

						itr.remove();
					}
				}
			}

		} ).start();
	}

	public void write(String str) {
		try {
			if ( socket.isOpen() ) {
				socket.write( ByteBuffer.wrap( str.getBytes() ) );
			}
			else {
				log.warn( "schannel is already closed" );
			}
		}
		catch ( IOException e ) {
			log.error( e );
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public List<String> getIds() {
		return ids;
	}
}
