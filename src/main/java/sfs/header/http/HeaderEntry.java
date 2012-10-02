package sfs.header.http;

public class HeaderEntry {

	private final String entry;
	public static final HeaderEntry CONTENT_TYPE = new HeaderEntry( "Content-Type" );
	public static final HeaderEntry ACCEPT = new HeaderEntry( "Accept" );
	public static final HeaderEntry GREETING = new HeaderEntry( "Greeting" );
	public static final HeaderEntry HOST = new HeaderEntry( "Host" );

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
