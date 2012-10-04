package sfs.header.http;

public class ResponseHeaderEntry extends HeaderEntry {

	public static final ResponseHeaderEntry HTTP_VERSION = new ResponseHeaderEntry( "HTTP-Version" );
	public static final ResponseHeaderEntry STATUS_CODE = new ResponseHeaderEntry( "Status-Code" );
	public static final ResponseHeaderEntry REASON_PHRASE = new ResponseHeaderEntry( "Reason-Phrase" );

	public ResponseHeaderEntry(String entry) {
		super( entry );
	}
}
