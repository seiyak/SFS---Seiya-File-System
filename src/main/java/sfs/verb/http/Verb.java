package sfs.verb.http;

public class Verb {

	private final String verb;
	public static final Verb GET = new Verb( "GET" );
	public static final Verb POST = new Verb( "POST" );
	public static final Verb DELETE = new Verb( "DELETE" );

	public Verb(String verb) {

		this.verb = verb;
	}

	public String getVerb() {
		return verb;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Verb ) {

			Verb v = (Verb) obj;
			return verb.equals( v.getVerb() );
		}

		return false;
	}
}
