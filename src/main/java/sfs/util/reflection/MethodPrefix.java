package sfs.util.reflection;

public class MethodPrefix {

	private final String prefix;
	public static final MethodPrefix SET = new MethodPrefix( "set" );

	public MethodPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof MethodPrefix ) {

			MethodPrefix methodPrefix = (MethodPrefix) obj;
			return prefix.equals( methodPrefix.toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return prefix;
	}
}
