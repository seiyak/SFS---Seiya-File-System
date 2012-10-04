package sfs.request.http;

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

}