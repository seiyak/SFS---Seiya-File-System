package sfs.mime;

import java.util.Map;

import sfs.util.reflection.ReflectionUtil;

public class Mime {

	private final String type;
	public static final Mime JSON = new Mime( "application/json" );
	public static final Mime HTML = new Mime( "text/html" );
	public static final Mime ANY = new Mime( "*/*" );
	public static final Mime TEXT = new Mime( "text/plain" );
	public static final Mime NULL = new Mime("null");
	public static Map MIMES;

	static{
		MIMES = ReflectionUtil.getStaticMembers( Mime.class, false );
	}

	public Mime(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Mime ) {

			Mime mime = (Mime) obj;
			return type.equals( mime.toString() );
		}

		return false;
	}

	@Override
	public String toString() {

		return type;
	}
}
