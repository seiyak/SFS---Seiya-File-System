package sfs.client.http;

import sfs.entry.HostEntry;

public abstract class HTTPClient extends AbstractClient{

	protected HTTPClient(HostEntry hostEntry) {
		super( hostEntry );
	}

}
