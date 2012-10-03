package sfs.header.http;

import java.util.Map.Entry;

import sfs.header.Header;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.Separator;

public class RequestHeader extends Header {

	/**
	 * Formats header and return it as String.
	 */
	protected String doFormat() {

		String headerEntries = "";

		for ( Entry<HeaderEntry, Object> entry : getHeader().entrySet() ) {

			headerEntries += entry.getKey() + Separator.putColonAndWhiteSpace() + entry.getValue().toString()
					+ Ending.CRLF;
		}

		// Following the HTTP header format.
		headerEntries += Ending.CRLF;

		return headerEntries;
	}
}
