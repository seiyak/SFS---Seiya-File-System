package sfs.header.http;

public class RequestHeaderEntry extends HeaderEntry{

	public static final RequestHeaderEntry ACCEPT = new RequestHeaderEntry( "Accept" );
	public static final RequestHeaderEntry GREETING = new RequestHeaderEntry( "Greeting" );
	public static final RequestHeaderEntry HOST = new RequestHeaderEntry( "Host" );
	public static final RequestHeaderEntry ORIGIN = new RequestHeaderEntry( "Origin" );
	public static final RequestHeaderEntry TYPE = new RequestHeaderEntry( "Type" );
	public static final RequestHeaderEntry LIVENESS = new RequestHeaderEntry( "Liveness" );
	public static final RequestHeaderEntry ACCEPT_LANGUAGE = new RequestHeaderEntry( "Accept-Language" );
	public static final RequestHeaderEntry USER_AGENT = new RequestHeaderEntry( "User-Agent" );
	public static final RequestHeaderEntry ACCEPT_ENCODING = new RequestHeaderEntry( "Accept-Encoding" );

	public RequestHeaderEntry(String entry) {
		super( entry );
	}

}
