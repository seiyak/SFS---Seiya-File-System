package sfs.entry;

import sfs.header.http.HeaderEntry;

public class HTTPHeaderEntry {

	private final HeaderEntry key;
	private final Object value;

	public HTTPHeaderEntry(HeaderEntry key, Object value) {
		this.key = key;
		this.value = value;
	}

	public HeaderEntry getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof HTTPHeaderEntry ) {

			HTTPHeaderEntry header = (HTTPHeaderEntry) obj;
			return key.toString().equals( header.getKey().toString() )
					&& value.toString().equals( header.getValue().toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return "{key=" + key + " value=" + value + "}";
	}
}
