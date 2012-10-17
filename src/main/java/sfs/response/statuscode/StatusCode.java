package sfs.response.statuscode;

public final class StatusCode {

	private final Status status;
	public static final StatusCode _100 = new StatusCode( 100, "Continue" );
	public static final StatusCode _101 = new StatusCode( 101, "Switching Protocols" );
	public static final StatusCode _200 = new StatusCode( 200, "OK" );
	public static final StatusCode _201 = new StatusCode( 201, "Created" );
	public static final StatusCode _202 = new StatusCode( 202, "Accepted" );
	public static final StatusCode _203 = new StatusCode( 203, "Non-Authoritative Information" );
	public static final StatusCode _204 = new StatusCode( 204, "No Content" );
	public static final StatusCode _205 = new StatusCode( 205, "Reset Content" );
	public static final StatusCode _206 = new StatusCode( 206, "Partial Content" );
	public static final StatusCode _300 = new StatusCode( 300, "Multiple Choices" );
	public static final StatusCode _301 = new StatusCode( 301, "Moved Permanently" );
	public static final StatusCode _302 = new StatusCode( 302, "Found" );
	public static final StatusCode _303 = new StatusCode( 303, "See Other" );
	public static final StatusCode _304 = new StatusCode( 304, "Not Modified" );
	public static final StatusCode _305 = new StatusCode( 305, "Use Proxy" );
	public static final StatusCode _307 = new StatusCode( 307, "Temporary Redirect" );
	public static final StatusCode _400 = new StatusCode( 400, "Bad Request" );
	public static final StatusCode _401 = new StatusCode( 401, "Unauthorized" );
	public static final StatusCode _402 = new StatusCode( 402, "Payment Required" );
	public static final StatusCode _403 = new StatusCode( 403, "Forbidden" );
	public static final StatusCode _404 = new StatusCode( 404, "Not Found" );
	public static final StatusCode _405 = new StatusCode( 405, "Methd Not Allowed" );
	public static final StatusCode _406 = new StatusCode( 406, "Not Acceptable" );
	public static final StatusCode _407 = new StatusCode( 407, "Proxy Authentication Required" );
	public static final StatusCode _408 = new StatusCode( 408, "Request Time-out" );
	public static final StatusCode _409 = new StatusCode( 409, "Conflict" );
	public static final StatusCode _410 = new StatusCode( 410, "Gone" );
	public static final StatusCode _411 = new StatusCode( 411, "Length Required" );
	public static final StatusCode _412 = new StatusCode( 412, "Precondition Failed" );
	public static final StatusCode _413 = new StatusCode( 413, "Request Entity Too Large" );
	public static final StatusCode _414 = new StatusCode( 414, "Request-URI Too Large" );
	public static final StatusCode _415 = new StatusCode( 415, "Unsupported Media Type" );
	public static final StatusCode _416 = new StatusCode( 416, "Unsupported range not satisfiable" );
	public static final StatusCode _417 = new StatusCode( 417, "Expectation Failed" );
	public static final StatusCode _500 = new StatusCode( 500, "Internal Server Error" );
	public static final StatusCode _501 = new StatusCode( 501, "Not Implemented" );
	public static final StatusCode _502 = new StatusCode( 502, "Bad Gateway" );
	public static final StatusCode _503 = new StatusCode( 503, "Service Unavailable" );
	public static final StatusCode _504 = new StatusCode( 504, "Gateway Time-out" );
	public static final StatusCode _505 = new StatusCode( 505, "HTTP Version not supported" );

	public StatusCode(int number, String string) {

		status = new Status( number, string );
	}

	public int getNumber() {
		return status.getNumber();
	}

	public String getString() {
		return new String( status.getString() );
	}

	@Override
	public boolean equals(Object obj) {
		return status.equals( obj );
	}

	@Override
	public String toString() {
		return status.toString();
	}
}
