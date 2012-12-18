package sfs.header.http;

public class HeaderEntry {

	private final String entry;
	public static final HeaderEntry CONTENT_LENGTH = new HeaderEntry("Content-length");
	public static final HeaderEntry CONTENT_TYPE = new HeaderEntry( "Content-type" );
	public static final HeaderEntry DATE = new HeaderEntry( "Date" );

	public HeaderEntry(String entry) {
		this.entry = entry;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof HeaderEntry ) {

			HeaderEntry headerEntry = (HeaderEntry) obj;

			return entry.equals( headerEntry.toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return entry;
	}
}
