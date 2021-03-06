package sfs.header.http.ending;

public class Ending {

	private final String ending;
	public static final Ending CRLF = new Ending( "\r\n" );
	public static final Ending LF = new Ending( "\n" );

	public Ending(String ending) {

		this.ending = ending;
	}

	@Override
	public String toString() {
		return ending;
	}
}
