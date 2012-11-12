package sfs.structure;

public class Node {

	private String timestamp;
	private String origin;
	private int port;
	private int internal;

	public Node() {

	}

	public Node(String timestamp, String origin, int port, int internal) {
		this.timestamp = timestamp;
		this.origin = origin;
		this.port = port;
		this.internal = internal;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getInternal() {
		return internal;
	}

	public void setInternal(int internal) {
		this.internal = internal;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof Node ) {
			Node node = (Node) obj;

			return timestamp.equals( node.getTimestamp() ) && origin.equals( node.getOrigin() )
					&& port == node.getPort() && internal == node.getInternal();
		}

		return false;
	}

	@Override
	public String toString() {
		return "{timestamp=" + timestamp + " origin=" + origin + " port=" + port + " internal=" + internal + "}";
	}
}
