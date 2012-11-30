package sfs.entry;

public class NullStatusEntry implements StatusEntryable {

	public HostEntry[] getHostEntries() {
		return new HostEntry[] { null };
	}

	public Entry[] getStatus() {
		return new Entry[] { null };
	}

}
