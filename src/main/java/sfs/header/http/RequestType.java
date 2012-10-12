package sfs.header.http;

public class RequestType {

	private final String type;

	public static RequestType CPU_MEMORY_USAGE = new RequestType( "CPU_MEMORY_USAGE" );

	public RequestType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof RequestType ) {

			RequestType t = (RequestType) obj;
			return t.toString().equals( toString() );
		}

		return false;
	}

	@Override
	public String toString() {
		return type;
	}
}
