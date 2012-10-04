package sfs.header.http;

import java.util.Map.Entry;

import sfs.header.Header;
import sfs.header.http.ending.Ending;
import sfs.header.http.separator.Colon;
import sfs.header.http.separator.WhiteSpace;

public class ResponseHeader extends Header {

	@Override
	protected String doFormat() {

		String headerEntries = "";

		for ( Entry<HeaderEntry, Object> entry : getHeader().entrySet() ) {

			headerEntries += entry.getKey() + new Colon().add( new WhiteSpace() ).toString()
					+ entry.getValue().toString() + Ending.LF;
		}

		return headerEntries;
	}

}
