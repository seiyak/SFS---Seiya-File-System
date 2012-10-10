package sfs.header.http;

public class RequestHeaderEntry extends HeaderEntry{

	public static final RequestHeaderEntry ACCEPT = new RequestHeaderEntry( "Accept" );
	public static final RequestHeaderEntry GREETING = new RequestHeaderEntry( "Greeting" );
	public static final RequestHeaderEntry HOST = new RequestHeaderEntry( "Host" );
	public static final RequestHeaderEntry ORIGIN = new RequestHeaderEntry( "Origin" );
	
	public RequestHeaderEntry(String entry) {
		super( entry );
	}

}
