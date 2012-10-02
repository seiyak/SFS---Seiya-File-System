package sfs.header.http.separator;

public class Separator {

	private final String separator;
	public static final Separator COLON = new Separator( ":" );

	public Separator(String separator) {
		this.separator = separator;
	}

	@Override
	public String toString() {
		return separator;
	}
}
