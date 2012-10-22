package sfs.request.http;

import sfs.entry.HTTPHeaderEntry;
import sfs.header.http.HTTPHeader;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.WhiteSpace;
import sfs.request.Request;
import sfs.verb.http.Verb;

public class RequestMessage extends Request {

	private static final String HTTP_VERSION = "HTTP/1.1";

	@Override
	protected String doRequest(Verb verb, String resource) {

		return verb.toString() + new WhiteSpace().getSeparator() + resource + new WhiteSpace().getSeparator()
				+ HTTP_VERSION + Ending.CRLF;
	}

	/**
	 * Creates Message with the specified verb, resource and HTTP headers.
	 * @param verb Verb used for the resource.
	 * @param resource Resource where the message to be sent.
	 * @param entries Headers.
	 * @return Message as String.
	 */
	public String createMessage(Verb verb, String resource, HTTPHeaderEntry[] entries) {

		String str = doRequest( verb, resource );
		HTTPHeader header = new HTTPHeader();
		for ( HTTPHeaderEntry entry : entries ) {
			header.put( entry.getKey(), entry.getValue() );
		}

		str += header.format();
		return str;
	}
}
