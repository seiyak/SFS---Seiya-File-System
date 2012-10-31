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
}
