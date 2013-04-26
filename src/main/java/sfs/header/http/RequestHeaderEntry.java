package sfs.header.http;

public class RequestHeaderEntry extends HeaderEntry{

	public static final RequestHeaderEntry REQUEST_HTTP_VERSION = new RequestHeaderEntry( "Http-Version" );
	public static final RequestHeaderEntry CONTEXT_PATH = new RequestHeaderEntry( "Context-Path" );
	public static final RequestHeaderEntry VERB = new RequestHeaderEntry( "Verb" );
	public static final RequestHeaderEntry ACCEPT = new RequestHeaderEntry( "Accept" );
	public static final RequestHeaderEntry GREETING = new RequestHeaderEntry( "Greeting" );
	public static final RequestHeaderEntry HOST = new RequestHeaderEntry( "Host" );
	public static final RequestHeaderEntry ORIGIN = new RequestHeaderEntry( "Origin" );
	public static final RequestHeaderEntry TYPE = new RequestHeaderEntry( "Type" );
	public static final RequestHeaderEntry LIVENESS = new RequestHeaderEntry( "Liveness" );
	public static final RequestHeaderEntry ACCEPT_LANGUAGE = new RequestHeaderEntry( "Accept-Language" );
	public static final RequestHeaderEntry USER_AGENT = new RequestHeaderEntry( "User-Agent" );
	public static final RequestHeaderEntry ACCEPT_ENCODING = new RequestHeaderEntry( "Accept-Encoding" );
	public static final RequestHeaderEntry IF_MODIFIED_SINCE = new RequestHeaderEntry( "If-Modified-Since" );
	public static final RequestHeaderEntry ACCEPT_CHARSET = new RequestHeaderEntry( "Accept-Charset" );
	public static final RequestHeaderEntry REFERER = new RequestHeaderEntry( "Referer" );
	public static final RequestHeaderEntry FROM = new RequestHeaderEntry( "From" );

	public RequestHeaderEntry(String entry) {
		super( entry );
	}

}
