package sfs.client.http;

import java.io.IOException;
import java.nio.channels.AlreadyConnectedException;

import org.apache.log4j.Logger;

import sfs.client.http.shortconversation.ShortConversation;
import sfs.entry.HostEntry;

public class SingleplexHTTPClient extends HTTPClient {

	private ShortConversation shortConversation;
	private static Logger log = Logger.getLogger( SingleplexHTTPClient.class );

	public SingleplexHTTPClient(HostEntry hostEntry) {
		super( hostEntry );
	}

	public SingleplexHTTPClient(HostEntry hostEntry, ShortConversation shortConversation) {
		super( hostEntry );
		this.shortConversation = shortConversation;

	}

	public void setShortConversation(ShortConversation shortConversation) {
		this.shortConversation = shortConversation;
	}

	public void initiate() throws IOException {

		if ( shortConversation == null ) {
			log.error( new IllegalArgumentException( "shortConversation property is null. " ) );
		}

		try {
			doInitiate();
		}catch(AlreadyConnectedException ex){
			log.warn( ex );
		}
		catch ( Exception ex ) {
			log.error( ex );
			log.warn( "its parent/the root node seems to be dead, need to change/rotate the parent/root node ..." );
			throw new IllegalArgumentException( "specified parent, "
					+ getServerHost( getHostEntry( 0 ).getHost(), getHostEntry( 0 ).getPort() )
					+ " is not alive. Need to change the parent node" );
		}
	}

	/**
	 * Initiates short conversation.
	 * 
	 * @throws IOException
	 */
	private void doInitiate() throws IOException {

		if ( ( getServerChannel( 0 ) != null ) ) {
			if ( !isConnected( getServerChannel( 0 ) ) ) {
				open( 0, getHostEntry( 0 ) );
			}
		}
		else {
			open( 0, getHostEntry( 0 ) );
		}

		shortConversation.writeRequest( getServerChannel( 0 ), getHostEntry( 0 ) );
	}
}
