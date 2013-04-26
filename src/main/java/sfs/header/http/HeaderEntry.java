package sfs.header.http;

public class HeaderEntry {

	private final String entry;
	public static final HeaderEntry CONTENT_LENGTH = new HeaderEntry("Content-length");
	public static final HeaderEntry CONTENT_TYPE = new HeaderEntry( "Content-type" );
	public static final HeaderEntry DATE = new HeaderEntry( "Date" );
	public static final HeaderEntry TRANSFER_ENCODING = new HeaderEntry( "Transfer-Encoding" );
	public static final HeaderEntry CACHE_CONTROL = new HeaderEntry( "Cache-Control" );
	public static final HeaderEntry CONNECTION = new HeaderEntry( "Connection" );
	public static final HeaderEntry COOKIE = new HeaderEntry( "Cookie" );

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
