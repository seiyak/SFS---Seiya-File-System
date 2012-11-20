package sfs.entry;

public class HostEntry {

	private String host;
	private int port;
	private int maxTrial;
	private final int DEFAULT_MAX_TRIAL = 30;

	public HostEntry(String host, int port) {
		this.host = host;
		this.port = port;
		this.maxTrial = DEFAULT_MAX_TRIAL;
	}

	public HostEntry(String host, int port, int maxTrial) {
		this.host = host;
		this.port = port;
		this.maxTrial = maxTrial;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxTrial() {
		return maxTrial;
	}

	public void setMaxTrial(int maxTrial) {
		this.maxTrial = maxTrial;
	}

	@Override
	public boolean equals(Object obj) {

		if ( obj instanceof HostEntry ) {

			HostEntry hostEntry = (HostEntry) obj;

			return ( host.equals( hostEntry.getHost() ) ) && ( port == hostEntry.getPort() )
					&& ( maxTrial == hostEntry.getMaxTrial() );
		}

		return false;
	}

	@Override
	public String toString() {
		return "{host=" + host + " port=" + port + " maxTrial=" + maxTrial + "}";
	}
}
