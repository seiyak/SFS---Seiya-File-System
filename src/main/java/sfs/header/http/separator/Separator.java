package sfs.header.http.separator;

public class Separator {

	/**
	 * TODO Use composite pattern to take care of concatenations of other separators.
	 */
	private final String separator;
	public static final Separator COLON = new Separator( ":" );
	public static final Separator WHITE_SPACE = new Separator(" ");
	public static final Separator SLASH = new Separator("/");

	public Separator(String separator) {
		this.separator = separator;
	}

	/**
	 * Puts ': '. This is a convenient method to separate key and value on HTTP request header.
	 * 
	 * @return Colon and a whitespace like ': '.
	 */
	public static final String putColonAndWhiteSpace() {
		return COLON.toString() + WHITE_SPACE.toString();
	}

	@Override
	public String toString() {
		return separator;
	}
}
