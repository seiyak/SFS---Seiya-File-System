package sfs.header.http;

public class ResponseHeaderEntry extends HeaderEntry {

	public static final ResponseHeaderEntry CONTENT_LENGTH = new ResponseHeaderEntry("Content-Length");
	public static final ResponseHeaderEntry LIVENESS_BACK = new ResponseHeaderEntry( "Liveness-Back" );

	public ResponseHeaderEntry(String entry) {
		super( entry );
	}
}
