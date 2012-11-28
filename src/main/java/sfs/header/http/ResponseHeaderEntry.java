package sfs.header.http;

public class ResponseHeaderEntry extends HeaderEntry {

	public static final ResponseHeaderEntry CONTENT_LENGTH = new ResponseHeaderEntry("Content-length");
	public static final ResponseHeaderEntry LIVENESS_BACK = new ResponseHeaderEntry( "Liveness-back" );
	public static final ResponseHeaderEntry GREETING_BACK = new ResponseHeaderEntry( "Greeting-back" );

	public ResponseHeaderEntry(String entry) {
		super( entry );
	}
}
