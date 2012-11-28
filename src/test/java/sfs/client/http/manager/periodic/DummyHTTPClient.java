package sfs.client.http.manager.periodic;

import java.io.IOException;

import org.apache.log4j.Logger;

import sfs.client.http.MultiplexHTTPClient;
import sfs.entry.HostEntry;

public class DummyHTTPClient extends MultiplexHTTPClient {

	private static Logger log = Logger.getLogger( DummyHTTPClient.class );

	public DummyHTTPClient(HostEntry hostEntry) {
		super( hostEntry );
	}

	@Override
	public void initiate() throws IOException {

		log.debug( "do nothing. let the internal server start" );
		setSelector();
		setUpInternalServer();
	}
}
