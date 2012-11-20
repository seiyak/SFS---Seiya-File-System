package sfs.entry;

public class StatusEntry {

	private final HostEntry[] hostEntries;
	private final Entry[] status;

	public StatusEntry(HostEntry[] hostEntries, Entry[] status) {
		this.hostEntries = hostEntries;
		this.status = status;
	}

	public HostEntry[] getHostEntries() {
		return hostEntries;
	}

	public Entry[] getStatus() {
		return status;
	}
}
